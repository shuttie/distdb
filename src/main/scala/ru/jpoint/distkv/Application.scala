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
  var value:String = "zero"

  def main(args: Array[String]) {
    val conf = Config.parse(args)
    log.info("Starting distkv")
    implicit val system = ActorSystem.create("distkv")
    implicit val mat = ActorMaterializer()
    val http = Http(system)

    val route = path("db") {
      get {
        complete {
          value
        }
      } ~ post {
        entity(as[String]) { data =>
          complete {
            if (conf.isMaster) {
              value = data
              conf.slaves.foreach( host => http.singleRequest(HttpRequest(uri = s"http://$host/db", method = HttpMethods.POST, entity = HttpEntity(data))))
              HttpResponse(StatusCodes.OK)
            } else {
              HttpResponse(StatusCodes.BadRequest)
            }
          }
        }
      }
    }

    Http().bindAndHandle(route, "localhost", 8000)

    Await.result(system.whenTerminated, Duration.Inf)
  }
}
