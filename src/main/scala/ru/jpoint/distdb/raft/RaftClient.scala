package ru.jpoint.distdb.raft

import io.atomix.catalyst.transport.{Address, NettyTransport}
import io.atomix.copycat.client.CopycatClient
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._
import scala.compat.java8.FutureConverters._
/**
  * Created by shutty on 4/15/16.
  */
class RaftClient(hosts:List[Address]) {
  lazy val log = LoggerFactory.getLogger(getClass)
  val client = CopycatClient.builder(hosts)
    .withTransport(NettyTransport.builder().build())
    .build()
  client.serializer().register(SetCommand.getClass)
  client.serializer().register(GetQuery.getClass)
  client.connect().join()

  def write(value:String) = {
    log.debug(s"raft write: $value")
    client.submit(new SetCommand(value)).toScala
  }
  def read = {
    log.debug("raft read")
    client.submit(new GetQuery()).toScala
  }
}
