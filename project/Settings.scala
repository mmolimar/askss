import sbt.Keys._

object Settings extends Dependencies {

  val moduleSettings = Seq(
    organization := "com.github.mmolimar",
    version := "1.0-SNAPSHOT",

    scalaVersion := scalaVersionUsed,

    scalacOptions ++= Seq(
      "-target:jvm-1.8",
      "-encoding", "UTF-8",
      "-unchecked",
      "-deprecation",
      "-feature",
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-language:postfixOps",
      "-Xlint"
    ),

    javacOptions ++= Seq(
      "-source", "1.8",
      "-target", "1.8",
      "Xlint"
    ),

    resolvers ++= commonResolvers,

    libraryDependencies ++= commonDeps,
    libraryDependencies ++= testDeps map (_ % "test"),

    ivyScala := ivyScala.value.map {_.copy(overrideScalaVersion = true)},

    fork in run := true
  )
}