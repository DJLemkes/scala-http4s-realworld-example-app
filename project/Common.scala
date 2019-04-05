import java.io.Closeable
import scala.io.Source

import sbt.Keys._
import sbt._

object Common {

  // Dependency versions
  private val circeVersion      = "0.11.1"
  private val http4sVersion     = "0.20.0-M6"
  private val logbackVersion    = "1.2.3"
  private val pureConfigVersion = "0.10.2"
  private val specs2Version     = "4.5.1"

  // Compiler plugin dependency versions
  private val kindProjectorVersion    = "0.9.8"
  private val betterMonadicForVersion = "0.2.4"

  val settings = Seq(
    organization := "com.hhandoko",
    name := "realworld",
    version := using(Source.fromFile("VERSION.txt")) { file => file.mkString },
    scalaVersion := "2.12.8",
    libraryDependencies ++= Seq(
      "io.circe"              %% "circe-generic"       % circeVersion,
      "ch.qos.logback"        %  "logback-classic"     % logbackVersion,
      "com.github.pureconfig" %% "pureconfig"          % pureConfigVersion,
      "org.http4s"            %% "http4s-blaze-client" % http4sVersion,
      "org.http4s"            %% "http4s-blaze-server" % http4sVersion,
      "org.http4s"            %% "http4s-circe"        % http4sVersion,
      "org.http4s"            %% "http4s-dsl"          % http4sVersion,
      "org.specs2"            %% "specs2-core"         % specs2Version % Test
    ),

    // Add syntax for type lambdas
    // See: https://github.com/non/kind-projector
    addCompilerPlugin("org.spire-math" %% "kind-projector" % kindProjectorVersion),

    // Desugaring scala `for` without implicit `withFilter`s
    // See: https://github.com/oleg-py/better-monadic-for
    addCompilerPlugin("com.olegpy"     %% "better-monadic-for" % betterMonadicForVersion)
  )

  private[this] def using[T <: Closeable, U](res: T)(fn: T => U): U =
    try { fn(res) } finally { res.close() }

}