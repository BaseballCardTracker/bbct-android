/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2015 codeguru <codeguru@users.sourceforge.net>
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

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;

import androidx.test.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.After;
import org.junit.Before;

import bbct.android.common.R;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public abstract class UiAutomatorTest {
    private static final int LAUNCH_TIMEOUT = 5000;

    protected UiDevice device;
    protected Instrumentation inst;
    protected Context context;

    private String targetPackage;

    @Before
    public void setUp() throws UiObjectNotFoundException {
        inst = InstrumentationRegistry.getInstrumentation();
        device = UiDevice.getInstance(inst);
        context = InstrumentationRegistry.getTargetContext();
        targetPackage = context.getPackageName();
        startApp();
    }

    @After
    public void tearDown() {
        device.pressHome();
    }

    private void startApp() {
        // Start from the home screen
        device.pressHome();

        // Wait for launcher
        final String launcherPackage = device.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        // Launch the app
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(targetPackage);
        // Clear out any previous instances
        assert intent != null;
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        device.wait(Until.hasObject(By.pkg(targetPackage).depth(0)), LAUNCH_TIMEOUT);
    }

    protected void clickLaterButton() throws UiObjectNotFoundException {
        UiSelector laterSelector = new UiSelector().text(context.getString(R.string.later));
        UiObject laterButton = device.findObject(laterSelector);
        laterButton.click();
    }
}
