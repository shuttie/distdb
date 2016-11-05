package ru.highload.distdb.ms

import akka.http.scaladsl.model._
import ru.highload.distdb.RestfulServer

import scala.concurrent.Future

/**
  * Created by shutty on 2/18/16.
  */
class MasterSlave extends RestfulServer {
  def read = {
    logger.info(s"read, result=$value")
    Future.successful(HttpResponse(StatusCodes.OK, entity = value))
  }

  def write(data: String) = {
    logger.info(s"write, before=$value, after=$data")
    value = data
    logger.info(s"replicating write to slaves: $slaves")
    slaves.foreach(node => httpWrite(node, data))
    Future.successful(HttpResponse(StatusCodes.OK, entity = value))
  }
}
