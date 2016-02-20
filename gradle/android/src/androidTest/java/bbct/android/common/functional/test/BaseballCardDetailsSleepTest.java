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

import android.os.RemoteException;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.widget.EditText;
import bbct.android.common.R;
import org.junit.Assert;
import org.junit.Test;

public class BaseballCardDetailsSleepTest extends UiAutomatorTest {
    @Test
    public void testSleep() throws UiObjectNotFoundException, RemoteException {
        String brandHint = inst.getTargetContext().getString(R.string.brand_hint);
        UiSelector brandSelector = new UiSelector().className(EditText.class).text(brandHint);
        UiObject brandUiObject = device.findObject(brandSelector);

        String expectedBrand = "Topps";
        brandUiObject.setText(expectedBrand);
        device.sleep();
        device.wakeUp();
        String actualBrand = brandUiObject.getText();
        Assert.assertEquals(expectedBrand, actualBrand);
    }
}
