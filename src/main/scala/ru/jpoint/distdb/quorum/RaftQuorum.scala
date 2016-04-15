package ru.jpoint.distdb.quorum

import java.io.File
import java.util.function.Supplier

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import io.atomix.catalyst.transport.{Address, NettyTransport}
import io.atomix.copycat.server.storage.{Storage, StorageLevel}
import io.atomix.copycat.server.{Commit, CopycatServer, StateMachine}
import io.atomix.copycat.{Command, Query}
import ru.jpoint.distdb.RestfulServer

import scala.collection.JavaConversions._
import scala.concurrent.Future

/**
  * Created by shutty on 4/15/16.
  */


class RaftQuorum extends RestfulServer {
  val server = new RaftServer(hostname)
  val client = new RaftClient(nodes.map(new Address(_, 5000)))

  def read = client.read.map(data => HttpResponse(StatusCodes.OK, entity = data))

  def write(value: String) = client.write(value).map(data => HttpResponse(StatusCodes.OK, entity = value))

}
