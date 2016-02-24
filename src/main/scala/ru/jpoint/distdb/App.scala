package ru.jpoint.distdb

import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import org.slf4j.LoggerFactory
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._
/**
  * Created by shutty on 11/16/15.
  */
object App {

  def main(args: Array[String]): Unit = {
    val dsys = new MasterSlaveAsync()
    dsys.start
  }
}
