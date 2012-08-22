package bbct.data;

import bbct.exceptions.IOException;
import java.sql.*;
import java.util.List;
import org.junit.*;

/**
 * TODO: JavaDoc
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardJDBCIONominalTest {

    private String url = "jdbc:hsqldb:mem:baseball_cards.db";
    private BaseballCardJDBCIO instance = null;
    private Connection conn = null;
    private Statement stmt = null;

    /**
     * 
     */
    public BaseballCardJDBCIONominalTest() {
    }

    /**
     * 
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    /**
     * 
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * 
     * @throws IOException
     * @throws SQLException
     */
    @Before
    public void setUp() throws IOException, SQLException {
        try {
            this.instance = new BaseballCardJDBCIO(this.url);

            // TODO: Do I need to pass in a user name and password for HSQLDB?
            this.conn = DriverManager.getConnection(this.url);
            this.stmt = this.conn.createStatement();
        } catch (IOException ex) {
            System.out.println("IOException caught in setUp()");
            ex.printStackTrace();
        }
    }

    /**
     * 
     * @throws IOException
     * @throws SQLException
     */
    @After
    public void tearDown() throws IOException, SQLException {
        // Drop the table. The tests in this class are for a single insert only.
        String drop = "DROP TABLE " + BaseballCardJDBCIO.TABLE_NAME;
        this.stmt.execute(drop);

        this.instance.close();
    }

    /**
     * Test for {@link baseball.data.BaseballCardJDBCIO#BaseballCardJDBCIO}.
     * @throws Exception 
     */
    @Test
    public void testConstructor() throws Exception {
        // Constructor is called from setUp(). This method simply asserts
        // that the table was created.

        // FIXME
        String query = "SELECT count(*)"
                + "  FROM information_schema.system_tables"
                + " WHERE table_name = '" + BaseballCardJDBCIO.TABLE_NAME + "'";
        ResultSet rs = this.stmt.executeQuery(query);
        Assert.assertTrue(rs.next());
        Assert.assertEquals(1, rs.getInt(1));
        Assert.assertFalse(rs.next());
    }

    /**
     * Test for {@link baseball.data.BaseballCardJDBCIO#close()}.
     * @throws Exception 
     */
    @Test
    public void testClose() throws Exception {
        instance.close();
    }

    /**
     * Test for {@link baseball.data.BaseballCardJDBCIO#insertBaseballCard(baseball.data.BaseballCard)}.
     * @throws Exception 
     */
    @Test
    public void testInsertBaseballCard() throws Exception {
        BaseballCard card = this.insertCard();

        String select = "SELECT * FROM " + BaseballCardJDBCIO.TABLE_NAME;
        ResultSet rs = this.stmt.executeQuery(select);

        Assert.assertTrue(rs.next());
        Assert.assertEquals(card.getBrand(), rs.getString("brand"));
        Assert.assertEquals(card.getYear(), rs.getInt("year"));
        Assert.assertEquals(card.getNumber(), rs.getInt("number"));
        Assert.assertEquals(card.getValue(), rs.getInt("value"));
        Assert.assertEquals(card.getCount(), rs.getInt("card_count"));
        Assert.assertEquals(card.getPlayerName(), rs.getString("player_name"));
        Assert.assertEquals(card.getPlayerPosition(), rs.getString("player_position"));
        Assert.assertFalse(rs.next());
    }

    /**
     * Test for {@link baseball.data.BaseballCardJDBCIO#getBaseballCardByYear(int)}.
     * @throws Exception 
     */
    @Test
    public void testGetBaseballCardByYear() throws Exception {
        BaseballCard card = this.insertCard();
        List result = this.instance.getBaseballCardsByYear(card.getYear());
        Assert.assertEquals(card, result.get(0));
    }

    /**
     * Test for {@link baseball.data.BaseballCardJDBCIO#getBaseballCardByNumber(int)}.
     * @throws Exception 
     */
    @Test
    public void testGetBaseballCardByNumber() throws Exception {
        BaseballCard card = this.insertCard();
        List result = this.instance.getBaseballCardsByNumber(card.getNumber());
        Assert.assertEquals(card, result.get(0));
    }

    /**
     * Test for {@link baseball.data.BaseballCardJDBCIO#getBaseballCardsByYearAndNumber(int, int)}.
     * @throws Exception 
     */
    @Test
    public void testGetBaseballCardByYearAndNumber() throws Exception {
        BaseballCard card = this.insertCard();
        List result = this.instance.getBaseballCardsByYearAndNumber(card.getYear(), card.getNumber());
        Assert.assertEquals(card, result.get(0));
    }

    /**
     * Test for {@link baseball.data.BaseballCardJDBCIO#getBaseballCardsByPlayerName(String)}.
     * @throws Exception 
     */
    @Test
    public void testGetBaseballCardByPlayerName() throws Exception {
        BaseballCard card = this.insertCard();
        List result = this.instance.getBaseballCardsByPlayerName(card.getPlayerName());
        Assert.assertEquals(card, result.get(0));
    }

    private BaseballCard insertCard() throws IOException {
        String brand = "Topps";
        int year = 1991;
        int num = 278;
        int val = 10000;
        int count = 1;
        String name = "Alex Fernandez";
        String pos = "Pitcher";

        BaseballCard card = new BaseballCard(brand, year, num, val, count, name, pos);
        this.instance.insertBaseballCard(card);

        return card;
    }
}
