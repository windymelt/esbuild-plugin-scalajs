package dev.capslock.esbuild

import scalajs.js
import scalajs.js.annotation._

trait EsbuildPlugin extends js.Object {
  val name: String
  def setup(build: Build): Unit
}

trait Build extends js.Object {
  def onResolve(
      props: OnResolveProps,
      callback: js.Function1[OnResolveArgs, js.Promise[OnResolveResult]],
  ): Unit
  def onLoad(
      props: OnLoadProps,
      callback: js.Function1[OnLoadArgs, js.Promise[OnLoadResult]],
  ): Unit
}

trait OnResolveProps extends js.Object {
  val filter: js.RegExp
}

trait OnLoadProps extends js.Object {
  val filter: js.RegExp
  val namespace: String
}

trait OnResolveArgs extends js.Object {
  val path: String
  val resolveDir: String
}

trait OnLoadArgs extends js.Object {
  // not used now
}

trait OnResolveResult extends js.Object {
  val path: String
}

// not used
trait OnLoadResult extends js.Object {
  val path: String
  val namespace: String
}
