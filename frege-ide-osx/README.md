# Installing the Frege IDE on OSX

*2 October 2012*

The focus of this article is to address a bug which breaks the [standard installation instructions](http://code.google.com/p/frege/wiki/HowToEclipseFregIDE) on OSX.

## Java

[Download](http://www.oracle.com/technetwork/java/javase/downloads/index.html) and install the Java 7 JDK

From Spotlight, run *Java Preferences* and drag *Java SE 7* to the top of the list.

## Eclipse

[Download](http://www.eclipse.org/indigo/) and extract your favorite flavor of Eclipse Indigo (3.7)

Fix [this bug](http://java.net/jira/browse/MACOSX_PORT-432) by replacing the *Eclipse.app/Contents/MacOS/eclipse* executable with the following script:

```sh
#!/bin/sh
#
# This file replaces Eclipse.app/Contents/MacOS/eclipse
#
# Fixes bug http://java.net/jira/browse/MACOSX_PORT-432
#
# Adapted from http://code.google.com/p/openjdk-osx-build/wiki/EclipseWithOpenJDK
#

cd `dirname $0`

export JAVA_HOME=`/usr/libexec/java_home`
LAUNCHER_JAR=../../../plugins/org.eclipse.equinox.launcher_1.2.0.v20110502.jar

java \
-showversion \
-XX:MaxPermSize=256m \
-Xms40m \
-Xmx512m \
-Xss4m \
-Xdock:icon=../Resources/Eclipse.icns \
-XstartOnFirstThread \
-Dorg.eclipse.swt.internal.carbon.smallFonts \
-Dosgi.requiredJavaVersion=1.5 \
-Declipse.vm=$JAVA_HOME/bin/java \
-jar $LAUNCHER_JAR 
```

## Frege IDE

Add the **IMP Update Site** to Eclipse:  *http://download.eclipse.org/technology/imp/updates/*

With the *Group items by category* checkbox disabled, install the updates from the **Frege IDE Update Site** at *http://frege.jamestastic.com/updates/*
