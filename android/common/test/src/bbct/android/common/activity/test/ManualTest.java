/*
 * This file is part of bbct.
 *
 * Copyright 2012 codeguru <codeguru@users.sourceforge.net>
 *
 * bbct is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * bbct is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bbct.android.common.activity.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import bbct.android.common.activity.BaseballCardList;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.test.util.BBCTTestUtil;
import bbct.android.common.test.util.BaseballCardCsvFileReader;
import bbct.android.common.test.util.DatabaseUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * This test adds data to the app's database so that a tester can perform manual
 * tests.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class ManualTest extends ActivityInstrumentationTestCase2<BaseballCardList> {

    /**
     * Create instrumented test cases for {@link BaseballCardList}.
     */
    public ManualTest() {
        super(BaseballCardList.class);
    }

    /**
     * Adds data to the app's database.
     *
     * @throws IOException If an error occurs while populating the database.
     */
    public void testManually() throws IOException {
        Activity activity = this.getActivity();
        InputStream in = this.getInstrumentation().getContext().getAssets().open(BBCTTestUtil.CARD_DATA);
        BaseballCardCsvFileReader input = new BaseballCardCsvFileReader(in, true);
        List<BaseballCard> allCards = input.getAllBaseballCards();
        DatabaseUtil dbUtil = new DatabaseUtil(activity.getPackageName());
        dbUtil.populateTable(allCards);
    }
}
