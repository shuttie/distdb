package ru.jpoint.distdb

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse}
import akka.stream.ActorMaterializer
import akka.util.ByteString
import com.typesafe.scalalogging.LazyLogging
import org.slf4j.LoggerFactory

trait HttpUtils extends LazyLogging {
  implicit val system = ActorSystem.create("distdb")
  implicit val executor = system.dispatcher
  implicit val mat = ActorMaterializer()
  val http = Http(system)

  def httpWrite(node: String, data: String) =
    http.singleRequest(HttpRequest(
      uri = s"http://$node:8000/local",
      method = HttpMethods.POST,
      entity = data))

  def httpRead(node: String) =
    http.singleRequest(
      HttpRequest(
        uri = s"http://$node:8000/local",
        method = HttpMethods.GET))

  def parseResponse(response: HttpResponse) = response
    .entity
    .dataBytes
    .runFold(ByteString(""))(_ ++ _)
    .map(_.utf8String)

}








