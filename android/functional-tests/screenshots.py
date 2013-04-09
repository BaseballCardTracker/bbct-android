from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice

# Amount of time to sleep in order to allow the Android emulator to finish
# a task before taking a screenshot. This is necessary for my slow-ass computer
delay = 5.0

#The directory where screenshots will be saved
screenshots = '../screenshots/'

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

    def __str__(self):
        return 'BaseballCard['\
            'brand=' + self.brand + ','\
            'year=' + str(self.year) + ','\
            'number=' + str(self.number) + ','\
            'value=' + str(self.value) + ','\
            'count=' + str(self.count) + ','\
            'player=' + self.player + ','\
            'team=' + self.team + ']'

def read_card_data(filepath):
    '''Read card data from the given file and return a list of BaseballCard\
       objects'''
    cards = []
    card_file = open(filepath)

    for card_line in card_file:
        card_data = card_line.strip().split(',')
        card = BaseballCard(*(card_data[:3] + [10000, 1] + card_data[3:]))
        cards.append(card)

    card_file.close
    return cards

def take_screenshot(device, filename):
    '''Take a screenshot of the given device and save it to the given PNG file\
       in the screenshots directory'''
    MonkeyRunner.sleep(delay)
    result = device.takeSnapshot()
    result.writeToFile(screenshots + filename, 'png')

device = MonkeyRunner.waitForConnection()

apkFile = '../common/main/bin/bbct-android-common-debug.apk'

if device.installPackage(apkFile):
    cards = read_card_data('cards.csv')

    package = 'bbct.android.common'
    activity = '.activity.BaseballCardList' 
    runComponent = package + '/' + activity

    device.startActivity(component=runComponent)

    device.press('KEYCODE_MENU', MonkeyDevice.DOWN_AND_UP)
    take_screenshot(device, 'menu.png')

    device.press('KEYCODE_DPAD_CENTER', MonkeyDevice.DOWN_AND_UP)
    take_screenshot(device, 'add-card.png')

    device.removePackage(package)
