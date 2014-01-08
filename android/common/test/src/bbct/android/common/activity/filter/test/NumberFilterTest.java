/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2012-14 codeguru <codeguru@users.sourceforge.net>
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
package bbct.android.common.activity.filter.test;

import android.app.Activity;
import android.widget.EditText;
import bbct.android.common.R;
import bbct.android.common.activity.filter.NumberFilter;
import junit.framework.Assert;

/**
 * Tests for {@link NumberFilter}.
 */
public class NumberFilterTest extends FilterActivityTest<NumberFilter> {

    /**
     * Create instrumented test cases for {@link NumberFilter}.
     */
    public NumberFilterTest() {
        super(NumberFilter.class);
    }

    /**
     * Set up test fixture. Most of the test fixture is set up by
     * {@link FilterActivityTest#setUp()}. This class adds a {@link EditText}
     * view which contain card number value being edited and an
     * <code>int</code> value for card number.
     *
     * @throws Exception If an error occurs while chaining to the super class.
     *
     * @see FilterActivityTest#setUp()
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.numberText = (EditText) this.activity.findViewById(R.id.number_text);

        this.testNumber = 123;
    }

    /**
     * Check preconditions which must hold to guarantee the validity of all
     * other tests. Most preconditions are checked by
     * {@link FilterActivityTest#testPreConditions()}. In addition, this class
     * checks that the {@link EditText} view for card number is not
     * <code>null</code>, that it is empty, and that it has focus.
     *
     * @see FilterActivityTest#testPreConditions()
     */
    @Override
    public void testPreConditions() {
        super.testPreConditions();

        Assert.assertNotNull(this.numberText);

        Assert.assertEquals("", this.numberText.getText().toString());
        Assert.assertTrue(this.numberText.hasFocus());
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
        return this.activity.getString(R.string.number_filter_title);
    }

    /**
     * Assert that the correct error message is set in the {@link EditText} view
     * when it is empty.
     *
     * @see FilterActivityTest#testOkButtonOnClickWithNoInput()
     */
    @Override
    protected void checkErrorMessage() {
        String expectedNumberError = this.activity.getString(R.string.number_input_error);
        Assert.assertEquals(expectedNumberError, this.numberText.getError());
    }

    /**
     * Inject instrumented key events to the card number {@link EditText} view.
     *
     * @see FilterActivityTest#testOkButtonOnClickWithSendInputKeys()
     */
    @Override
    protected void sendInputKeys() {
        this.getInstrumentation().sendStringSync(Integer.toString(this.testNumber));
    }
    private EditText numberText = null;
    private int testNumber = -1;
}
