val scala3Version = "3.5.2"

lazy val root = project
  .in(file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name         := "esbuild Plugin for Scala.js",
    version      := "0.0.1-SNAPSHOT",
    scalaVersion := scala3Version,
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.ESModule) },
    libraryDependencies += "org.scalameta" %%% "munit" % "1.0.0" % Test,
  )
