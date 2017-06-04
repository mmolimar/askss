
lazy val root = Project(id = "askss", base = file("."))
  .aggregate(common, events2kafka, streaming)
  .settings(Settings.moduleSettings: _*)

lazy val common = Project(id = "common", base = file("common"))
  .settings(Settings.moduleSettings: _*)
  .settings(libraryDependencies ++= Settings.commonDeps: _*)

lazy val events2kafka = Project(id = "events2kafka", base = file("events2kafka"))
  .settings(Settings.moduleSettings: _*)
  .settings(libraryDependencies ++= Settings.eventsDeps: _*)
  .dependsOn(common)

lazy val streaming = Project(id = "streaming", base = file("streaming"))
  .settings(Settings.moduleSettings: _*)
  //.settings (libraryDependencies ++= Settings.eventsDeps: _*)
  .settings(libraryDependencies ++= Settings.sparkDeps: _*)
  .settings(SparkSubmit.settings: _*)
  .dependsOn(common)