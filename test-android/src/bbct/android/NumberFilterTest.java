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

import android.widget.EditText;
import junit.framework.Assert;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class NumberFilterTest extends FilterActivityTest<NumberFilter> {

    public NumberFilterTest() {
        super(NumberFilter.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.numberText = (EditText) this.activity.findViewById(R.id.number_text);

        this.testNumber = 123;
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    public void testPreConditions() {
        super.testPreConditions();

        Assert.assertNotNull(this.numberText);

        Assert.assertEquals("", this.numberText.getText().toString());
        Assert.assertTrue(this.numberText.hasFocus());
    }

    @Override
    protected String getTitleSubString() {
        return this.activity.getString(R.string.number_filter_title);
    }

    @Override
    protected void checkErrorMessage() {
        Assert.fail("Need to test that error message is displayed");
    }

    @Override
    protected void setInputText() {
        this.numberText.setText(Integer.toString(this.testNumber));
    }

    @Override
    protected void sendInputKeys() {
        AndroidTestUtil.sendKeysFromInt(this, this.testNumber);
    }
    private EditText numberText = null;
    private int testNumber = -1;
}
