import org.scalajs.linker.interface.OutputPatterns

val scala3Version = "3.6.2"

lazy val root = project
  .in(file("."))
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(BuildInfoPlugin)
  .settings(
    name         := "esbuild-scalajs",
    version      := "0.0.5",
    scalaVersion := scala3Version,
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.ESModule).withOutputPatterns(OutputPatterns.fromJSFile("%s.mjs")) },
    libraryDependencies += "com.outr"      %%% "scribe" % "3.15.2",
    libraryDependencies += "org.scalameta" %%% "munit"  % "1.0.0" % Test,

    buildInfoKeys    := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "dev.capslock.esbuild",
  )
