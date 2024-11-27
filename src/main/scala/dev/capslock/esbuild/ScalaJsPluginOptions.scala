package dev.capslock.esbuild

import scala.scalajs.js

trait ScalaJsPluginOptions extends js.Object {
  val scalaVersion: String
  val scalaProjectName: String
  val scalaTargetFileExtension: String
}
