#!/bin/sh
##############################################################################
##  Gradle start up script for UN*X
##############################################################################
exec java -Xmx64m -Xms64m -classpath "gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "$@"
