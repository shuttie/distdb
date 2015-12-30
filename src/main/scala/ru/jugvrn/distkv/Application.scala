package ru.jugvrn.distkv

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpEntity, HttpMethods, HttpRequest}
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._
/**
  * Created by shutty on 11/16/15.
  */
object Application {

  case object Get
  case class Put(value:String)

  def main(args: Array[String]) {
    implicit val system = ActorSystem.create("distkv")
    implicit val mat = ActorMaterializer()

    val hosts = List("localhost:8881", "localhost:8882", "localhost:8883", "localhost:8884", "localhost:8885")
    val http = Http(system)
    var value = "<empty>"

    val route = path("db") {
      get {
        complete {
          value
        }
      } ~ post {
        entity(as[String]) { data =>
          complete {
            hosts.map( host => http.singleRequest(HttpRequest(uri = s"http://$host/db", method = HttpMethods.PUT, entity = HttpEntity(data))))
            data
          }
        }
      } ~ put {
        entity(as[String]) { data =>
          complete {
            value = data
            data
          }
        }
      }
    }

    Http().bindAndHandle(route, "localhost", args.head.toInt)

    Await.result(system.whenTerminated, Duration.Inf)
  }
}
