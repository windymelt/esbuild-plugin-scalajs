package dev.capslock.esbuild

import scala.scalajs.js

trait ScalaJsPluginOptions extends js.Object {
  var scalaVersion: String
  var scalaProjectName: String
  var scalaTargetFileExtension: String
}
