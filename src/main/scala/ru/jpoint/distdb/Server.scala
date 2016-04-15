package ru.jpoint.distdb

import ru.jpoint.distdb.quorum.RaftQuorum

/**
  * Created by shutty on 11/16/15.
  */
object Server {
  def main(args: Array[String]): Unit = {
    val dsys = new RaftQuorum()
    dsys.start
  }
}
