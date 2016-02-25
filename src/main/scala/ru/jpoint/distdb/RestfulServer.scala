package ru.jpoint.distdb

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by shutty on 2/18/16.
  */
trait RestfulServer extends HttpUtils {

  def read: Future[HttpResponse]
  def write(value: String): Future[HttpResponse]

  var value:String = "0"
  val hostname = sys.env("HOSTNAME")
  val nodes = sys.env("NODES")
    .split(",")
    .toList
    .filter(_.nonEmpty)

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
