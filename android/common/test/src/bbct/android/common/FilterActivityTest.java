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
package bbct.android.common;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import junit.framework.Assert;

/**
 * Tests for subclasses of {@link FilterActivity}.
 *
 * TODO Add tests for configuration changes
 *
 * @param <T> The concrete subclass of {@link FilterActivity} being tested.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public abstract class FilterActivityTest<T extends FilterActivity> extends ActivityInstrumentationTestCase2<T> {

    /**
     * Create instrumented test cases for a subclass of {@link FilterActivity}.
     *
     * @param activityClass The concrete subclass of {@link FilterActivity}
     * being tested.
     */
    public FilterActivityTest(Class<T> activityClass) {
        super(activityClass);
    }

    /**
     * Set up test fixture. This test fixture includes an instance of the
     * subclass of {@link FilterActivity} being tested and the "OK" and "Cancel"
     * {@link Button}s from that activity.
     *
     * @throws Exception If an error occurs while chaining to the super class.
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.inst = this.getInstrumentation();

        this.activity = this.getActivity();
        this.okButton = (Button) this.activity.findViewById(R.id.ok_button);
        this.cancelButton = (Button) this.activity.findViewById(R.id.cancel_button);
    }

    /**
     * Tear down the test fixture by calling {@link Activity#finish()}.
     *
     * @throws Exception If an error occurs while chaining to the super class.
     */
    @Override
    public void tearDown() throws Exception {
        this.activity.finish();

        super.tearDown();
    }

    /**
     * Check preconditions which must hold to guarantee the validity of all
     * other tests. Asserts that the {@link Activity} and "OK" and "Cancel"
     * {@link Button}s are not
     * <code>null</code>.
     */
    public void testPreConditions() {
        Assert.assertNotNull(this.activity);
        Assert.assertNotNull(this.okButton);
        Assert.assertNotNull(this.cancelButton);
    }

    /**
     * Get the substring which should appear in the title bar of the
     * {@link Activity} being tested. Called from the template method
     * {@link #testTitle()}.
     *
     * @return The substring which should appear in the title bar of the
     * {@link Activity} being tested.
     *
     * @see #testTitle()
     */
    protected abstract String getTitleSubString();

    /**
     * Tests that the title bar contains the correct text. Calls
     * {@link #getTitleSubString()} to get the title substring for the concrete
     * subclass of {@link FilterActivity}.
     *
     * @see #getTitleSubString()
     */
    public void testTitle() {
        String title = this.activity.getTitle().toString();
        String titleSubString = this.getTitleSubString();

        Assert.assertTrue(title.contains(titleSubString));
    }

    /**
     * Assert that the correct error messages are set in the {@link EditText}
     * views of the subclass of {@link FilterActivity} when they are empty.
     * Called from the template method
     * {@link #testOkButtonOnClickWithNoInput()}.
     *
     * @see #testOkButtonOnClickWithNoInput()}
     */
    protected abstract void checkErrorMessage();

    /**
     * Asserts that the {@link Activity} does not finish and that the correct
     * error messages appear if the {@link EditText} views are empty. Calls
     * {@link #checkErrorMessage()} to assert that the error messages are
     * correct.
     *
     * @see #checkErrorMessage()
     */
    @UiThreadTest
    public void testOkButtonOnClickWithNoInput() {
        Assert.assertTrue(this.okButton.performClick());
        Assert.assertFalse(this.activity.isFinishing());
        this.checkErrorMessage();
    }

    /**
     * Set the text of any {@link EditText} views in the subclass of
     * {@link FilterActivity} being tested. Called from the template method
     * {@link #testOkButtonOnClickWithSetInputText()}.
     *
     * @see #testOkButtonOnClickWithSetInputText()
     */
    protected abstract void setInputText();

    /**
     * Test that the text of all {@link EditText} views is set correctly and
     * that the {@link Activity} finishes when the user clicks the "OK" button.
     * Calls {@link #setInputText()} to set the text of the {@link EditTex}
     * views in the subclass of {@link FilterActivity} being tested.
     */
    @UiThreadTest
    public void testOkButtonOnClickWithSetInputText() {
        this.setInputText();
        Assert.assertTrue(this.okButton.performClick());
        Assert.assertTrue(this.activity.isFinishing());
        // TODO Assert that the text views contain the correct text?
    }

    /**
     * Inject instrumented key events to the {@link EditText} views. Called from
     * the template method {@link #testOkButtonOnClickWithSendInputKeys()}.
     *
     * @see #testOkButtonOnClickWithSendInputKeys()
     */
    protected abstract void sendInputKeys();

    /**
     * Test that the text of all {@link EditText} correct when injected through
     * instrumentation and that the {@link Activity} finishes when the user
     * clicks the "OK" button. Calls {@link #sendInputKeys()} to inject the user
     * input to the subclass of {@link FilterActivity} being tested.
     */
    public void testOkButtonOnClickWithSendInputKeys() {
        Log.d(TAG, "testOkButtonOnClickWithSendInputKeys()");

        this.sendInputKeys();

        Log.d(TAG, "Run something on the UI thread");

        this.inst.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Can I click the OK button?");
                Assert.assertTrue(FilterActivityTest.this.okButton.performClick());
                Log.d(TAG, "YES!");
            }
        });

        Log.d(TAG, "Did the XxxFilterActivity finish?");
        Assert.assertTrue(FilterActivityTest.this.activity.isFinishing());
        Log.d(TAG, "YES!");
    }

    /**
     * Tests that the {@link Activity} finishes when the user clicks the
     * "Cancel" {@link Button}.
     */
    @UiThreadTest
    public void testCancelButtonOnClick() {
        Assert.assertTrue(this.cancelButton.performClick());
        Assert.assertTrue(this.activity.isFinishing());
    }
    /**
     * The {@link Instrumentation} instance for this test.
     */
    protected Instrumentation inst = null;
    /**
     * The instance of a subclass of {@link FilterActivity} being tested.
     */
    protected Activity activity = null;
    private Button okButton = null;
    private Button cancelButton = null;
    private static final String TAG = "FilterActivityTest";
}
