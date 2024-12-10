![](./logo.png)

## Thin esbuild plugin for Scala.js ![NPM Version](https://img.shields.io/npm/v/esbuild-scalajs)

This plugin provides esbuild plugin that enables build, import, bundle Scala.js code in your project.

### TODO

- self bootstrap
- configurable target root
- scala steward

### Usage

#### Prerequisites

- Scala.js project built in `sbt`
- `sbtn` (you can install it via Coursier)
- Place `target` directory for sbt aside `package.json` like below:

```
.
├── build.sbt
├── esbuild.mjs
├── package.json
├── pnpm-lock.yaml
├── project
├── src
│   └── main
│       └── scala
└── target
```

#### Installation

At first, you should install this plugin from npm:

```sh
% npm i esbuild-scalajs
```
#### Configure

You will need a config object to address Scala artifact:

```js
import * as esbuild from 'esbuild'
import { scalaJsPlugin } from 'esbuild-scalajs'

const opts = {
  "scalaVersion": "3.5.2",
  "scalaProjectName": "esbuild-exercise",
  "scalaTargetFileExtension": "js",
}

await esbuild.build({
  entryPoints: ['main.js'],
  bundle: true,
  platform: 'node',
  outfile: 'dist.cjs',
  minify: true,
  plugins: [scalaJsPlugin(opts)],
})
```

#### Importing Scala.js module

You can import Scala.js module by prefixing `scala:` with module name.

```js
import { fib } from 'scala:scalamain'
```

Corresponding Scala source code:

```scala
package scalamain

import scala.scalajs.js
import scala.scalajs.js.annotation._

object Main {
  @JSExportTopLevel("fib", moduleID = "scalamain")
  def fib(n: Int): Int = {
    if (n <= 1) n
    else fib(n - 1) + fib(n - 2)
  }
}
```

#### Mapping

This plugin simply searches Scala.js artifact with this interpolation:

`s"target/scala-$scalaVersion/$scalaProjectName$scalaTargetDirSuffix/$importModuleName.$scalaTargetFileExtension"`

- `$scalaTargetDirSuffis` corresponds to...
  - `-opt` if `NODE_ENV=production`.
  - `-fastopt` otherwise.
- `$importModuleName` corresponds to `import { foobar } from 'scala:${HERE!!}'`