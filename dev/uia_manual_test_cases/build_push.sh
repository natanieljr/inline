#!/bin/bash

ant build
adb push ./bin/uia_manual_test_cases.jar data/local/tmp