#! /bin/bash

javac -cp src/lib/junit-platform-console-standalone.jar -d out/ src/app/*.java src/app/container/*.java src/game/composent/*.java src/game/exceptions/*.java src/tests/*.java   
java -jar src/lib/junit-platform-console-standalone.jar --class-path out/ --scan-classpath