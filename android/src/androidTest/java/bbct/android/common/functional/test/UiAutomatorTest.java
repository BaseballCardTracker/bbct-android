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
package bbct.android.common.functional.test;

import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import bbct.android.common.R;
import org.junit.After;
import org.junit.Before;

public abstract class UiAutomatorTest {
    protected UiDevice device;
    protected Instrumentation inst;
    protected Context context;


    @Before
    public void setUp() throws UiObjectNotFoundException {
        inst = InstrumentationRegistry.getInstrumentation();
        device = UiDevice.getInstance(inst);
        context = InstrumentationRegistry.getTargetContext();
        startApp();
    }

    @After
    public void tearDown() {
        device.pressHome();
    }

    private void startApp() throws UiObjectNotFoundException {
        device.pressHome();
        UiObject allAppsButton = device.findObject(new UiSelector().description("Apps"));
        allAppsButton.clickAndWaitForNewWindow();
        UiObject appsTab = device.findObject(new UiSelector().text("Apps"));
        appsTab.click();
        UiScrollable appViews = new UiScrollable(new UiSelector().scrollable(true));
        appViews.setAsHorizontalList();
        String appName = inst.getTargetContext().getString(R.string.app_name);
        UiObject ourApp = appViews.getChildByText(
                new UiSelector().className("android.widget.TextView"), appName);
        ourApp.clickAndWaitForNewWindow();
    }

    protected void clickLaterButton() throws UiObjectNotFoundException {
        UiSelector laterSelector = new UiSelector().text(context.getString(R.string.later));
        UiObject laterButton = device.findObject(laterSelector);
        laterButton.click();
    }
}
