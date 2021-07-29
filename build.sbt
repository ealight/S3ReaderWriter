name := "AWSs3Test"

version := "0.1"

scalaVersion := "2.13.6"

val AkkaVersion = "2.6.14"
val AkkaHttpVersion = "10.1.11"

libraryDependencies ++= Seq(
  "com.lightbend.akka" %% "akka-stream-alpakka-s3" % "3.0.2",
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-xml" % AkkaHttpVersion,
  "ch.qos.logback" % "logback-classic" % "1.1.3"
)
