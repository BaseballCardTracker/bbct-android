from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice

# Amount of time to sleep in order to allow the Android emulator to finish
# a task before taking a screenshot. This is necessary for my slow-ass computer
delay = 5.0

#The directory where screenshots will be saved
screenshots = '../screenshots/'

def read_card_data(filepath):

def take_screenshot(device, filename):
    'Take a screenshot of the given device and save it to the given PNG file in
    the screenshots directory'
    MonkeyRunner.sleep(delay)
    result = device.takeSnapshot()
    result.writeToFile(screenshots + filename, 'png')

device = MonkeyRunner.waitForConnection()

apkFile = '../common/main/bin/bbct-android-common-debug.apk'

if device.installPackage(apkFile):
    package = 'bbct.android.common'
    activity = '.activity.BaseballCardList' 
    runComponent = package + '/' + activity

    device.startActivity(component=runComponent)

    device.press('KEYCODE_MENU', MonkeyDevice.DOWN_AND_UP)
    take_screenshot(device, 'menu.png')

    device.press('KEYCODE_DPAD_CENTER', MonkeyDevice.DOWN_AND_UP)
    take_screenshot(device, 'add-card.png')

    device.removePackage(package)
