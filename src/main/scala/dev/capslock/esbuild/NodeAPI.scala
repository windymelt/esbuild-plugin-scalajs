package dev.capslock.esbuild

import scalajs.js
import js.annotation._
import scala.concurrent.Promise
import js.JSConverters._
import scala.concurrent.Future

def runCommand(command: String, args: Seq[String]): Future[Unit] = {
  val p  = Promise[Unit]()
  val cp = spawn(command, args.toJSArray, new SpawnOptions { val stdio = "inherit" })
  cp.on("exit", _ => p.success(())) // TODO: read exit code
  cp.on("error", _ => p.failure(new Exception("subprocess reported error")))

  p.future
}

@js.native
@JSImport("node:child_process", "spawn")
private def spawn(command: String, args: js.Array[String], options: SpawnOptions): ChildProcess = js.native

trait SpawnOptions extends js.Object {
  val stdio: String
}

@js.native
trait ChildProcess extends js.Object {
  def on(ev: String, callback: js.Function1[String, Unit]): Unit = js.native
}
