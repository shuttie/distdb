package ru.jpoint.distkv

import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._
/**
  * Created by shutty on 11/16/15.
  */
object Application extends Logging {

  object Config {
    def isMaster = sys.env("MASTER").toBoolean
    def slaves = sys.env("SLAVES").split(",").toList
  }

  var value:String = "zero"

  def main(args: Array[String]) {
    implicit val system = ActorSystem.create("distkv")
    implicit val mat = ActorMaterializer()
    val http = Http(system)

    val route = path("db") {
      get {
        complete {
          log.info(s"read, result=$value")
          value
        }
      } ~ post {
        entity(as[String]) { data =>
          complete {
            log.info(s"write, before=$value, after=$data")
            value = data
            if (Config.isMaster) {
              log.info(s"replicating write to slaves: ${Config.slaves}")
              Config.slaves.foreach( host =>
                http.singleRequest(HttpRequest(
                  uri = s"http://$host:8000/db",
                  method = HttpMethods.POST,
                  entity = HttpEntity(data))))

            }
            HttpResponse(StatusCodes.OK)
          }
        }
      }
    }

    Http().bindAndHandle(route, "0.0.0.0", 8000)
    log.info("Service started")
    Await.result(system.whenTerminated, Duration.Inf)
  }
}
