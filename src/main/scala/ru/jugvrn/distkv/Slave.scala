package ru.jugvrn.distkv

import akka.actor.{Actor, ActorLogging}
import akka.cluster.pubsub.DistributedPubSubMediator.{SubscribeAck, Subscribe}
import akka.cluster.pubsub.DistributedPubSub
import better.files.File
import org.mapdb.DBMaker
import ru.jugvrn.distkv.Application.{Get, Put}

/**
  * Created by shutty on 11/16/15.
  */

class Slave extends Node {

  broadcast ! Subscribe("data", self)

  def receive = {
    case SubscribeAck(Subscribe(topic, _, _)) =>
      log.info(s"slave subscribed to $topic")

    case Put(data) =>
      write("/tmp/slave.db", data)
      log.info(s"stored ($data)")

    case Get =>
      val result = read("/tmp/slave.db")
      sender() ! result
      log.info(s"read ($result)")
  }
}
