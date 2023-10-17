import sbt.Def

import scala.{ Console => csl }

val developer = Developer(
  "mvillafuertem",
  "Miguel Villafuerte",
  "mvillafuertem@email.com",
  url("https://github.com/mvillafuertem")
)

Global / onLoad := {
  val GREEN = csl.GREEN
  val RESET = csl.RESET
  println(s"""$GREEN
             |$GREEN         █████╗  ██╗    ██╗ ███████╗
             |$GREEN        ██╔══██╗ ██║    ██║ ██╔════╝
             |$GREEN        ███████║ ██║ █╗ ██║ ███████╗
             |$GREEN        ██╔══██║ ██║███╗██║ ╚════██║
             |$GREEN        ██║  ██║ ╚███╔███╔╝ ███████║
             |$GREEN        ╚═╝  ╚═╝  ╚══╝╚══╝  ╚══════╝
             |$RESET        v.${version.value}
             |$RESET        https://github.com/mvillafuertem/aws
             |""".stripMargin)
  (Global / onLoad).value
}

val scala213 = "2.13.10"

lazy val aws = (project in file("."))
  .aggregate(cdktf)
  .settings(
    scalaVersion := scala213,
    welcomeMessage
  )

lazy val cdktf = (project in file("modules/cdktf"))
  .settings(scalaVersion := scala213)
  .settings(watchTriggers += baseDirectory.value.toGlob / "*.scala")

lazy val iam = (project in file("modules/iam"))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)
  .settings(scalaVersion := scala213)
  .settings(libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % IntegrationTest)
  .settings(libraryDependencies += "com.dimafeng" %% "testcontainers-scala-core" % "0.40.12" % IntegrationTest)
  .dependsOn(cdktf)

def welcomeMessage: Def.Setting[String] = onLoadMessage := {
  def header(text: String): String                = s"${csl.BOLD}${csl.MAGENTA}$text${csl.RESET}"
  def cmd(text: String, description: String = "") = f"${csl.GREEN}> ${csl.CYAN}$text%10s $description${csl.RESET}"
  // def subItem(text: String): String = s"  ${Console.YELLOW}> ${Console.CYAN}$text${Console.RESET}"

  s"""|${header("sbt")}:
      |${cmd("build", "- Prepares sources, compiles and runs tests")}
      |${cmd("prepare", "- Prepares sources by applying both scalafix and scalafmt")}
      |${cmd("fmt", "- Formats source files using scalafmt")}
      |${cmd("cdktf/run", "- Create cdk.tf.json files")}
      |
      |${header("yarn")}:
      |${cmd("--cwd modules/cdktf/ install")}
      |${cmd("--cwd modules/cdktf/ get")}
      """.stripMargin
}
