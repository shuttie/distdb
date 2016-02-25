package ru.jpoint.distdb

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.util.ByteString
import org.slf4j.LoggerFactory
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by shutty on 2/18/16.
  */
trait RESTfulRegister {

  def read:Future[HttpResponse]
  def write(value:String):Future[HttpResponse]

  lazy val log = LoggerFactory.getLogger(getClass)
  implicit val system = ActorSystem.create("distdb")
  implicit val mat = ActorMaterializer()
  val http = Http(system)

  var value:String = "0"
  val hostname = sys.env("HOSTNAME")
  val nodes = sys.env("NODES")
    .split(",")
    .toList
    .filter(_.nonEmpty)
    .filterNot(_ == hostname)

  def httpWrite(slave:String, data:String) =
    http.singleRequest(HttpRequest(
      uri = s"http://$slave:8000/db",
      method = HttpMethods.POST,
      entity = HttpEntity(data)))

  def httpRead(slave:String) = http
      .singleRequest(HttpRequest(
        uri = s"http://$slave:8000/db",
        method = HttpMethods.GET))
      .flatMap(response => response.entity.dataBytes.runFold(ByteString(""))(_ ++ _)) // wtf?
      .map(_.utf8String)

  def start = {

    val route = path("db") {
      get {
        complete {
          read
        }
      } ~ post {
        entity(as[String]) { data =>
          complete {
            write(data)
          }
        }
      }
    }

    http.bindAndHandle(route, "0.0.0.0", 8000)
    log.info("Service started")
    Await.result(system.whenTerminated, Duration.Inf)
  }
}
