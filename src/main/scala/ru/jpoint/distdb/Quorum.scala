package ru.jpoint.distdb

import akka.http.scaladsl.model.{StatusCodes, HttpResponse}
import scala.concurrent.Future

class Quorum extends RestfulServer {
  def quorumSize = Math.ceil(nodes.size / 2.0)

  def handleQuorum(responses: List[HttpResponse]) =
    Future.sequence(responses.map(parseResponse)).map(_
      .groupBy(x => x)
      .toList
      .map(item => item._1 -> item._2.size)
      .sortBy(_._2)
      .headOption)

  def buildResponse(quorumResponse: Option[(String, Int)]) = quorumResponse match {
    case Some((quorumValue, numberVotes)) if numberVotes >= quorumSize =>
      log.info(s"quorum: $quorumValue with $numberVotes votes (of $quorumSize)")
      HttpResponse(StatusCodes.OK, entity = quorumValue)
    case other =>
      HttpResponse(StatusCodes.Conflict, entity = s"quorum response: $other")
  }


  def read =
    Future.sequence(nodes.map(node => httpRead(node)))
      .flatMap(handleQuorum)
      .map(buildResponse)

  def write(data: String) = {
    Future.sequence(nodes.map(node => httpWrite(node, data)))
      .flatMap(handleQuorum)
      .map(buildResponse)
  }

}
