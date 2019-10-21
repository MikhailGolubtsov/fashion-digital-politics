name := "fashion-digital-task"
version := "0.1"
scalaVersion := "2.12.8"

mainClass := Some("de.fashiondigital.politics.Main")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http"   % "10.1.10",
  "com.typesafe.akka" %% "akka-stream" % "2.5.23",
  "io.circe" %% "circe-core" % "0.12.1",
  "io.circe" %% "circe-generic" % "0.12.1",
  "de.heikoseeberger" %% "akka-http-circe" % "1.29.1"
)
