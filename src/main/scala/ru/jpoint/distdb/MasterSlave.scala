package ru.jpoint.distdb

import akka.http.scaladsl.model._
import scala.concurrent.Future

/**
  * Created by shutty on 2/18/16.
  */
class MasterSlave extends RESTfulRegister {
  var value:String = "0"
  def slaves = sys.env("SLAVES").split(",").toList.filter(_.nonEmpty)

  def httpWrite(slave:String, data:String) =
    http.singleRequest(HttpRequest(
      uri = s"http://$slave:8000/db",
      method = HttpMethods.POST,
      entity = HttpEntity(data)))

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
