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

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.util.Log;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.test.rule.DataTestRule;
import java.io.File;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class Screenshots extends UiAutomatorTest {
    @Rule
    public DataTestRule dataTestRule = new DataTestRule();

    private static final String TAG = Screenshots.class.getName();

    private int screenshotCount;
    private BaseballCard card;
    private File screenshotDir;

    @Before
    public void setUp() throws UiObjectNotFoundException {
        super.setUp();
        screenshotCount = 1;
        screenshotDir = InstrumentationRegistry.getTargetContext().getExternalFilesDir(null);
        card = dataTestRule.getCard(0);
    }

    @Test
    public void takeScreenshots() {
        File screenshotFile;

        screenshotFile
                = new File(screenshotDir, String.format("%02d-CardList.png", screenshotCount++));
        Log.d(TAG, screenshotFile.getAbsolutePath());
        device.takeScreenshot(screenshotFile);
    }
}
