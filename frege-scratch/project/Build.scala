import sbt._
import Keys._
import com.github.siasia.WebPlugin.webSettings

trait FregeCompiler {
  def apply(sources: Seq[File], classpath: Seq[File], outputDirectory: File, options: Seq[String])(implicit log: Logger): Unit
}
object FregeCompiler {

  def construct(f: (Seq[String], Logger) => Int, cp: ClasspathOptions, scalaInstance: ScalaInstance): FregeCompiler =
    new FregeCompiler {
      def apply(sources: Seq[File], classpath: Seq[File], outputDirectory: File, options: Seq[String])(implicit log: Logger) {
        val cp = "project/lib/fregec.jar:" + classpath.mkString(String.valueOf(java.io.File.pathSeparatorChar))
        println(cp)
        (file(".") / "target" / "scala-2.9.1" / "classes").mkdirs()
        val fregeArgs = Seq(
            "-fp", cp
          , "-d", "target/frege/classes"
        ) ++ sources.map(_.getPath)

        frege.compiler.Main.main(fregeArgs.toArray)
      }
    }
}

object FregeScratch extends Build {

  def fregec(classpath: Seq[sbt.Attributed[java.io.File]]) = {
    // TODO incremental compile -- don't recompile stuff unless needed
    // TODO mixed-mode compiling Java/Frege(/Scala?)
    val cp = "project/lib/fregec.jar:" + classpath.map(_.data).mkString(String.valueOf(java.io.File.pathSeparatorChar))
    println(cp)
    (file(".") / "target" / "scala-2.9.1" / "classes").mkdirs()
    val fregeSrcs = ((file(".") / "src" / "main" / "frege") ** "*.fr").getPaths
    val fregeArgs = Seq(
        "-fp", cp
      , "-d", "target/scala-2.9.1/classes"
    ) ++ fregeSrcs

    frege.compiler.Main.main(fregeArgs.toArray)
  }

  lazy val root =
    Project(
      id = "frege-scratch",
      base = file("."),
      settings = Project.defaultSettings ++ Seq(
          organization := "com.earldouglas"
        , unmanagedClasspath in Compile <+= (baseDirectory) map { bd => Attributed.blank(bd / "target/scala-2.9.1/classes") }
        , javacOptions += "-Xlint:unchecked"
        , libraryDependencies += "junit" % "junit" % "4.8.2" % "test"
        , libraryDependencies += "com.novocode" % "junit-interface" % "0.8" % "test"
        , libraryDependencies += "org.mortbay.jetty" % "jetty" % "6.1.22" % "compile,container"
          // TODO add a new task for compiling Frege code
        , (compile in Compile) <<= (compile in Compile, managedClasspath in Compile) map {
            (x: sbt.inc.Analysis, managedClasspath) =>
              fregec(managedClasspath)
              x
          }
      ) ++ seq(webSettings :_*)
    )
}
