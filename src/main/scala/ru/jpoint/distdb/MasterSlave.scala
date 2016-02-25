package ru.jpoint.distdb

import akka.http.scaladsl.model._
import scala.concurrent.Future

/**
  * Created by shutty on 2/18/16.
  */
class MasterSlave extends RESTfulRegister {
  var value:String = "0"
  val hostname = sys.env("HOSTNAME")
  val slaves = sys.env("NODES")
    .split(",")
    .toList
    .filter(_.nonEmpty)
    .filterNot(_ == hostname)

  def read = {
    log.info(s"read, result=$value")
    Future.successful(HttpResponse(StatusCodes.OK, entity = HttpEntity(value)))
  }

  def write(data:String) = {
    log.info(s"write, before=$value, after=$data")
    value = data
    log.info(s"replicating write to slaves: $slaves")
    slaves.foreach(slave => httpWrite(slave, data))
    Future.successful(HttpResponse(StatusCodes.OK))
  }

}
