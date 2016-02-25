package ru.jpoint.distdb

/**
  * Created by shutty on 11/16/15.
  */
object App {

  def main(args: Array[String]): Unit = {
    val dsys = new MasterSlaveAsync()
    dsys.start
  }
}
