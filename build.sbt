name := "distdb"
version := "1.0"
scalaVersion := "2.11.8"
mainClass in assembly := Some("ru.jpoint.distdb.Server")
scalacOptions += "-Xexperimental"

resolvers += "kender" at "http://dl.bintray.com/kender/maven"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.4",
//  "com.typesafe.akka" %% "akka-slf4j" % "2.4.4",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.4.0"
)

assemblyMergeStrategy in assembly <<= (assemblyMergeStrategy in assembly) {(old) => {
  case m if m.toLowerCase.endsWith("io.netty.versions.properties") => MergeStrategy.first
  case other => old.apply(other)
}}