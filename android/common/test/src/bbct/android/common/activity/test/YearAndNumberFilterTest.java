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
package bbct.android.common.activity.test;

import android.app.Activity;
import android.view.KeyEvent;
import android.widget.EditText;
import bbct.android.common.R;
import bbct.android.common.activity.YearAndNumberFilter;
import junit.framework.Assert;

/**
 * Tests for {@link YearAndNumberFilter}.
 *
 * TODO: Add tests for input year but no number and for input number but no year
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class YearAndNumberFilterTest extends FilterActivityTest<YearAndNumberFilter> {

    /**
     * Create instrumented test cases for {@link YearAndNumberFilter}.
     */
    public YearAndNumberFilterTest() {
        super(YearAndNumberFilter.class);
    }

    /**
     * Set up test fixture. Most of the test fixture is set up by
     * {@link FilterActivityTest#setUp()}. This class adds {@link EditText}
     * views which contain the year and card number values being edited and
     * <code>int</code> values for the year and card number.
     *
     * @throws Exception If an error occurs while chaining to the super class.
     *
     * @see FilterActivityTest#setUp()
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.yearText = (EditText) this.activity.findViewById(R.id.year_text);
        this.numberText = (EditText) this.activity.findViewById(R.id.number_text);

        this.testYear = 1976;
        this.testNumber = 123;
    }

    /**
     * Check preconditions which must hold to guarantee the validity of all
     * other tests. Most preconditions are checked by
     * {@link FilterActivityTest#testPreConditions()}. In addition, this class
     * checks that the {@link EditText} views for the year and card number is
     * not
     * <code>null</code>, that they are empty, and that the {@link EditText}
     * view for the year has focus.
     *
     * @see FilterActivityTest#testPreConditions()
     */
    @Override
    public void testPreConditions() {
        super.testPreConditions();

        Assert.assertNotNull(this.yearText);
        Assert.assertNotNull(this.numberText);

        Assert.assertEquals("", this.yearText.getText().toString());
        Assert.assertEquals("", this.numberText.getText().toString());
        Assert.assertTrue(this.yearText.hasFocus());
    }

    /**
     * Get the substring which should appear in the title bar of the
     * {@link Activity} being tested.
     *
     * @return The substring which should appear in the title bar of the
     * {@link Activity} being tested.
     *
     * @see FilterActivityTest#testTitle()
     */
    @Override
    protected String getTitleSubString() {
        return this.activity.getString(R.string.year_and_number_filter_title);
    }

    /**
     * Assert that the correct error message is set in the {@link EditText}
     * views when they are empty.
     *
     * @see FilterActivityTest#testOkButtonOnClickWithNoInput()
     */
    @Override
    protected void checkErrorMessage() {
        // TODO: Need two other tests where only one of the two fields are filled
        String expectedYearError = this.activity.getString(R.string.year_input_error);
        Assert.assertEquals(expectedYearError, this.yearText.getError());

        String expectedNumberError = this.activity.getString(R.string.number_input_error);
        Assert.assertEquals(expectedNumberError, this.numberText.getError());
    }

    /**
     * Set the text of the year and card number {@link EditText} views.
     *
     * @see FilterActivityTest#testOkButtonOnClickWithSetInputText()
     */
    @Override
    protected void setInputText() {
        this.yearText.setText(Integer.toString(this.testYear));
        this.numberText.setText(Integer.toString(this.testNumber));
    }

    /**
     * Inject instrumented key events to the year and card number
     * {@link EditText} views.
     *
     * @see FilterActivityTest#testOkButtonOnClickWithSendInputKeys()
     */
    @Override
    protected void sendInputKeys() {
        this.getInstrumentation().sendStringSync(Integer.toString(this.testYear));
        this.sendKeys(KeyEvent.KEYCODE_ENTER);
        this.getInstrumentation().sendStringSync(Integer.toString(this.testNumber));
    }
    private EditText yearText = null;
    private EditText numberText = null;
    private int testYear = -1;
    private int testNumber = -1;
}
