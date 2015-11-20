package ru.jugvrn.distkv

import akka.actor.{Props, ActorSystem}
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.Publish
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.Await
import scala.concurrent.duration._
/**
  * Created by shutty on 11/16/15.
  */
object Application {

  case class Get(key:String)
  case class Put(key:String, value:String)

  def main(args: Array[String]) {
    implicit val system = ActorSystem.create("distkv")
    implicit val mat = ActorMaterializer()
    implicit val timeout = Timeout(1.second)

    val worker = system.actorOf(Props(classOf[Node]), name = "worker")
    val mediator = DistributedPubSub(system).mediator


    val route = path("db" / Segment) { key =>
      get {
        complete {
          worker.ask(Get(key)).mapTo[String]
        }
      } ~
        put {
          entity(as[String]) { data =>
            complete {
              mediator ! Publish("data", Put(key, data))
              "ok"
            }
          }
        }
    } ~ path("stop") {
      get {
        complete {
          system.terminate()
          "kansas is going byebye"
        }
      }
    }


    Http().bindAndHandle(route, "localhost", 8888)

    Await.result(system.whenTerminated, Duration.Inf)
  }
}
