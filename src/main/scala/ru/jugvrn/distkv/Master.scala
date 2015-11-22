package ru.jugvrn.distkv

import akka.cluster.pubsub.DistributedPubSubMediator.Publish
import ru.jugvrn.distkv.Application.Put

/**
  * Created by shutty on 11/21/15.
  */
class Master extends Node {
  def receive = {
    case put @ Put(data) =>
      write("/tmp/master.db",data)
      log.info(s"master stored ($data)")
      broadcast ! Publish("data", put)
      sender() ! "ok"
  }
}
