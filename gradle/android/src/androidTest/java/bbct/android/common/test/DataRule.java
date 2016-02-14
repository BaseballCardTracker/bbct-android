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
package bbct.android.common.test;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.util.Log;
import bbct.android.common.data.BaseballCard;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class DataRule implements TestRule {
    private static final String TAG = DataRule.class.getName();

    private List<BaseballCard> allCards;

    @Override
    public Statement apply(Statement statement, Description description) {
        return new DataStatement(statement);
    }

    public BaseballCard getCard(int index) {
        return allCards.get(index);
    }

    public List<BaseballCard> getAllCards() {
        return allCards;
    }

    private class DataStatement extends Statement {
        private static final String CARD_DATA = "cards.csv";
        private DatabaseUtil dbUtil;
        private final Statement statement;

        public DataStatement(Statement statement) {
            this.statement = statement;
        }

        @Override
        public void evaluate() throws Throwable {
            Log.d(TAG, "evaluate()");
            setUp();
            try {
                statement.evaluate();
            } finally {
                tearDown();
            }
        }

        private void setUp() throws IOException {
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

        private void tearDown() {
            dbUtil.clearDatabase();
        }
    }
}
