#!/bin/bash

# Testing
javac -d threading Main.java MainTest.java PlayingFieldTest.java TestSuite.java TestRunner.java UserThread.java UserTest.java
cd threading
java TestRunner
cd ..
