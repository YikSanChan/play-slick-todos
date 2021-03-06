name := """play-slick-todos"""

version := "2.6.x"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.6"

libraryDependencies += guice
libraryDependencies += specs2 % Test
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.5"
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "3.0.1",
  "com.typesafe.play" %% "play-slick-evolutions" % "3.0.1"
)
libraryDependencies += "com.github.tminglei" %% "slick-pg" % "0.16.3"
