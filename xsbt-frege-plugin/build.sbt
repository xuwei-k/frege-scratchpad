sbtPlugin := true

version := "0.1-SNAPSHOT"

name := "sbt-frege"

organization := "com.earldouglas"

ScriptedPlugin.scriptedSettings

ScriptedPlugin.scriptedBufferLog := false

val fregeV = "3.21.297"
val fregeHash = "g6b54457"

libraryDependencies += "frege" % "frege" % fregeV % "provided" from s"https://github.com/Frege/frege/releases/download/${fregeV}/frege${fregeV}-${fregeHash}.jar"
