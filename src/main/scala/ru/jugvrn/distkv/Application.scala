package ru.jugvrn.distkv

import akka.actor.{PoisonPill, Props, ActorSystem}
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.Publish
import akka.cluster.singleton.{ClusterSingletonManagerSettings, ClusterSingletonManager}
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

  case object Get
  case class Put(value:String)

  def main(args: Array[String]) {
    implicit val system = ActorSystem.create("distkv")
    implicit val mat = ActorMaterializer()
    implicit val timeout = Timeout(1.second)

    val worker = system.actorOf(Props(classOf[Slave]), name = "worker")

    system.actorOf(ClusterSingletonManager.props(
      singletonProps = Props(classOf[Master]),
      terminationMessage = PoisonPill,
      settings = ClusterSingletonManagerSettings(system)),
      name = "master")

    val master = Node.masterFor(system)

    val route = path("db") {
      get {
        complete {
          worker.ask(Get).mapTo[String]
        }
      } ~ put {
        entity(as[String]) { data =>
          complete {
            master.ask(Put(data)).mapTo[String]
          }
        }
      }
    }

    Http().bindAndHandle(route, "localhost", 8888)

    Await.result(system.whenTerminated, Duration.Inf)
  }
}
