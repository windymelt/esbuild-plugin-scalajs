val scala3Version = "3.6.2"

lazy val root = project
  .in(file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name         := "esbuild-plugin-scalajs",
    version      := "0.0.1",
    scalaVersion := scala3Version,
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.ESModule) },
    libraryDependencies += "com.outr"      %%% "scribe" % "3.15.2",
    libraryDependencies += "org.scalameta" %%% "munit"  % "1.0.0" % Test,
  )
  .enablePlugins(BuildInfoPlugin)
  .settings(
    buildInfoKeys    := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "dev.capslock.esbuild",
  )
  .enablePlugins(NpmPackagePlugin)
  .settings(
    npmPackageName        := "esbuild-plugin-scalajs",
    npmPackageDescription := "esbuild plugin for Scala.js",
    npmPackageStage       := FullOptStage,
    npmPackageRepository  := Some("https://github.com/windymelt/esbuild-plugin-scalajs"),
    npmPackageAuthor      := "windymelt",
    npmPackageLicense     := Some("BSD-2-Clause"),
    npmPackageKeywords    := Seq("scala", "esbuild"),
  )
