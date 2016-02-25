package ru.jpoint.distdb

import akka.http.scaladsl.model._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by shutty on 2/19/16.
  */
class MasterSlaveSync extends MasterSlave {
  override def write(data:String) = {
    log.info(s"write, before=$value, after=$data")
    value = data

    log.info(s"replicating write to slaves: $slaves")
    Future.sequence(slaves
      .map(node => httpWrite(node, data)))
      .map(responses => responses
        .find(_.status != StatusCodes.OK)
        .getOrElse(HttpResponse(StatusCodes.OK, entity = value)))
  }
}
