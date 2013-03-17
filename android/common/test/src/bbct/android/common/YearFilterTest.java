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
import android.widget.EditText;
import junit.framework.Assert;

/**
 * Tests for {@link YearFilter}.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class YearFilterTest extends FilterActivityTest<YearFilter> {

    /**
     * Create instrumented test cases for {@link YearFilter}.
     */
    public YearFilterTest() {
        super(YearFilter.class);
    }

    /**
     * Set up test fixture. Most of the test fixture is set up by
     * {@link FilterActivityTest#setUp()}. This class adds a {@link EditText}
     * view which contains the year value being edited and an
     * <code>int</code> value for the year.
     *
     * @throws Exception If an error occurs while chaining to the super class.
     *
     * @see FilterActivityTest#setUp()
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.yearText = (EditText) this.activity.findViewById(R.id.year_text);

        this.testYear = 1976;
    }

    /**
     * Tear down the test fixture by calling {@link Activity#finish()}.
     *
     * @throws Exception If an error occurs while chaining to the super class.
     *
     * @see FilterActivityTest#tearDown()
     */
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Check preconditions which must hold to guarantee the validity of all
     * other tests. Most preconditions are checked by
     * {@link FilterActivityTest#testPreConditions()}. In addition, this class
     * checks that the {@link EditText} view for the year is not
     * <code>null</code>, that it is empty, and that it has focus.
     *
     * @see FilterActivityTest#testPreConditions()
     */
    @Override
    public void testPreConditions() {
        super.testPreConditions();

        Assert.assertNotNull(this.yearText);

        Assert.assertEquals("", this.yearText.getText().toString());
        Assert.assertTrue(this.yearText.hasFocus());
    }

    /**
     * Get the substring representing this {@link Activity} which should appear
     * in the title bar.
     *
     * @return The substring representing this {@link Activity} which should
     * appear in the title bar.
     *
     * @see FilterActivityTest#testTitle()
     */
    @Override
    protected String getTitleSubString() {
        return this.activity.getString(R.string.year_filter_title);
    }

    /**
     * Assert that the correct error message is set in the {@link EditText} view
     * when it is empty.
     *
     * @see FilterActivityTest#testOkButtonOnClickWithNoInput()}
     */
    @Override
    protected void checkErrorMessage() {
        String expectedError = this.activity.getString(R.string.year_input_error);
        Assert.assertEquals(expectedError, this.yearText.getError());
    }

    /**
     * Set the text of the year {@link EditText} view.
     *
     * @see FilterActivityTest#testOkButtonOnClickWithSetInputText()
     */
    @Override
    protected void setInputText() {
        this.yearText.setText(Integer.toString(this.testYear));
    }

    /**
     * Inject instrumented key events for the year text.
     *
     * @see FilterActivityTest#testOkButtonOnClickWithSendInputKeys()
     */
    @Override
    protected void sendInputKeys() {
        this.getInstrumentation().sendStringSync(Integer.toString(this.testYear));
    }
    private EditText yearText = null;
    private int testYear = -1;
}
