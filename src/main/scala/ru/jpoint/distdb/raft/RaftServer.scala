package ru.jpoint.distdb.raft

import java.io.File
import java.util.function.Supplier

import io.atomix.catalyst.transport.{Address, NettyTransport}
import io.atomix.copycat.server.storage.{Storage, StorageLevel}
import io.atomix.copycat.server.{Commit, CopycatServer, StateMachine}
import io.atomix.copycat.{Command, Query}
import scala.collection.JavaConversions._

/**
  * Created by shutty on 4/15/16.
  */
case class SetCommand(value:String) extends Command[String]
case class GetQuery() extends Query[String]

class RegisterStateMachine extends StateMachine {
  var value:String = "0"
  def put(commit:Commit[SetCommand]) = try { value = commit.operation().value } finally { commit.close() }
  def get(commit:Commit[GetQuery]) = try { value } finally { commit.close() }
}

class RaftServer(hostname:String) {
  val address = new Address(hostname, 5000)
  val server = CopycatServer.builder(address)
    .withStateMachine(new Supplier[StateMachine] { def get = new RegisterStateMachine })
    .withTransport(NettyTransport.builder().build())
    .withStorage(Storage.builder().withDirectory(new File("logs")).withStorageLevel(StorageLevel.DISK).build())
    .build()
  server.serializer().register(SetCommand.getClass)
  server.serializer().register(GetQuery.getClass)

  if (hostname == "n1") {
    server.bootstrap().join()
  } else {
    server.join(List(new Address("n1", 5000))).join()
  }
}
