package ru.jpoint.distdb

import akka.http.scaladsl.model._
import scala.concurrent.Future

/**
  * Created by shutty on 2/18/16.
  */
class MasterSlave extends RestfulServer {
  def read = {
    log.info(s"read, result=$storedValue")
    Future.successful(HttpResponse(StatusCodes.OK, entity = storedValue))
  }

  def write(data: String) = {
    log.info(s"write, before=$storedValue, after=$data")
    storedValue = data
    log.info(s"replicating write to slaves: $slaves")
    slaves.foreach(node => httpWrite(node, data))
    Future.successful(HttpResponse(StatusCodes.OK, entity = storedValue))
  }

}
