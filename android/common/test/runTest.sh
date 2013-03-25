#!/bin/bash
adb shell am instrument -w -e class $1 bbct.android.common.tests/android.test.InstrumentationTestRunner