package ru.jpoint.distdb

import akka.http.scaladsl.model.{StatusCodes, HttpResponse}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by shutty on 2/25/16.
  */
class Quorum extends RestfulServer {
  def handleQuorum(responses:List[HttpResponse]) =
    Future.sequence(responses.map(parseResponse)).map(_
      .groupBy(x => x)
      .toList
      .map(item => item._1 -> item._2.size)
      .sortBy(_._2)
      .headOption)

  def buildResponse(quorumResponse:Option[(String, Int)]) = quorumResponse match {
    case Some((quorumValue, numberVotes)) if numberVotes >= 3 =>
      HttpResponse(StatusCodes.OK, entity = quorumValue)
    case _ =>
      HttpResponse(StatusCodes.InternalServerError)
  }

  def read =
    Future.sequence(nodes.map(node => httpRead(node)))
      .flatMap(handleQuorum)
      .map(buildResponse)

  def write(value: String) =
    Future.sequence(nodes.map(node => httpWrite(node, value)))
      .flatMap(handleQuorum)
      .map(buildResponse)
}
