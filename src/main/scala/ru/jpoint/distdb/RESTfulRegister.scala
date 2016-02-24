package ru.jpoint.distdb

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import org.slf4j.LoggerFactory
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
  * Created by shutty on 2/18/16.
  */
trait RESTfulRegister {
  lazy val log = LoggerFactory.getLogger(getClass)
  implicit val system = ActorSystem.create("distdb")
  implicit val mat = ActorMaterializer()
  val http = Http(system)

  def read:Future[HttpResponse]
  def write(value:String):Future[HttpResponse]

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
