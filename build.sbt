name := "SportEventsAnalyzer"

version := "1.0"

scalaVersion := "2.12.4"

scalacOptions ++= Seq("-feature")

libraryDependencies += "com.typesafe" % "config" % "1.3.2"
libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "2.2.1"

/*Akka*/
libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.0.11"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.8"
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.8"

/*Logging*/
libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.25"

/*HTML Parser*/
libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "2.0.0"

/* Test Dependency */
libraryDependencies += "org.scalatest" % "scalatest_2.12" % "3.0.4" % "test"
libraryDependencies += "org.scalamock" %% "scalamock" % "4.0.0" % Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % Test

/* NScala-time */
libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "2.18.0"