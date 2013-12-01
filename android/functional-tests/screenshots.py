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
import util

import os
import sys

defaultPackage = 'bbct.android.common'
defaultDbFileName = 'data/bbct.db'
defaultDelay = 30.0

parser = OptionParser(usage='Usage: %prog [options] <APK file> <list activity>')
parser.add_option('-k', '--package', dest='package', help='Android package name', default=defaultPackage)
parser.add_option('-f', '--dbfile', dest='dbfile', help='Path to local database file', default=defaultDbFileName)
parser.add_option('-d', '--delay', dest='delay', type='float', help='delay before taking a screenshot', default=defaultDelay)

try:
    options, (apkFile, activity) = parser.parse_args()
except ValueError:
    parser.print_help()
    sys.exit(1)

package = options.package
dbFileName = "bbct.db"
localDb = options.dbfile
remoteDb = "/data/data/" + package + "/databases/" + dbFileName

runComponent = package + '/' + activity

# Amount of time to sleep in order to allow the Android emulator to finish
# a task before taking a screenshot. This is necessary for my slow-ass computer
delay = options.delay

print("Connecting to device...")
device = MonkeyRunner.waitForConnection()

print("Installing APK: " + apkFile + "...")
if device.installPackage(apkFile):
    print("Reading baseball card data...")
    cards = util.read_card_data('data/cards.csv')

    for card in cards:
        print card

    print("Starting activity: " + runComponent + "...")
    device.startActivity(component=runComponent)
    MonkeyRunner.sleep(delay)
    util.take_screenshot(device, 'start.png', delay)

    print("Menu...")
    device.press('KEYCODE_MENU', MonkeyDevice.DOWN_AND_UP)
    util.take_screenshot(device, 'menu.png', delay)

    print("Add card...")
    device.press('KEYCODE_DPAD_CENTER', MonkeyDevice.DOWN_AND_UP)
    util.take_screenshot(device, 'add-card-blank.png', delay)

    print("Enter data...")
    util.input_card(device, cards[0])
    util.take_screenshot(device, 'add-card-filled.png', delay)

    print("Removing package: " + package + "...")
    device.removePackage(package)
