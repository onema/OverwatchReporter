
lazy val scala213 = "2.13.1"
lazy val scala212 = "2.12.10"
lazy val supportedScalaVersions = List(scala213, scala212)

ThisBuild / organization := "io.onema"
ThisBuild / version      := "0.3.0"
ThisBuild / scalaVersion := scala213
ThisBuild / parallelExecution in Test := false

lazy val overwatchReporter = (project in file("."))
  .settings(skip in publish := true)
    .aggregate(overwatchReorterErrors, overwatchReorterMetrics, overwatchReorterNotifications)
publishArtifact in overwatchReporter := false

val awsSdkVersion = "1.11.792"

lazy val overwatchReorterMetrics = (project in file("overwatch-reporter-metrics"))
  .settings(
    name := "overwatch-reporer-metrics",
    commonPublishSettings,
    crossScalaVersions := supportedScalaVersions,
    libraryDependencies ++= {
      Seq(
        // Serverless Base!
        "io.onema"                  %% "userverless-core"          % "0.4.1",
        "com.amazonaws"             % "aws-java-sdk-sns"           % awsSdkVersion,
        "com.amazonaws"             % "aws-java-sdk-cloudwatch"    % awsSdkVersion,

        // Testing
        "org.scalatest"             %% "scalatest"                 % "3.1.2"   % Test,
        "org.scalamock"             %% "scalamock"                 % "4.4.0"   % Test
      )
    }
  )

lazy val overwatchReorterErrors = (project in file("overwatch-reporter-errors"))
  .settings(
    name := "overwatch-reporter-errors",
    commonPublishSettings,
    crossScalaVersions := supportedScalaVersions,
    libraryDependencies ++= {
      Seq(
        // Serverless Base!
        "io.onema"                  %% "userverless-core"          % "0.4.1",
        "com.amazonaws"             % "aws-java-sdk-sns"           % awsSdkVersion,

        // Testing
        "org.scalatest"             %% "scalatest"                 % "3.1.2"   % Test,
        "org.scalamock"             %% "scalamock"                 % "4.4.0"   % Test
      )
    }
  )

lazy val overwatchReorterNotifications = (project in file("overwatch-reporter-notifications"))
  .settings(
    name := "overwatch-reporter-notifications",
    commonPublishSettings,
    crossScalaVersions := supportedScalaVersions,
    libraryDependencies ++= {
      Seq(
        // Serverless Base!
        "io.onema"                  %% "userverless-core"          % "0.4.1",
        "com.amazonaws"             % "aws-java-sdk-sns"           % awsSdkVersion,

        // Testing
        "org.scalatest"             %% "scalatest"                 % "3.1.2"   % Test,
        "org.scalamock"             %% "scalamock"                 % "4.4.0"   % Test
      )
    }
  )

// Maven Central Repo boilerplate configuration
lazy val commonPublishSettings = Seq(
  //  publishTo := Some("Onema Snapshots" at "s3://s3-us-east-1.amazonaws.com/ones-deployment-bucket/snapshots"),
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  },
  publishMavenStyle := true,
  pomIncludeRepository := { _ => false },
  licenses := Seq("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0")),
  homepage := Some(url("https://github.com/onema/OverwatchReporter")),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/onema/OverwatchReporter"),
      "scm:git@github.com:onema/OverwatchReporter.git"
    )
  ),
  developers := List(
    Developer(
      id    = "onema",
      name  = "Juan Manuel Torres",
      email = "software@onema.io",
      url   = url("https://github.com/onema/")
    )
  ),
  publishArtifact in Test := false
)

