#! /bin/bash

javac -cp src/lib/junit-platform-console-standalone.jar -d out/ src/game/composent/*.java src/tests/*.java   
java -jar src/lib/junit-platform-console-standalone.jar --class-path out/ --scan-classpath