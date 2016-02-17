name := "distdb"
version := "1.0"
scalaVersion := "2.11.7"
mainClass in assembly := Some("ru.jpoint.distdb.App")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.2-RC3",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.2-RC3",
  "ch.qos.logback" % "logback-classic" % "1.1.3"
)