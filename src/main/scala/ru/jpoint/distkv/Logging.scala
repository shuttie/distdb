package ru.jpoint.distkv

import org.slf4j.LoggerFactory

/**
  * Created by shutty on 2/16/16.
  */
trait Logging {
  lazy val log = LoggerFactory.getLogger(getClass)
}
