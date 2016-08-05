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
package bbct.android.common.functional.test;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;

import bbct.android.common.R;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.test.rule.DataTestRule;
import java.io.File;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class Screenshots extends UiAutomatorTest {
    @Rule
    public DataTestRule dataTestRule = new DataTestRule();

    private static final String TAG = Screenshots.class.getName();
    private static final long TIMEOUT = 5000;

    private Context context;
    private File screenshotDir;
    private int screenshotCount;
    private BaseballCard card;

    @Before
    public void setUp() throws UiObjectNotFoundException {
        super.setUp();
        screenshotCount = 1;
        context = InstrumentationRegistry.getTargetContext();
        screenshotDir = new File(context.getFilesDir(), "screenshots");

        if (!screenshotDir.exists()) {
            assertTrue("Unable to create directory: " + screenshotDir.getName(), screenshotDir.mkdir());
        }

        Log.d(TAG, "screenshotDir: " + screenshotDir);

        card = dataTestRule.getCard(0);
    }

    @Test
    public void takeScreenshots() throws Throwable {
        UiSelector laterSelector = new UiSelector().text(context.getString(R.string.later));
        UiObject laterButton = device.findObject(laterSelector);
        laterButton.click();

        UiSelector addSelector = new UiSelector().description(context.getString(R.string.add_menu));
        UiObject addMenu = device.findObject(addSelector);
        addMenu.click();
        device.waitForWindowUpdate(context.getPackageName(), TIMEOUT);
        takeScreenshot("NewCard");

        device.pressBack();
        device.waitForWindowUpdate(context.getPackageName(), TIMEOUT);
        takeScreenshot("CardList");

        UiSelector playerNameSelector = new UiSelector().text(card.getPlayerName());
        UiObject listItem = device.findObject(playerNameSelector);
        listItem.click();
        device.waitForWindowUpdate(context.getPackageName(), TIMEOUT);
        device.pressBack();
        takeScreenshot("CardDetails");
    }

    private void takeScreenshot(String description) {
        File screenshotFile
                = new File(screenshotDir,
                           String.format("%02d-%s.png", screenshotCount++, description));
        device.takeScreenshot(screenshotFile);
    }
}
