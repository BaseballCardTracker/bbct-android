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
from optparse import OptionParser
import os
import sys

defaultPackage = 'bbct.android.common'
defaultDbFileName = 'data/bbct.db'

parser = OptionParser(usage='Usage: %prog [options] <APK file>')
parser.add_option('-k', '--package', dest='package', help='Android package name', default=defaultPackage)
parser.add_option('-f', '--dbfile', dest='dbfile', help='Path to local database file', default=defaultDbFileName)

try:
    options, (apkFile,) = parser.parse_args()
except ValueError:
    parser.print_help()
    sys.exit(1)

package = options.package
dbFileName = "bbct.db"
localDb = options.dbfile
remoteDb = "/data/data/" + package + "/databases/" + dbFileName

# Amount of time to sleep in order to allow the Android emulator to finish
# a task before taking a screenshot. This is necessary for my slow-ass computer
delay = 10.0

print("Connect to device...")
device = MonkeyRunner.waitForConnection()

print("Install APK: " + apkFile + "...")
if device.installPackage(apkFile):
    print("Push database to device...")
    os.system("adb push " + localDb + " " + remoteDb)
