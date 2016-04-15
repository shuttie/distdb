name := "distdb"
version := "1.0"
scalaVersion := "2.11.8"
mainClass in assembly := Some("ru.jpoint.distdb.Server")
scalacOptions += "-Xexperimental"

resolvers += "kender" at "http://dl.bintray.com/kender/maven"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.4",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.4",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "io.atomix.copycat" % "copycat-server" % "1.0.0-rc7",
  "io.atomix.copycat" % "copycat-client" % "1.0.0-rc7",
  "io.atomix.catalyst" % "catalyst-netty" % "1.0.7",
  "me.enkode" %% "java8-converters" % "1.1.0",
  "org.scala-lang.modules" % "scala-java8-compat_2.11" % "0.8.0-RC1"
)

assemblyMergeStrategy in assembly <<= (assemblyMergeStrategy in assembly) {(old) => {
  case m if m.toLowerCase.endsWith("io.netty.versions.properties") => MergeStrategy.first
  case other => old.apply(other)
}}