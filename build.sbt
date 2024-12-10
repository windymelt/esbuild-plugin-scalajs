val scala3Version = "3.6.2"

lazy val root = project
  .in(file("."))
  .enablePlugins(ScalaJSPlugin)
  //.enablePlugins(NpmPackagePlugin)
    .enablePlugins(BuildInfoPlugin)
.settings(
    name         := "esbuild-plugin-scalajs",
    version      := "0.0.1",
    scalaVersion := scala3Version,
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.ESModule) },
    libraryDependencies += "com.outr"      %%% "scribe" % "3.15.2",
    libraryDependencies += "org.scalameta" %%% "munit"  % "1.0.0" % Test,

    buildInfoKeys    := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "dev.capslock.esbuild",

    // npmPackageName        := "esbuild-plugin-scalajs",
    // npmPackageDescription := "esbuild plugin for Scala.js",
    // npmPackageStage       := FastOptStage,
    // npmPackageRepository  := Some("https://github.com/windymelt/esbuild-plugin-scalajs"),
    // npmPackageAuthor      := "windymelt",
    // npmPackageLicense     := Some("BSD-2-Clause"),
    // npmPackageKeywords    := Seq("scala", "esbuild"),
    // npmPackageType := "module"
  )
