package ru.jpoint.distkv

import scopt.OptionParser

/**
  * Created by shutty on 2/16/16.
  */
case class Config(isMaster:Boolean = true, hostname:String = "localhost", slaves:Seq[String] = Nil)

object Config extends Logging {
  def parse(args:Array[String]) = {
    val parser = new OptionParser[Config]("distkv") {
      head("distkv", "1.0")
      help("help")
      opt[String]('h', "hostname")
        .valueName("<host>")
        .action { (value, conf) => conf.copy(hostname = value) }
        .text("local node hostname")

      opt[Unit]('m', "master")
        .action { (_, conf) => conf.copy(isMaster = true) }
        .text("is master")

      opt[Seq[String]]('s',"slaves")
        .valueName("<host1>,<host2>")
        .action { (values, conf) => conf.copy(slaves = values) }
        .text("slave hosts")
    }
    parser.parse(args, Config()) match {
      case Some(config) => {
        log.info(s"Master: ${config.isMaster}, Hostname: ${config.hostname}, Slaves: ${config.slaves}")
        config
      }
      case None => {
        log.error("Cannot parse arguments")
        throw new IllegalArgumentException("time to die")
      }
    }
  }
}
