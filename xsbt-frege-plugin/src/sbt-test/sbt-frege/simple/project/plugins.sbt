addSbtPlugin("com.earldouglas" % "sbt-frege" % "0.1-SNAPSHOT")

val fregeV = "3.21.297"
val fregeHash = "g6b54457"

libraryDependencies += "frege" % "frege" % fregeV from s"https://github.com/Frege/frege/releases/download/${fregeV}/frege${fregeV}-${fregeHash}.jar"
