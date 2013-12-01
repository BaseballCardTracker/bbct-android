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

positions = ['Pitcher', 'Catcher', 'First Base', 'Second Base', 'Third Base',
             'Shortstop', 'Left Field', 'Center Field', 'Right Field']

class BaseballCard:
    '''Data on a baseball card'''

    def __init__(self, brand, year, number, value, count, player, team,
                 position):
        self.brand = brand
        self.year = year
        self.number = number
        self.value = value
        self.count = count
        self.player = player
        self.team = team
        self.position = position

    def __str__(self):
        return 'BaseballCard['\
            'brand=' + self.brand + ','\
            'year=' + str(self.year) + ','\
            'number=' + str(self.number) + ','\
            'value=' + str(self.value) + ','\
            'count=' + str(self.count) + ','\
            'player=' + self.player + ','\
            'team=' + self.team + ','\
            'position=' + self.position + ']'

def read_card_data(filepath):
    '''Read card data from the given file and return a list of BaseballCard\
       objects'''
    cards = []
    card_file = open(filepath)
    lines = card_file.readlines()

    for line in lines[1:]:
        data = line.strip().split(',')
        card = BaseballCard(*(data[:3] + [10000, 1] + data[3:]))
        cards.append(card)

    card_file.close
    return cards

scrnCnt = 0
def take_screenshot(device, screenshotDir, delay=0.0):
    '''Take a screenshot of the given device and save it to the given PNG file\
       in the screenshots directory'''
    global scrnCnt
    MonkeyRunner.sleep(delay)
    result = device.takeSnapshot()
    result.writeToFile(screenshotDir + '/' + ("%03d" % scrnCnt) + '.png', 'png')
    scrnCnt += 1

currPosIndex = 0
def input_card(device, card):
    '''Input card data assuming that BaseballCardDetails is the currently\
       active activity.'''
    device.type(card.brand)
    device.press('KEYCODE_DPAD_DOWN', MonkeyDevice.DOWN_AND_UP)
    device.type(str(card.year))
    device.press('KEYCODE_DPAD_DOWN', MonkeyDevice.DOWN_AND_UP)
    device.type(str(card.number))
    device.press('KEYCODE_DPAD_DOWN', MonkeyDevice.DOWN_AND_UP)
    device.type(str(card.value / 100.0))
    device.press('KEYCODE_DPAD_DOWN', MonkeyDevice.DOWN_AND_UP)
    device.type(str(card.count))
    device.press('KEYCODE_DPAD_DOWN', MonkeyDevice.DOWN_AND_UP)
    typeWithSpaces(device, card.player)
    device.press('KEYCODE_DPAD_DOWN', MonkeyDevice.DOWN_AND_UP)
    typeWithSpaces(device, card.team)
    device.press('KEYCODE_DPAD_DOWN', MonkeyDevice.DOWN_AND_UP)

#    nextPosIndex = positions.find(card.position)
#    print nextPosIndex

    device.type(card.position)
    device.press('KEYCODE_DPAD_DOWN', MonkeyDevice.DOWN_AND_UP)

    # Click "Save" button
    device.press('KEYCODE_DPAD_LEFT', MonkeyDevice.DOWN_AND_UP)
    device.press('KEYCODE_DPAD_CENTER', MonkeyDevice.DOWN_AND_UP)

def typeWithSpaces(device, str):
    strs = str.split(' ')
    for i in range(len(strs)):
        device.type(strs[i])
        if i != len(strs) - 1:
            device.press('KEYCODE_SPACE', MonkeyDevice.DOWN_AND_UP)
