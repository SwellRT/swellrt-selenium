#!/bin/bash

WEBDRIVER="/home/pablo/Development/projects/swellrt-selenium/webdriver/chromedriver,/home/pablo/Development/projects/swellrt-selenium/webdriver/geckodriver"
PADURL="http://local.net:9898/test-pad.html?id=pad03"
T1="/home/pablo/Development/projects/swellrt-selenium/input/dracula-part-1.txt,4"
T2="/home/pablo/Development/projects/swellrt-selenium/input/dracula-part-2.txt,8"
T3="/home/pablo/Development/projects/swellrt-selenium/input/input-1.txt,12"

./gradlew jar
java -jar ./build/libs/swellrt-selenium.jar $WEBDRIVER $PADURL $T1 $T2 $T3
