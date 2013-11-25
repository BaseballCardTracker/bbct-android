package bbct.android.common.activity.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.ListActivity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import bbct.android.common.R;
import bbct.android.common.activity.DeleteCardList;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.test.BBCTTestUtil;
import bbct.android.common.test.BaseballCardCsvFileReader;
import bbct.android.common.test.DatabaseUtil;
import java.io.InputStream;
import java.util.List;
import junit.framework.Assert;

public class DeleteCardListTest extends ActivityInstrumentationTestCase2<DeleteCardList> {

    /**
     * Create instrumented test cases for {@link DeleteCardList}.
     */
    public DeleteCardListTest() {
        super(DeleteCardList.class);
    }

    /**
     * Set up the test fixture.
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.inst = this.getInstrumentation();
        Intent intent = new Intent(this.inst.getContext(), DeleteCardList.class);
        intent.putExtra(this.inst.getTargetContext().getString(R.string.filter_request_extra), R.id.no_filter);
        this.setActivityIntent(intent);
        this.activity = this.getActivity();

        // Create the database and populate table with test data
        InputStream cardInputStream = this.inst.getContext().getAssets().open(BBCTTestUtil.CARD_DATA);
        BaseballCardCsvFileReader cardInput = new BaseballCardCsvFileReader(cardInputStream, true);
        this.allCards = cardInput.getAllBaseballCards();
        cardInput.close();

        this.dbUtil = new DatabaseUtil(this.activity.getPackageName());
        this.dbUtil.populateTable(allCards);
    }

    /**
     * Destroy the test fixture and remove database.
     */
    @Override
    public void tearDown() throws Exception {
        this.dbUtil.deleteDatabase();

        super.tearDown();
    }

    /**
     * Make sure that everything is ok in order to
     * validate other tests.
     */
    public void testPreConditions() {
        Assert.assertNotNull(this.activity);
        Assert.assertNotNull(((ListActivity) this.activity).getListView());
    }

    /**
     * Test that the title of the {@link Activity} is correct.
     */
    public void testTitle() {
        String title = this.activity.getTitle().toString();
        String deleteTitle = this.activity.getString(R.string.delete_cards_title);

        Assert.assertTrue(title.contains(deleteTitle));
    }

    /**
     * Test that the "Delete" {@link Button} is disabled
     * when no items are selected in {@link ListView}.
     */
    public void testEmptySelection() {
        Assert.assertFalse(this.activity.findViewById(R.id.delete_button).isEnabled());
    }

    /**
     * Test that the "Delete" {@link Button} is enabled
     * once an item is selected from {@link ListView}.
     */
    public void testSelection() throws Throwable {
        final ListView lv = ((ListActivity)this.activity).getListView();
        final int index = (int) Math.random()*(lv.getChildCount()-1) + 1;
        final View row = lv.getChildAt(index);

        this.runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                Assert.assertTrue(lv.performItemClick(row, index, lv.getAdapter().getItemId(index)));
            }
        });

        Assert.assertTrue(this.activity.findViewById(R.id.delete_button).isEnabled());
    }

    /**
     * Test that upon clicking on header {@link View}, all
     * items in {@link ListView} are selected.
     */
    public void testMarkAll() throws Throwable {
        final ListView lv = ((ListActivity)this.activity).getListView();
        final View header = lv.getChildAt(0);

        this.runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                Assert.assertTrue(lv.performItemClick(header, 0, lv.getAdapter().getItemId(0)));
            }
        });

        int numMarked = 0;
        for (int i = 0; i < lv.getChildCount(); i++) {
            CheckedTextView ctv = (CheckedTextView) lv.getChildAt(i).findViewById(R.id.checkmark);

            if (ctv.isChecked())
                numMarked++;
        }

        Assert.assertEquals(lv.getChildCount(), numMarked);
    }

    private Instrumentation inst = null;
    private Activity activity = null;
    private DatabaseUtil dbUtil = null;
    private List<BaseballCard> allCards;
}
