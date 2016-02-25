package ru.jpoint.distdb

import akka.http.scaladsl.model._
import scala.concurrent.Future

/**
  * Created by shutty on 2/18/16.
  */
class MasterSlave extends HttpRegister {
  def read = {
    log.info(s"read, result=$value")
    Future.successful(HttpResponse(StatusCodes.OK, entity = HttpEntity(value)))
  }

  def write(data:String) = {
    log.info(s"write, before=$value, after=$data")
    value = data
    log.info(s"replicating write to slaves: $nodes")
    nodes.foreach(node => httpWrite(node, data))
    Future.successful(HttpResponse(StatusCodes.OK))
  }

}
