package ru.jugvrn.distkv

import java.io.File
import akka.actor.{Actor, ActorLogging}
import akka.cluster.pubsub.DistributedPubSubMediator.{SubscribeAck, Subscribe}
import akka.cluster.pubsub.DistributedPubSub
import org.mapdb.DBMaker
import ru.jugvrn.distkv.Application.{Get, Put}

/**
  * Created by shutty on 11/16/15.
  */

class Node extends Actor with ActorLogging {
  val db = DBMaker.newFileDB(new File("/tmp/db.dat")).closeOnJvmShutdown().make()
  val data = db.getHashMap[String,String]("data")

  val mediator = DistributedPubSub(context.system).mediator
  mediator ! Subscribe("data", self)

  def receive = {
    case SubscribeAck(Subscribe(topic, _, _)) =>
      log.info(s"worker subscribed to $topic")

    case Put(k, v) =>
      data.put(k, v)
      db.commit()
      log.info(s"stored ($k, $v)")

    case Get(k) =>
      val result = Option(data.get(k)).getOrElse("<empty>")
      sender() ! result
      log.info(s"read ($k, $result)")
  }
}
