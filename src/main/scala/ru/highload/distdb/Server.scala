package ru.highload.distdb

import ru.highload.distdb.ms.{MasterSlave, MasterSlaveSync}

/**
  * Created by shutty on 11/16/15.
  */
object Server {
  def main(args: Array[String]): Unit = {
    val dsys = new Quorum()
    dsys.start
  }
}
