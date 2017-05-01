#!/bin/bash

WEBDRIVER="/home/pablo/Development/projects/swellrt-selenium/webdriver/chromedriver,/home/pablo/Development/projects/swellrt-selenium/webdriver/geckodriver"
PADURL="http://localhost:9898/test-pad.html?id=test3"
T1="/home/pablo/Development/projects/swellrt-selenium/input/input-1.txt,4"
T2="/home/pablo/Development/projects/swellrt-selenium/input/input-2.txt,8"
T3="/home/pablo/Development/projects/swellrt-selenium/input/input-1.txt,12"
T4="/home/pablo/Development/projects/swellrt-selenium/input/input-2.txt,16"

java -jar ./swellrt-selenium.jar $WEBDRIVER $PADURL $T1 $T2 $T3 
