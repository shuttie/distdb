package ru.jpoint.distdb

import akka.http.scaladsl.model._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by shutty on 2/19/16.
  */
class MasterSlaveSync extends MasterSlave {
  override def write(value: String) = {
    log.info(s"write, before=$storedValue, after=$value")
    storedValue = value

    log.info(s"replicating write to slaves: $slaves")
    Future.sequence(slaves
      .map(node => httpWrite(node, value)))
      .map(_
        .find(_.status != StatusCodes.OK)
        .getOrElse(HttpResponse(StatusCodes.OK, entity = storedValue)))
  }

}
