package ru.jugvrn.distkv

import akka.cluster.pubsub.DistributedPubSubMediator.{SubscribeAck, Subscribe, Publish}
import ru.jugvrn.distkv.Application.{Get, Put}

/**
  * Created by shutty on 11/21/15.
  */
class Master extends Node {

  broadcast ! Subscribe("data", self)

  def receive = {
    case SubscribeAck(Subscribe(topic, _, _)) =>
      log.info(s"subscribed to $topic")

    case Get =>
      val result = read("/tmp/master.db")
      sender() ! result
      log.info(s"read ($result)")

    case put @ Put(data) if sender() != self =>
      write("/tmp/master.db",data)
      log.info(s"stored ($data)")
      broadcast ! Publish("data", put)
      sender() ! "ok"
  }
}
