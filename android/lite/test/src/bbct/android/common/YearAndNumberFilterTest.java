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

import android.view.KeyEvent;
import android.widget.EditText;
import junit.framework.Assert;

/**
 * TODO: Add tests for input year but no number and for input number but no year
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class YearAndNumberFilterTest extends FilterActivityTest<YearAndNumberFilter> {

    public YearAndNumberFilterTest() {
        super(YearAndNumberFilter.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.yearText = (EditText) this.activity.findViewById(R.id.year_text);
        this.numberText = (EditText) this.activity.findViewById(R.id.number_text);

        this.testYear = 1976;
        this.testNumber = 123;
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    public void testPreConditions() {
        super.testPreConditions();

        Assert.assertNotNull(this.yearText);
        Assert.assertNotNull(this.numberText);

        Assert.assertEquals("", this.yearText.getText().toString());
        Assert.assertEquals("", this.numberText.getText().toString());
        Assert.assertTrue(this.yearText.hasFocus());
    }

    @Override
    protected String getTitleSubString() {
        return this.activity.getString(R.string.year_and_number_filter_title);
    }

    @Override
    protected void checkErrorMessage() {
        Assert.fail("Need to test that error message is displayed");
    }

    @Override
    protected void setInputText() {
        this.yearText.setText(Integer.toString(this.testYear));
        this.numberText.setText(Integer.toString(this.testNumber));
    }

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
