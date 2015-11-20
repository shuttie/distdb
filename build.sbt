name := "distkv"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.0"

libraryDependencies += "com.typesafe.akka" %% "akka-cluster" % "2.4.0"

libraryDependencies += "com.typesafe.akka" %% "akka-cluster-tools" % "2.4.0"

libraryDependencies += "com.typesafe.akka" %% "akka-http-experimental" % "1.0"

libraryDependencies += "com.typesafe" % "config" % "1.3.0"

libraryDependencies += "org.mapdb" % "mapdb" % "1.0.8"