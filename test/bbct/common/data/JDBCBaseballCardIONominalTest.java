/*
 * This file is part of BBCT.
 *
 * Copyright 2012 codeguru <codeguru@users.sourceforge.net>
 *
 * BBCT is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BBCT is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bbct.common.data;

import bbct.common.exceptions.BBCTIOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.*;

/**
 * TODO: JavaDoc
 *
 * TODO: testGetBaseballCardByX() methods should be extended to find multiple
 * cards
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class JDBCBaseballCardIONominalTest {

    private String url = "jdbc:hsqldb:mem:baseball_cards.db";
    private JDBCBaseballCardIO instance = null;
    private Connection conn = null;
    private Statement stmt = null;
    private BaseballCard card = null;

    /**
     *
     */
    public JDBCBaseballCardIONominalTest() {
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
     * @throws BBCTIOException 
     * @throws SQLException
     */
    @Before
    public void setUp() throws BBCTIOException, SQLException {
        try {
            this.instance = new JDBCBaseballCardIO(this.url);

            // TODO: Do I need to pass in a user name and password for HSQLDB?
            this.conn = DriverManager.getConnection(this.url);
            this.stmt = this.conn.createStatement();

            this.card = createBaseballCard();
        } catch (BBCTIOException ex) {
            System.out.println("IOException caught in setUp()");
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     *
     * @throws BBCTIOException 
     * @throws SQLException
     */
    @After
    public void tearDown() throws BBCTIOException, SQLException {
        // Drop the table. The tests in this class are for a single insert only.
        String drop = "DROP TABLE " + JDBCBaseballCardIO.TABLE_NAME;
        this.stmt.execute(drop);

        this.instance.close();
    }

    /**
     * Test for {@link JDBCBaseballCardIO#JDBCBaseballCardIO}.
     *
     * @throws Exception
     */
        public void testConstructor() throws Exception {
        // Constructor is called from setUp(). This method simply asserts
        // that the table was created.

        // FIXME
        String query = "SELECT count(*)"
                + "  FROM information_schema.system_tables"
                + " WHERE table_name = '" + JDBCBaseballCardIO.TABLE_NAME + "'";
        ResultSet rs = this.stmt.executeQuery(query);
        Assert.assertTrue(rs.next());
        Assert.assertEquals(1, rs.getInt(1));
        Assert.assertFalse(rs.next());
    }

    /**
     * Test for {@link JDBCBaseballCardIO#close()}.
     *
     * @throws Exception
     */
        public void testClose() throws Exception {
        instance.close();
    }

    /**
     * Test for {@link JDBCBaseballCardIO#insertBaseballCard(BaseballCard)}.
     *
     * @throws Exception
     */
        public void testInsertBaseballCard() throws Exception {
        this.instance.insertBaseballCard(this.card);

        String select = "SELECT * FROM " + JDBCBaseballCardIO.TABLE_NAME;
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
     * Test for {@link JDBCBaseballCardIO#getBaseballCardByYear(int)}.
     *
     * @throws Exception
     */
        public void testGetBaseballCardByYear() throws Exception {
        this.instance.insertBaseballCard(this.card);
        List result = this.instance.getBaseballCardsByYear(card.getYear());
        Assert.assertEquals(card, result.get(0));
    }

    /**
     * Test for {@link JDBCBaseballCardIO#getBaseballCardByNumber(int)}.
     *
     * @throws Exception
     */
        public void testGetBaseballCardByNumber() throws Exception {
        this.instance.insertBaseballCard(this.card);
        List result = this.instance.getBaseballCardsByNumber(card.getNumber());
        Assert.assertEquals(card, result.get(0));
    }

    /**
     * Test for
     * {@link JDBCBaseballCardIO#getBaseballCardsByYearAndNumber(int, int)}.
     *
     * @throws Exception
     */
        public void testGetBaseballCardByYearAndNumber() throws Exception {
        this.instance.insertBaseballCard(this.card);
        List result = this.instance.getBaseballCardsByYearAndNumber(card.getYear(), card.getNumber());
        Assert.assertEquals(card, result.get(0));
    }

    /**
     * Test for {@link JDBCBaseballCardIO#getBaseballCardsByPlayerName(String)}.
     *
     * @throws Exception
     */
        public void testGetBaseballCardByPlayerName() throws Exception {
        this.instance.insertBaseballCard(this.card);
        List result = this.instance.getBaseballCardsByPlayerName(card.getPlayerName());
        Assert.assertEquals(card, result.get(0));
    }

    private BaseballCard createBaseballCard() {
        String brand = "Topps";
        int year = 1991;
        int num = 278;
        int val = 10000;
        int count = 1;
        String name = "Alex Fernandez";
        String pos = "Pitcher";

        return new BaseballCard(brand, year, num, val, count, name, pos);
    }

    /**
     * Test for {@link JDBCBaseballCardIO#getBaseballCardsByYear(int)}.
     *
     * @throws Exception
     */
        public void testGetBaseballCardsByYear() throws Exception {
        this.instance.insertBaseballCard(this.card);

        List<BaseballCard> expResult = new ArrayList<BaseballCard>();
        expResult.add(this.card);

        List result = instance.getBaseballCardsByYear(this.card.getYear());
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test for {@link JDBCBaseballCardIO#getBaseballCardsByNumber(int)}.
     *
     * @throws Exception
     */
        public void testGetBaseballCardsByNumber() throws Exception {
        this.instance.insertBaseballCard(this.card);

        List<BaseballCard> expResult = new ArrayList<BaseballCard>();
        expResult.add(this.card);

        List result = instance.getBaseballCardsByNumber(this.card.getNumber());
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test for
     * {@link JDBCBaseballCardIO#getBaseballCardsByYearAndNumber(int, int)}.
     *
     * @throws Exception
     */
        public void testGetBaseballCardsByYearAndNumber() throws Exception {
        this.instance.insertBaseballCard(this.card);

        List<BaseballCard> expResult = new ArrayList<BaseballCard>();
        expResult.add(this.card);

        List result = instance.getBaseballCardsByYearAndNumber(this.card.getYear(), this.card.getNumber());
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test for {@link JDBCBaseballCardIO#getBaseballCardsByPlayerName(String)}.
     *
     * @throws Exception
     */
        public void testGetBaseballCardsByPlayerName() throws Exception {
        this.instance.insertBaseballCard(this.card);

        List<BaseballCard> expResult = new ArrayList<BaseballCard>();
        expResult.add(this.card);

        List result = instance.getBaseballCardsByPlayerName(this.card.getPlayerName());
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test for {@link JDBCBaseballCardIO#updateBaseballCard(BaseballCard)}.
     *
     * @throws Exception
     */
        public void testUpdateCard() throws Exception {
        this.instance.insertBaseballCard(this.card);
        int newValue = 20000;
        int newCount = 3;
        card.setValue(newValue);
        card.setCount(newCount);
        instance.updateBaseballCard(card);

        String brand = card.getBrand();
        int year = card.getYear();
        int number = card.getNumber();
        String query = "SELECT * "
                + "  FROM " + JDBCBaseballCardIO.TABLE_NAME
                + " WHERE " + JDBCBaseballCardIO.BRAND_COL_NAME + " = ? "
                + "   AND " + JDBCBaseballCardIO.YEAR_COL_NAME + " = ? "
                + "   AND " + JDBCBaseballCardIO.NUMBER_COL_NAME + " = ?";

        PreparedStatement ps = this.conn.prepareStatement(query);
        ps.setString(1, brand);
        ps.setInt(2, year);
        ps.setInt(3, number);

        ResultSet rs = ps.executeQuery(query);

        Assert.fail("The test case is a prototype.");
    }
}
