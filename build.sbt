enablePlugins(JavaAppPackaging)

name := "akka-http-test"
organization := "com.sap"
version := "1.0"
scalaVersion := "2.11.7"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {

  val akkaVersion = "2.4.2"
  val akkaStreamVersion = "2.0.3"
  val scalaTestVersion = "2.2.6"

  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream-experimental" % akkaStreamVersion,

    "com.typesafe.akka" %% "akka-http-core-experimental" % akkaStreamVersion,
    "com.typesafe.akka" %% "akka-http-experimental" % akkaStreamVersion,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaStreamVersion,
    "com.typesafe.akka" %% "akka-http-testkit-experimental" % akkaStreamVersion,

    "org.mongodb" % "mongodb-driver-reactivestreams" % "1.2.0",

    "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
    "org.scalamock" %% "scalamock-scalatest-support" % "3.2.2" % "test",
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
  )
}

Revolver.settings
