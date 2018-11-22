lazy val root = (project in file("."))
  .settings(
    organization := "io.onema",

    name := "userverless-event-reporter",

    version := "0.1.0",

    scalaVersion := "2.12.7",

    libraryDependencies ++= {
      val awsSdkVersion = "1.11.451"
      Seq(
        // Serverless Base!
        "io.onema"                  % "userverless-core_2.12"      % "0.1.0",
        "com.amazonaws"             % "aws-java-sdk-sns"           % awsSdkVersion,

        // Testing
        "org.scalatest"             %% "scalatest"                          % "3.0.4"   % Test,
        "org.scalamock"             % "scalamock-scalatest-support_2.12"    % "3.6.0"   % Test
      )
    }
  )
//  .dependsOn(uServerless)

//lazy val uServerless = (project in file("../"))

// Assembly
assemblyJarName in assembly := "app.jar"

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-Xfatal-warnings")

