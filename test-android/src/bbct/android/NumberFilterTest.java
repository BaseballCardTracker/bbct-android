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
import android.widget.EditText;
import junit.framework.Assert;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class NumberFilterTest extends ActivityInstrumentationTestCase2<NumberFilter> {

    public NumberFilterTest() {
        super(NumberFilter.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.activity = this.getActivity();
        this.numberText = (EditText) this.activity.findViewById(R.id.number_text);
        this.okButton = (Button) this.activity.findViewById(R.id.ok_button);
        this.cancelButton = (Button) this.activity.findViewById(R.id.cancel_button);

        this.testNumber = 123;
    }

    @Override
    public void tearDown() throws Exception {
        this.activity.finish();

        super.tearDown();
    }

    public void testPreConditions() {
        Assert.assertNotNull(this.activity);
        Assert.assertNotNull(this.numberText);
        Assert.assertNotNull(this.okButton);
        Assert.assertNotNull(this.cancelButton);

        Assert.assertEquals("", this.numberText.getText().toString());
        Assert.assertTrue(this.numberText.hasFocus());
    }

    public void testTitle() {
        String title = this.activity.getTitle().toString();
        String numberFilterTitle = this.activity.getString(R.string.number_filter_title);

        Assert.assertTrue(title.contains(numberFilterTitle));
    }

    @UiThreadTest
    public void testOkButtonOnClickWithNoNumber() {
        Assert.assertTrue(this.okButton.performClick());
        Assert.assertFalse(this.activity.isFinishing());
        Assert.fail("Need to test that error message is displayed");
    }

    @UiThreadTest
    public void testOkButtonOnClickWithNumber() {
        this.numberText.setText(Integer.toString(this.testNumber));
        Assert.assertTrue(this.okButton.performClick());
        Assert.assertTrue(this.activity.isFinishing());
    }

    public void testOkButtonOnClickWithUserInputNumber() throws Throwable {
        AndroidTestUtil.sendKeysFromInt(this, testNumber);

        this.runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                Assert.assertTrue(NumberFilterTest.this.okButton.performClick());
            }
        });

        this.getInstrumentation().waitForIdleSync();
        Assert.assertTrue(NumberFilterTest.this.activity.isFinishing());
    }

    @UiThreadTest
    public void testCancelButtonOnClick() {
        Assert.assertTrue(this.cancelButton.performClick());
        Assert.assertTrue(this.activity.isFinishing());
    }
    private Activity activity = null;
    private EditText numberText = null;
    private Button okButton = null;
    private Button cancelButton = null;
    private int testNumber = -1;
}
