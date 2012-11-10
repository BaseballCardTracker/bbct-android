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

import android.app.Activity;
import android.app.Instrumentation;
import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import bbct.common.data.BaseballCard;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardListTest extends ActivityInstrumentationTestCase2<BaseballCardList> {

    public BaseballCardListTest() {
        super(BaseballCardList.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.inst = this.getInstrumentation();

        this.activity = this.getActivity();
        this.list = (ListView) this.activity.findViewById(android.R.id.list);

        InputStream cardInputStream = this.inst.getContext().getAssets().open(DATA_ASSET);
        this.cardInput = new BaseballCardCsvFileReader(cardInputStream, true);
        this.dbUtil = new DatabaseUtil();
    }

    @Override
    public void tearDown() throws Exception {
        this.activity.finish();
        this.dbUtil.deleteDatabase();
        this.cardInput.close();

        super.tearDown();
    }

    public void testPreConditions() {
        Assert.assertNotNull(this.activity);
        Assert.assertNotNull(this.list);

        // Check that database was created with the correct version and table
        SQLiteDatabase db = this.dbUtil.getDatabase();
        Assert.assertNotNull(db);
        Assert.assertEquals(BaseballCardSQLHelper.SCHEMA_VERSION, db.getVersion());
        Assert.assertEquals(BaseballCardSQLHelper.TABLE_NAME, SQLiteDatabase.findEditTable(BaseballCardSQLHelper.TABLE_NAME));
    }

    public void testTitle() {
        String expectedTitle = this.activity.getString(R.string.app_name);

        Assert.assertEquals(expectedTitle, this.activity.getTitle());
    }

    public void testStartActivityWithEmptyDatabase() {
        Assert.fail("Check that instructions are raised as a Toast.");
    }

    public void testStateDestroy() {
        Assert.fail("Implement me!");
    }

    public void testStatePause() {
        Assert.fail("Implement me!");
    }

    public void testMenuLayoutNoFilter() {
        this.sendKeys(KeyEvent.KEYCODE_MENU);
        Assert.fail("Now how do I check the contents of the menu?");
    }

    public void testMenuLayoutWithFilter() {
        Assert.fail("Implement me!");
    }

    public void testAddCardsMenuItem() {
        Activity cardDetails = this.testMenuItem(R.id.add_menu, BaseballCardDetails.class);

        cardDetails.finish();
        Assert.assertTrue(cardDetails.isFinishing());
    }

    public void testFilterCardsMenuItem() {
        Activity filterOptions = this.testMenuItem(R.id.filter_menu, FilterOptions.class);

        filterOptions.finish();
        Assert.assertTrue(filterOptions.isFinishing());
    }

    public void testAboutMenuItem() {
        Activity about = this.testMenuItem(R.id.about_menu, About.class);

        about.finish();
        Assert.assertTrue(about.isFinishing());
    }

    /**
     * Test of {@link BaseballCardList#onListItemClick}.
     */
    public void testOnListItemClick() {
        System.out.println("onListItemClick");
        ListView l = null;
        View v = null;
        int position = 0;
        long id = 0L;
        BaseballCardList instance = new BaseballCardList();
        instance.onListItemClick(l, v, position, id);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    public void testAddCardToEmptyDatabase() throws IOException {
        Activity cardDetails = this.testMenuItem(R.id.add_menu, BaseballCardDetails.class);
        BaseballCard card = this.cardInput.getNextBaseballCard();

        this.addCard(cardDetails, card);
        this.clickCardDetailsDone(cardDetails);

        List<BaseballCard> cards = new ArrayList<BaseballCard>();
        cards.add(card);
        this.assertListViewContainsItems(cards);
    }

    public void testAddCardToPopulatedDatabase() {
        Assert.fail("Implement me!");
    }

    public void testAddMultipleCards() throws IOException {
        Activity cardDetails = this.testMenuItem(R.id.add_menu, BaseballCardDetails.class);
        List<BaseballCard> cards = this.cardInput.getAllBaseballCards();

        for (BaseballCard card : cards) {
            this.addCard(cardDetails, card);
        }

        this.clickCardDetailsDone(cardDetails);
        this.assertListViewContainsItems(cards);
    }

    public void testAddCardMatchingCurrentFilter() {
        Assert.fail("Implement me!");
    }

    public void testAddCardMatchingNotCurrentFilter() {
        Assert.fail("Implement me!");
    }

    public void testAddCardAfterClearFilter() {
        Assert.fail("Implement me!");
    }

    public void testYearFilter() {
        Assert.fail("Implement me!");
    }

    public void testNumberFilter() {
        Assert.fail("Implement me!");
    }

    public void testYearAndNumberFilter() {
        Assert.fail("Implement me!");
    }

    public void testPlayerNameFilter() {
        Assert.fail("Implement me!");
    }

    private void assertListViewContainsItems(List<BaseballCard> expectedItems) {
        Assert.assertEquals(expectedItems.size(), this.list.getCount());

        for (int i = 0; i < expectedItems.size(); ++i) {
            View row = this.list.getChildAt(i);
            TextView brandTextView = (TextView) row.findViewById(R.id.brand_text_view);
            TextView yearTextView = (TextView) row.findViewById(R.id.year_text_view);
            TextView numberTextView = (TextView) row.findViewById(R.id.number_text_view);
            TextView playerNameTextView = (TextView) row.findViewById(R.id.player_name_text_view);

            BaseballCard expectedCard = expectedItems.get(i);
            Assert.assertEquals(expectedCard.getBrand(), brandTextView.getText().toString());
            Assert.assertEquals(expectedCard.getYear(), yearTextView.getText().toString());
            Assert.assertEquals(expectedCard.getNumber(), numberTextView.getText().toString());
            Assert.assertEquals(expectedCard.getPlayerName(), playerNameTextView.getText().toString());
        }
    }

    private Activity testMenuItem(int id, Class childActivityClass) {
        Instrumentation.ActivityMonitor monitor = new Instrumentation.ActivityMonitor(childActivityClass.getName(), null, false);
        this.inst.addMonitor(monitor);

        Assert.assertTrue(this.inst.invokeMenuActionSync(this.activity, id, MENU_FLAGS));

        Activity childActivity = this.inst.waitForMonitorWithTimeout(monitor, TIME_OUT);

        Assert.assertNotNull(childActivity);
        Assert.assertEquals(childActivityClass, childActivity.getClass());

        return childActivity;
    }

    // TODO Should this method be in AndroidTestUtil or some other utility class?
    private void sendKeysToCardDetails(Activity cardDetails, BaseballCard card) {
        AndroidTestUtil.sendKeysFromString(this, card.getBrand());
        this.sendKeys(KeyEvent.KEYCODE_ENTER);

        AndroidTestUtil.sendKeysFromInt(this, card.getYear());
        this.sendKeys(KeyEvent.KEYCODE_ENTER);

        AndroidTestUtil.sendKeysFromInt(this, card.getNumber());
        this.sendKeys(KeyEvent.KEYCODE_ENTER);

        int value = card.getValue();
        AndroidTestUtil.sendKeysFromInt(this, value / 100);
        this.sendKeys(KeyEvent.KEYCODE_PERIOD);
        if (value % 100 < 10) {
            this.sendKeys(KeyEvent.KEYCODE_0);
        }
        AndroidTestUtil.sendKeysFromInt(this, value % 100);
        this.sendKeys(KeyEvent.KEYCODE_ENTER);

        AndroidTestUtil.sendKeysFromInt(this, card.getCount());
        this.sendKeys(KeyEvent.KEYCODE_ENTER);

        AndroidTestUtil.sendKeysFromString(this, card.getPlayerName());
        this.sendKeys(KeyEvent.KEYCODE_ENTER);

        Spinner playerPositionSpinner = (Spinner) cardDetails.findViewById(R.id.player_position_text);
        ArrayAdapter<CharSequence> playerPositionAdapter = (ArrayAdapter<CharSequence>) playerPositionSpinner.getAdapter();
        int newPos = playerPositionAdapter.getPosition(card.getPlayerPosition());
        int oldPos = playerPositionSpinner.getSelectedItemPosition();
        int move = newPos - oldPos;
        
        this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
        if (move > 0) {
            this.sendRepeatedKeys(move, KeyEvent.KEYCODE_DPAD_DOWN);
        } else {
            this.sendRepeatedKeys(-move, KeyEvent.KEYCODE_DPAD_UP);
        }
        this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
    }

    private void addCard(Activity cardDetails, BaseballCard card) {
        this.sendKeysToCardDetails(cardDetails, card);

        final Button saveButton = (Button) cardDetails.findViewById(R.id.save_button);

        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Assert.assertTrue(saveButton.performClick());
                
                // TODO Check that Toast appears with correct message.
            }
        });
    }

    private void clickCardDetailsDone(Activity cardDetails) {
        final Button doneButton = (Button) cardDetails.findViewById(R.id.done_button);
        
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Assert.assertTrue(doneButton.performClick());
            }
        });

        this.inst.waitForIdleSync();
        Assert.assertTrue(cardDetails.isFinishing());
    }
    private Instrumentation inst = null;
    private Activity activity = null;
    private ListView list = null;
    private DatabaseUtil dbUtil = null;
    private BaseballCardCsvFileReader cardInput = null;
    private static final String DATA_ASSET = "cards.csv";
    private static final int MENU_FLAGS = 0;
    private static final int TIME_OUT = 5 * 1000; // 5 seconds
    private static final String TAG = "BaseballCardListTest";
}
