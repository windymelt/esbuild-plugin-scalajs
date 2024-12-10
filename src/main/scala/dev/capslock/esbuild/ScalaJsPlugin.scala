package dev.capslock.esbuild

import scalajs.js
import js.annotation._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
// Future to Promise
import js.JSConverters._
// (implicitly) Promise to Future
import scala.scalajs.js.Thenable.Implicits._

@JSGlobal
@js.native
object process extends js.Object {
  @js.native
  object env extends js.Object {
    val NODE_ENV: js.UndefOr[String] = js.native
  }
}

@JSExportTopLevel("scalaJsPlugin", "esbuildScalaJsPlugin")
def scalaJsPlugin(options: ScalaJsPluginOptions): EsbuildPlugin = {
  scribe.info("scalaJsPlugin is starting up...")
  opts = options
  return ScalaJsPlugin
}

@JSExportTopLevel("scalaJsPluginOpts", "esbuildScalaJsPlugin")
var opts: ScalaJsPluginOptions = new {
  var scalaVersion: String             = ""
  var scalaProjectName: String         = ""
  var scalaTargetFileExtension: String = ""
}

object ScalaJsPlugin extends EsbuildPlugin {
  val name = "scala-js"

  def setup(build: Build): Unit = {
    val isProd                           = process.env.NODE_ENV.map(_ == "production").getOrElse(false)
    val scalaProjectName                 = opts.scalaProjectName
    val scalaTargetDirSuffix             = if isProd then "-opt" else "-fastopt"
    var scalaTargetFileExtension: String = opts.scalaTargetFileExtension
    // if and only if cache miss is detected first, run sbtn
    build.onResolve(
      new OnResolveProps {
        val filter = js.RegExp("""^scala:.+""")
      },
      (args) => {
        onResolve(args, isProd, scalaProjectName, scalaTargetDirSuffix, scalaTargetFileExtension).toJSPromise
      },
    )
  }

  inline def runBuild(isProd: Boolean): Future[Unit] = {
    // assuming sbtn
    val subcommand = if isProd then "fullLinkJS" else "fastLinkJS"
    scribe.info("running sbtn")
    runCommand("sbtn", Seq(subcommand))
  }

  inline def onResolve(
      args: OnResolveArgs,
      isProd: Boolean,
      scalaProjectName: String,
      scalaTargetDirSuffix: String,
      scalaTargetFileExtension: String,
  ): Future[OnResolveResult] = {
    val importModuleName = """^scala:""".r.replaceFirstIn(args.path, "")
    val sv               = opts.scalaVersion
    val scalaTargetDir =
      s"target/scala-$sv/$scalaProjectName$scalaTargetDirSuffix/"
    val targetPath =
      s"$scalaTargetDir$importModuleName.$scalaTargetFileExtension"
    val absoluteTargerPath = s"${args.resolveDir}/$targetPath"

    // TODO: if and only if first time, run sbtn
    runBuild(isProd).map { _ =>
      // XXX: assuming not failed
      new OnResolveResult {
        val path = absoluteTargerPath
      }
    }
  }
}
