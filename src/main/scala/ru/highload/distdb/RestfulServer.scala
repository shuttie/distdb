package ru.highload.distdb

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait RestfulServer extends HttpUtils {
  var value: String = "0"
  val hostname = sys.env("HOSTNAME")
  val nodes = sys.env("NODES")
    .split(",")
    .toList
    .filter(_.nonEmpty)
  val slaves = nodes.filter(_ != hostname)

  def read: Future[HttpResponse]

  def write(value: String): Future[HttpResponse]

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
    } ~ path("local") {
      get {
        complete {
          logger.info(s"local read: $value")
          HttpResponse(StatusCodes.OK, entity = value)
        }
      } ~ post {
        entity(as[String]) { data =>
          complete {
            value = data
            logger.info(s"local commit: $data")
            HttpResponse(StatusCodes.OK, entity = value)
          }
        }
      }
    }

    http.bindAndHandle(route, "0.0.0.0", 8000)
    logger.info("Service started")
    Await.result(system.whenTerminated, Duration.Inf)
  }

}
