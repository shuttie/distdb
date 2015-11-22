package ru.jugvrn.distkv

import akka.actor._
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.singleton._
import better.files._

/**
  * Created by shutty on 11/21/15.
  */
trait Node extends Actor with ActorLogging {
  val master = Node.masterFor(context.system)

  val broadcast = DistributedPubSub(context.system).mediator


  def write(fileName:String, data:String) = {
    val file = File(fileName)
    file.overwrite(data)
  }

  def read(fileName:String) = {
    val file = File(fileName)
    if (file.exists) file.contentAsString else "<empty>"
  }
}

object Node {
  def masterFor(system:ActorSystem) = system.actorOf(ClusterSingletonProxy.props(
    singletonManagerPath = "/user/master",
    settings = ClusterSingletonProxySettings(system)))

}
