#!/bin/bash

adb shell uiautomator runtest uia_manual_test_cases.jar -c org.droidmate.uia_manual_test_cases.${1}TestCases#test_useCase_$2