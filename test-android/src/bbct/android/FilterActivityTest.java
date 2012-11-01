/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2012 codeguru <codeguru@users.sourceforge.net>
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
package bbct.android;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;
import junit.framework.Assert;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public abstract class FilterActivityTest<T extends Activity> extends ActivityInstrumentationTestCase2<T> {

    public FilterActivityTest(Class<T> activityClass) {
        super(activityClass);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.activity = this.getActivity();
        this.okButton = (Button) this.activity.findViewById(R.id.ok_button);
        this.cancelButton = (Button) this.activity.findViewById(R.id.cancel_button);
    }

    @Override
    public void tearDown() throws Exception {
        this.activity.finish();

        super.tearDown();
    }

    public void testPreConditions() {
        Assert.assertNotNull(this.activity);
        Assert.assertNotNull(this.okButton);
        Assert.assertNotNull(this.cancelButton);
    }

    protected abstract String getTitleSubString();

    public void testTitle() {
        String title = this.activity.getTitle().toString();
        String titleSubString = this.getTitleSubString();

        Assert.assertTrue(title.contains(titleSubString));
    }

    protected abstract void checkErrorMessage();

    @UiThreadTest
    public void testOkButtonOnClickWithNoInput() {
        Assert.assertTrue(this.okButton.performClick());
        Assert.assertFalse(this.activity.isFinishing());
        this.checkErrorMessage();
    }

    protected abstract void setInputText();

    @UiThreadTest
    public void testOkButtonOnClickWithSetInputText() {
        this.setInputText();
        Assert.assertTrue(this.okButton.performClick());
        Assert.assertTrue(this.activity.isFinishing());
    }

    protected abstract void sendInputKeys();

    public void testOkButtonOnClickWithSendInputKeys() throws Throwable {
        this.sendInputKeys();

        this.runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                Assert.assertTrue(FilterActivityTest.this.okButton.performClick());
            }
        });

        this.getInstrumentation().waitForIdleSync();
        Assert.assertTrue(FilterActivityTest.this.activity.isFinishing());
    }

    @UiThreadTest
    public void testCancelButtonOnClick() {
        Assert.assertTrue(this.cancelButton.performClick());
        Assert.assertTrue(this.activity.isFinishing());
    }
    protected Activity activity = null;
    private Button okButton = null;
    private Button cancelButton = null;
}
