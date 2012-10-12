package bbct.android;

import android.test.ActivityInstrumentationTestCase2;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class bbct.android.BaseballCardListTest \
 * bbct.android.tests/android.test.InstrumentationTestRunner
 */
public class BaseballCardListTest extends ActivityInstrumentationTestCase2<BaseballCardList> {

    public BaseballCardListTest() {
        super("bbct.android", BaseballCardList.class);
    }

}
