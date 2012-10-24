name := "frege-db"

organization := "com.earldouglas"

version := "0.1-SNAPSHOT"

scalaVersion := "2.9.1"

EclipseKeys.withSource := true

seq(webSettings :_*)

libraryDependencies ++= Seq(
    "org.hsqldb" % "hsqldb" % "2.2.8"
  , "org.mortbay.jetty" % "jetty" % "6.1.22" % "container,test"
)

port in container.Configuration := 9999

