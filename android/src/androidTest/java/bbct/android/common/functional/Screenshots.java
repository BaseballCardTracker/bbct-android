/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2016 codeguru <codeguru@users.sourceforge.net>
 *
 * BBCT for Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BBCT for Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bbct.android.common.functional;

import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import tools.fastlane.screengrab.Screengrab;
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy;

import java.io.File;

import bbct.android.common.R;
import bbct.android.common.database.BaseballCard;
import bbct.android.common.test.rule.DataTestRule;

import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class Screenshots extends UiAutomatorTest {
    @Rule
    public DataTestRule dataTestRule = new DataTestRule();

    private static final String TAG = Screenshots.class.getName();
    private static final long TIMEOUT = 5000;
    private static final String YEAR_STRING = "1993";
    public static final int YEAR_INSTANCE = 1;

    private int screenshotCount;
    private BaseballCard card;

    @Before
    public void setUp() throws UiObjectNotFoundException {
        super.setUp();
        screenshotCount = 1;
        card = dataTestRule.getCard(0);
        Screengrab.setDefaultScreenshotStrategy(new UiAutomatorScreenshotStrategy());
    }

    @Test
    public void takeScreenshots() throws Throwable {
        UiSelector addSelector = new UiSelector().description(context.getString(R.string.add_menu));
        UiObject addMenu = device.findObject(addSelector);
        addMenu.click();
        device.waitForWindowUpdate(context.getPackageName(), TIMEOUT);
        takeScreenshot("NewCard");

        device.pressBack();
        device.waitForWindowUpdate(context.getPackageName(), TIMEOUT);
        takeScreenshot("CardList");

        UiSelector playerNameSelector = new UiSelector().text(card.playerName);
        UiObject listItem = device.findObject(playerNameSelector);
        listItem.click();
        device.waitForWindowUpdate(context.getPackageName(), TIMEOUT);
        device.pressBack();
        takeScreenshot("CardDetails");

        device.pressBack();
        UiSelector filterSelector = new UiSelector().description(context.getString(R.string.filter_menu));
        UiObject filterMenu = device.findObject(filterSelector);
        filterMenu.click();
        device.waitForWindowUpdate(context.getPackageName(), TIMEOUT);
        UiSelector yearCheckBoxSelector = new UiSelector().className(CheckBox.class).instance(YEAR_INSTANCE);
        UiObject yearCheckBox = device.findObject(yearCheckBoxSelector);
        yearCheckBox.click();
        UiSelector yearTextViewSelector = new UiSelector().className(EditText.class).instance(YEAR_INSTANCE);
        UiObject yearTextView = device.findObject(yearTextViewSelector);
        yearTextView.setText(YEAR_STRING);
        device.waitForIdle();
        takeScreenshot("FilterCards");

        UiSelector saveSelector = new UiSelector().description(context.getString(R.string.save_menu));
        UiObject saveMenu = device.findObject(saveSelector);
        saveMenu.click();
        device.waitForWindowUpdate(context.getPackageName(), TIMEOUT);
        takeScreenshot("FilteredList");

        UiSelector overflowSelector = new UiSelector().description("More options");
        UiObject overflowMenu = device.findObject(overflowSelector);
        overflowMenu.click();
        device.waitForWindowUpdate(context.getPackageName(), TIMEOUT);
        UiSelector aboutSelector = new UiSelector().text(context.getString(R.string.about_menu));
        UiObject aboutMenu = device.findObject(aboutSelector);
        aboutMenu.click();
        device.waitForWindowUpdate(context.getPackageName(), TIMEOUT);
        takeScreenshot("About");
    }

    private void takeScreenshot(String description) {
        String screenshotName = String.format("%02d-%s", screenshotCount++,
               description);
        Log.d("screenshot", screenshotName);
        Screengrab.screenshot(screenshotName);
    }
}
