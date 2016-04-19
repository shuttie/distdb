package ru.jpoint.distdb.ms

import akka.http.scaladsl.model._

import scala.concurrent.Future

/**
  * Created by shutty on 2/19/16.
  */
class MasterSlaveSync extends MasterSlave {
  override def write(data: String) = {
    logger.info(s"write, before=$value, after=$data")
    value = data

    logger.info(s"replicating write to slaves: $slaves")
    Future.sequence(slaves
      .map(node => httpWrite(node, data)))
      .map(responses => responses
        .find(_.status != StatusCodes.OK)
        .getOrElse(HttpResponse(StatusCodes.OK, entity = value)))
  }

}
