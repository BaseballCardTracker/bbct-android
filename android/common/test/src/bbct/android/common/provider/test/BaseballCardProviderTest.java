package bbct.android.common.provider.test;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.test.ProviderTestCase2;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.provider.BaseballCardContract;
import bbct.android.common.provider.BaseballCardProvider;
import bbct.android.common.provider.BaseballCardSQLHelper;
import bbct.android.common.test.DatabaseUtil;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;

/**
 * Tests for {@link BaseballCardProvider}.
 */
public class BaseballCardProviderTest extends
        ProviderTestCase2<BaseballCardProvider> {
    private static final String CREATE_TABLE = "CREATE TABLE baseball_cards"
            + "  (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "    brand      VARCHAR(10), year            INTEGER,"
            + "    number     INTEGER,     value           INTEGER,"
            + "    card_count INTEGER,     player_name     VARCHAR(50),"
            + "    team       VARCHAR(50), player_position VARCHAR(20),"
            + "  UNIQUE (brand, year, number));\n";
    private static final String INSERT_DATA = "BEGIN TRANSACTION;\n"
            + "INSERT INTO \"baseball_cards\" VALUES(1,'Topps',1991,278,10000,1,'Alex Fernandez','White Sox','Pitcher');\n"
            + "INSERT INTO \"baseball_cards\" VALUES(2,'Topps',1974,175,10000,1,'Bob Stanley','Red Sox','Pitcher');\n"
            + "INSERT INTO \"baseball_cards\" VALUES(3,'Topps',1985,201,10000,1,'Vince Coleman','Cardinals','Left Field');\n"
            + "INSERT INTO \"baseball_cards\" VALUES(4,'TMG',1993,5,10000,1,'Ken Griffey Jr.','All-Star','Center Field');\n"
            + "INSERT INTO \"baseball_cards\" VALUES(5,'Upper Deck',1993,18,10000,1,'Dave Hollins','Phillies','Third Base');\n"
            + "INSERT INTO \"baseball_cards\" VALUES(6,'Upper Deck',1990,189,10000,1,'Tom Browning','Reds','Pitcher');\n"
            + "INSERT INTO \"baseball_cards\" VALUES(7,'Topps',1982,121,10000,1,'Ed Lynch','Mets','Pitcher');\n"
            + "COMMIT;\n";
    private static final List<BaseballCard> CARDS = new ArrayList<BaseballCard>();
    static {
        CARDS.add(new BaseballCard("Topps", 1991, 278, 10000, 1,
                "Alex Fernandez", "White Sox", "Pitcher"));
        CARDS.add(new BaseballCard("Topps", 1974, 175, 10000, 1, "Bob Stanley",
                "Red Sox", "Pitcher"));
        CARDS.add(new BaseballCard("Topps", 1985, 201, 10000, 1,
                "Vince Coleman", "Cardinals", "Left Field"));
        CARDS.add(new BaseballCard("TMG", 1993, 5, 10000, 1, "Ken Griffey Jr.",
                "All-Star", "Center Field"));
        CARDS.add(new BaseballCard("Upper Deck", 1993, 18, 10000, 1,
                "Dave Hollins", "Phillies", "Third Base"));
        CARDS.add(new BaseballCard("Upper Deck", 1990, 189, 10000, 1,
                "Tom Browning", "Reds", "Pitcher"));
        CARDS.add(new BaseballCard("Topps", 1982, 121, 10000, 1, "Ed Lynch",
                "Mets", "Pitcher"));
    }

    private ContentResolver resolver = null;

    public BaseballCardProviderTest() {
        super(BaseballCardProvider.class, BaseballCardContract.AUTHORITY);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        DatabaseUtils.createDbFromSqlStatements(this.getMockContext(),
                BaseballCardSQLHelper.DATABASE_NAME,
                BaseballCardSQLHelper.SCHEMA_VERSION, CREATE_TABLE
                        + INSERT_DATA);
        this.resolver = this.getMockContentResolver();
    }

    @Override
    public void tearDown() throws Exception {
        this.getMockContext().deleteDatabase(
                BaseballCardSQLHelper.DATABASE_NAME);

        super.tearDown();
    }

    /**
     * Assert that the database contains the expected initial baseball card
     * data.
     */
    public void testPreConditions() {
        DatabaseUtil dbUtil = new DatabaseUtil(this.getMockContext());
        Assert.assertTrue(dbUtil
                .containsAllBaseballCards(BaseballCardProviderTest.CARDS));
    }

    /**
     * Test for
     * {@link BaseballCardProvider#query(android.net.Uri, String[], String, String[], String)}
     * . Query the {@link ContentProvider} without any selection arguments.
     */
    public void testQueryAll() {
        Cursor cursor = this.resolver.query(BaseballCardContract.CONTENT_URI,
                BaseballCardContract.PROJECTION, null, null, null);
        Assert.assertNotNull(cursor);

        BaseballCardSQLHelper sqlHelper = new BaseballCardSQLHelper(
                this.getMockContext());
        List<BaseballCard> actual = sqlHelper
                .getAllBaseballCardsFromCursor(cursor);
        Assert.assertTrue(BaseballCardProviderTest.CARDS.containsAll(actual));
        Assert.assertTrue(actual.containsAll(BaseballCardProviderTest.CARDS));
    }

    /**
     * Test for
     * {@link BaseballCardProvider#insert(Uri, android.content.ContentValues)}.
     */
    public void testInsert() {
        BaseballCard newCard = new BaseballCard("Code Guru Apps", 2013, 1, 25,
                1, "Code Guru", "Code Guru Devs", "Pitcher");
        Uri result = this.resolver.insert(BaseballCardContract.CONTENT_URI,
                BaseballCardContract.getContentValues(newCard));
        Assert.assertNotNull(result);
    }

    public void testUpdate() {
        Assert.fail("Stub");
    }

    public void testDelete() {
        Assert.fail("Stub");
    }

    public void testGetTypeItem() {
        String expected = BaseballCardContract.BASEBALL_CARD_ITEM_MIME_TYPE;
        String actual = this.resolver.getType(Uri.withAppendedPath(
                BaseballCardContract.CONTENT_URI, "/1"));
        Assert.assertEquals(expected, actual);
    }

    public void testGetTypeList() {
        String expected = BaseballCardContract.BASEBALL_CARD_LIST_MIME_TYPE;
        String actual = this.resolver.getType(BaseballCardContract.CONTENT_URI);
        Assert.assertEquals(expected, actual);
    }

}
