name := "distkv"
version := "1.0"
scalaVersion := "2.11.7"
mainClass in assembly := Some("ru.jpoint.distkv.Application")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.2-RC3",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.2-RC3",
  "com.github.scopt" %% "scopt" % "3.4.0",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "ch.qos.logback" % "logback-classic" % "1.1.3"
)