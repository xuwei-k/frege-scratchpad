name := "frege-web"

organization := "com.earldouglas"

version := "0.1-SNAPSHOT"

scalaVersion := "2.9.2"

EclipseKeys.withSource := true

seq(webSettings :_*)

libraryDependencies ++= Seq(
    "org.mortbay.jetty" % "jetty" % "6.1.22" % "container,test"
  , "javax.servlet" % "servlet-api" % "2.5" % "provided,test"
)
