/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2016 codeguru <codeguru@users.sourceforge.net>
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
package bbct.android.common.test.rule;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import bbct.data.BaseballCard;
import bbct.android.common.test.BaseballCardCsvFileReader;
import bbct.android.common.test.DatabaseUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.junit.rules.ExternalResource;

public class DataTestRule extends ExternalResource {
    private static final String TAG = DataTestRule.class.getName();

    private static final String CARD_DATA = "cards.csv";
    private DatabaseUtil dbUtil;
    private List<BaseballCard> allCards;

    public BaseballCard getCard(int index) {
        return allCards.get(index);
    }

    public List<BaseballCard> getAllCards() {
        return allCards;
    }

    @Override
    protected void before() throws IOException {
        Instrumentation inst = InstrumentationRegistry.getInstrumentation();

        // Create the database and populate table with test data
        InputStream cardInputStream = inst.getContext().getAssets().open(CARD_DATA);
        BaseballCardCsvFileReader cardInput = new BaseballCardCsvFileReader(
                cardInputStream, true);
        allCards = cardInput.getAllBaseballCards();
        cardInput.close();

        dbUtil = new DatabaseUtil(inst.getTargetContext());
        dbUtil.populateTable(allCards);
    }

    @Override
    protected void after() {
        dbUtil.clearDatabase();
    }
}
