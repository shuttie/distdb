name := "distdb"
version := "1.0"
scalaVersion := "2.11.7"
mainClass in assembly := Some("ru.jpoint.distdb.Server")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.2",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.2",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "io.atomix.copycat" % "copycat-server" % "1.0.0-rc2",
  "io.atomix.copycat" % "copycat-client" % "1.0.0-rc2",
  "io.atomix.copycat" % "copycat-protocol" % "1.0.0-rc2"
)