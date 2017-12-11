name := "SportEventsAnalyzer"

version := "1.0"

scalaVersion := "2.12.4"

scalacOptions ++= Seq("-feature")

libraryDependencies += "com.typesafe" % "config" % "1.3.2"
libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "2.1.0"

/* Test Dependency */
libraryDependencies += "org.scalatest" % "scalatest_2.12" % "3.0.4" % "test"
