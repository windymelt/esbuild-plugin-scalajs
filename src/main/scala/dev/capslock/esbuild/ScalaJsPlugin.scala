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
    val NODE_ENV: String = js.native
  }
}

@JSExportTopLevel("scalaJsPlugin", "esbuildScalaJsPlugin")
def scalaJsPluginOptions(opts: ScalaJsPluginOptions): EsbuildPlugin = {
  val impl = ScalaJsPluginImpl(opts)
  new {
    val name                      = "scala-js"
    def setup(build: Build): Unit = impl.setup(build)
  }
}

class ScalaJsPluginImpl(opts: ScalaJsPluginOptions) {
  val name                     = "scala-js"
  val isProd                   = process.env.NODE_ENV == "production"
  val scalaVersion             = opts.scalaVersion
  val scalaProjectName         = opts.scalaProjectName
  val scalaTargetDirSuffix     = if isProd then "-opt" else "-fastopt"
  val scalaTargetFileExtension = opts.scalaTargetFileExtension

  def setup(build: Build): Unit = {

    // if and only if cache miss is detected first, run sbtn
    build.onResolve(
      new OnResolveProps {
        val filter = js.RegExp("""^scala:.+""")
      },
      (args) => onResolve(args).toJSPromise,
    )
  }

  def runBuild(): Future[Unit] = {
    // assuming sbtn
    val subcommand = if isProd then "fullLinkJS" else "fastLinkJS"
    runCommand("sbtn", Seq(subcommand))
  }

  def onResolve(args: OnResolveArgs): Future[OnResolveResult] = {
    val importModuleName = """^scala:""".r.replaceFirstIn(args.path, "")
    val scalaTargetDir =
      s"target/scala-$scalaVersion/$scalaProjectName$scalaTargetDirSuffix/"
    val targetPath =
      s"$scalaTargetDir$importModuleName.$scalaTargetFileExtension"
    val absoluteTargerPath = s"${args.resolveDir}/$targetPath"

    // TODO: if and only if first time, run sbtn
    runBuild().map { _ =>
      // XXX: assuming not failed
      new OnResolveResult {
        val path = absoluteTargerPath
      }
    }
  }
}
