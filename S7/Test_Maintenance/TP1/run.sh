#! /bin/bash

javac -cp ../../../lib/junit-platform-console-standalone.jar -d out/ src/*.java tests/*.java   
java -jar ../../../lib/junit-platform-console-standalone.jar --class-path out/ --scan-classpath