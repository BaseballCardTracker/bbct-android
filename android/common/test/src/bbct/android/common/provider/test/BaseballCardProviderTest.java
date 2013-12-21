package bbct.android.common.provider.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import android.content.ContentResolver;
import android.database.DatabaseUtils;
import android.test.ProviderTestCase2;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.provider.BaseballCardContract;
import bbct.android.common.provider.BaseballCardProvider;
import bbct.android.common.provider.BaseballCardSQLHelper;
import bbct.android.common.test.DatabaseUtil;

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
    private static final List<BaseballCard> cards = new ArrayList<BaseballCard>();
    static {
        cards.add(new BaseballCard("Topps", 1991, 278, 10000, 1,
                "Alex Fernandez", "White Sox", "Pitcher"));
        cards.add(new BaseballCard("Topps", 1974, 175, 10000, 1, "Bob Stanley",
                "Red Sox", "Pitcher"));
        cards.add(new BaseballCard("Topps", 1985, 201, 10000, 1,
                "Vince Coleman", "Cardinals", "Left Field"));
        cards.add(new BaseballCard("TMG", 1993, 5, 10000, 1, "Ken Griffey Jr.",
                "All-Star", "Center Field"));
        cards.add(new BaseballCard("Upper Deck", 1993, 18, 10000, 1,
                "Dave Hollins", "Phillies", "Third Base"));
        cards.add(new BaseballCard("Upper Deck", 1990, 189, 10000, 1,
                "Tom Browning", "Reds", "Pitcher"));
        cards.add(new BaseballCard("Topps", 1982, 121, 10000, 1, "Ed Lynch",
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
                .containsAllBaseballCards(BaseballCardProviderTest.cards));
    }

    public void testQuery() {
        Assert.fail("Stub");
    }

    public void testInsert() {
        Assert.fail("Stub");
    }

    public void testUpdate() {
        Assert.fail("Stub");
    }

    public void testDelete() {
        Assert.fail("Stub");
    }

}
