package ru.jpoint.distdb

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpResponse, HttpMethods, HttpRequest}
import akka.stream.ActorMaterializer
import akka.util.ByteString
import org.slf4j.LoggerFactory
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by shutty on 2/25/16.
  */
trait HttpUtils {
  lazy val log = LoggerFactory.getLogger(getClass)
  implicit val system = ActorSystem.create("distdb")
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








