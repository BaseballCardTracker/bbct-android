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
import android.util.Log;
import android.widget.Button;
import bbct.common.data.BaseballCard;
import java.io.IOException;
import java.io.InputStream;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardDetailsPartialInputTest extends ActivityInstrumentationTestCase2<BaseballCardDetails> {

    public static Test suite() {
        TestSuite suite = new TestSuite(BaseballCardDetailsPartialInputTest.class.getName());

        for (int inputFieldsMask = 0x00; inputFieldsMask < BBCTTestUtil.ALL_FIELDS; ++inputFieldsMask) {
            suite.addTest(new BaseballCardDetailsPartialInputTest(inputFieldsMask));
        }

        return suite;
    }

    public BaseballCardDetailsPartialInputTest(int inputFieldsMask) {
        super(BaseballCardDetails.class);

        this.setName(TEST_NAME);
        this.inputFieldsMask = inputFieldsMask;
    }

    @Override
    public void setUp() throws IOException {
        this.inst = this.getInstrumentation();

        InputStream in = this.inst.getContext().getAssets().open(BBCTTestUtil.CARD_DATA);
        BaseballCardCsvFileReader cardInput = new BaseballCardCsvFileReader(in, true);
        this.card = cardInput.getNextBaseballCard();
        cardInput.close();

        this.activity = this.getActivity();
        this.saveButton = (Button) this.activity.findViewById(R.id.save_button);
    }

    public void testPartialInput() {
        Log.d(TAG, "testPartialInput()");
        Log.d(TAG, "inputFieldsMask=" + inputFieldsMask);

        BBCTTestUtil.sendKeysToCardDetails(this, this.activity, this.card, this.inputFieldsMask);
        this.inst.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                Assert.assertTrue(BaseballCardDetailsPartialInputTest.this.saveButton.performClick());
            }
        });

//        String expectedErrorMessage = this.activity.getString(expectedErrorMessageId);
        Assert.fail("Check error message");
    }

    private Activity activity = null;
    private Instrumentation inst = null;
    private Button saveButton = null;
    private BaseballCard card = null;
    private final int inputFieldsMask;
    private static final String TEST_NAME = "testPartialInput";
    private static final String TAG = BaseballCardDetailsPartialInputTest.class.getName();
}
