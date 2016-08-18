# This file is part of BBCT for Android.
#
# Copyright 2012 codeguru <codeguru@users.sourceforge.net>
#
# BBCT for Android is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.

# BBCT for Android is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.

# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.

from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice
import util

apkFile = '../common/main/bin/bbct-android-common-debug.apk'
package = 'bbct.android.common'
activity = '.activity.BaseballCardList' 
runComponent = package + '/' + activity

# Amount of time to sleep in order to allow the Android emulator to finish
# a task before taking a screenshot. This is necessary for my slow-ass computer
delay = 10.0

print("Connecting to device...")
device = MonkeyRunner.waitForConnection()

print("Installing APK: " + apkFile + "...")
if device.installPackage(apkFile):
    print("Starting activity: " + runComponent + "...")
    device.startActivity(component=runComponent)

    # Insert code here

    print("Removing package: " + package + "...")
    device.removePackage(package)
