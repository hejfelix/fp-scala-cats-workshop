val V = new {
  val tapir = "0.4"
  val http4s = "0.20.0-M6"
}

val commonSettings = Seq(
  organization := "com.lambdaminute",
  version := "0.1",
  scalaVersion := "2.12.8"
)

lazy val randomService = project
  .in(file("services/random"))
  .settings(
    commonSettings,
    libraryDependencies ++=
      Seq(
        "com.softwaremill.tapir" %% "tapir-core" % V.tapir,
        "com.softwaremill.tapir" %% "tapir-http4s-server" % V.tapir,
        "com.softwaremill.tapir" %% "tapir-json-circe" % V.tapir,
        "org.http4s" %% "http4s-dsl" % V.http4s,
        "org.http4s" %% "http4s-server" % V.http4s,
        "ch.qos.logback" % "logback-classic" % "1.3.0-alpha4",
        "org.webjars" % "swagger-ui" % "3.20.9",
        "com.softwaremill.tapir" %% "tapir-openapi-docs" % V.tapir,
        "com.softwaremill.tapir" %% "tapir-openapi-circe-yaml" % V.tapir,
        "org.scalatest" %% "scalatest" % "3.0.5" % Test
      ),
  )
  .dependsOn(randomApi)

lazy val randomApi = project
  .in(file("services/randomApi"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "com.softwaremill.tapir" %% "tapir-core" % V.tapir,
      "com.softwaremill.sttp" %% "async-http-client-backend-cats" % "1.5.11",
      "com.softwaremill.tapir" %% "tapir-sttp-client" % V.tapir,
    )
  )

lazy val exercise1 = project
  .in(file("exercises/exercise1"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp" %% "async-http-client-backend-cats" % "1.5.11",
    )
  )
  .dependsOn(randomApi, randomService)
