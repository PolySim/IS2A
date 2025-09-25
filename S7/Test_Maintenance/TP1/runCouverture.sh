#! /bin/bash

java -javaagent:../../../lib/jacoco/lib/jacocoagent.jar -cp ../../../lib/junit-platform-console-standalone.jar:out org.junit.platform.console.ConsoleLauncher --scan-classpath
java -jar ../../../lib/jacoco/lib/jacococli.jar report jacoco.exec --classfiles out --sourcefiles src --html report
