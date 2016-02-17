package ru.jpoint.distdb

import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import org.slf4j.LoggerFactory
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._
/**
  * Created by shutty on 11/16/15.
  */
object App {
  lazy val log = LoggerFactory.getLogger(getClass)

  object Config {
    def isMaster = sys.env("MASTER").toBoolean
    def slaves = sys.env("SLAVES").split(",").toList
  }

  def main(args: Array[String]) {
    implicit val system = ActorSystem.create("distkv")
    implicit val mat = ActorMaterializer()
    val http = Http(system)

    var value:String = "0"

    val route = path("db") {
      get {
        complete {
          log.info(s"read, result=$value")
          value
        }
      } ~ post {
        entity(as[String]) { data =>
          complete {
            if (Config.isMaster) {
              log.info(s"write, before=$value, after=$data")
              value = data
              log.info(s"replicating write to slaves: ${Config.slaves}")
              Config.slaves.foreach( host =>
                http.singleRequest(HttpRequest(
                  uri = s"http://$host:8000/db",
                  method = HttpMethods.POST,
                  entity = HttpEntity(data))))

              HttpResponse(StatusCodes.OK)
            } else {
              HttpResponse(StatusCodes.BadRequest)
            }
          }
        }
      }
    }

    Http().bindAndHandle(route, "0.0.0.0", 8000)
    log.info("Service started")
    Await.result(system.whenTerminated, Duration.Inf)
  }
}
