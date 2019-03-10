lazy val root = (project in file("."))
  .settings(
    organization := "io.onema",

    name := "overwatch-reporter",

    version := "0.2.0",

    scalaVersion := "2.12.8",

    libraryDependencies ++= {
      val awsSdkVersion = "1.11.515"
      Seq(
        // Serverless Base!
        "io.onema"                  % "userverless-core_2.12"      % "0.4.0",
        "com.amazonaws"             % "aws-java-sdk-sns"           % awsSdkVersion,
        "com.amazonaws"             % "aws-java-sdk-cloudwatch"    % awsSdkVersion,

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

