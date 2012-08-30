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

    /**
     * Test for {@link baseball.data.BaseballCardJDBCIO#getBaseballCardsByYear(int)}.
     */
    @Test
    public void testGetBaseballCardsByYear() throws Exception {
        System.out.println("getBaseballCardsByYear");
        int year = 0;
        List expResult = null;
        List result = instance.getBaseballCardsByYear(year);
        Assert.assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test for {@link baseball.data.BaseballCardJDBCIO#getBaseballCardsByNumber(int)}.
     */
    @Test
    public void testGetBaseballCardsByNumber() throws Exception {
        System.out.println("getBaseballCardsByNumber");
        int number = 0;
        List expResult = null;
        List result = instance.getBaseballCardsByNumber(number);
        Assert.assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test for {@link baseball.data.BaseballCardJDBCIO#getBaseballCardsByYearAndNumber(int, int)}.
     */
    @Test
    public void testGetBaseballCardsByYearAndNumber() throws Exception {
        System.out.println("getBaseballCardsByYearAndNumber");
        int year = 0;
        int number = 0;
        List expResult = null;
        List result = instance.getBaseballCardsByYearAndNumber(year, number);
        Assert.assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test for {@link baseball.data.BaseballCardJDBCIO#getBaseballCardsByPlayerName(String)}.
     */
    @Test
    public void testGetBaseballCardsByPlayerName() throws Exception {
        System.out.println("getBaseballCardsByPlayerName");
        String playerName = "";
        List expResult = null;
        List result = instance.getBaseballCardsByPlayerName(playerName);
        Assert.assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test for {@link baseball.data.BaseballCardJDBCIO#updateCard(Card)}.
     */
    @Test
    public void testUpdateCard() throws Exception {
        BaseballCard card = this.insertCard();
        int newValue = 20000;
        int newCount = 3;
        card.setValue(newValue);
        card.setCount(newCount);
        instance.updateCard(card);
        
        String brand = card.getBrand();
        int year = card.getYear();
        int number = card.getNumber();
        String query = "SELECT * "
                + "  FROM " + BaseballCardJDBCIO.TABLE_NAME
                + " WHERE " + BaseballCardJDBCIO.BRAND_COL_NAME + " = ? "
                + "   AND " + BaseballCardJDBCIO.YEAR_COL_NAME + " = ? "
                + "   AND " + BaseballCardJDBCIO.NUMBER_COL_NAME + " = ?";
        
        PreparedStatement ps  = this.conn.prepareStatement(query);
        ps.setString(1, brand);
        ps.setInt(2, year);
        ps.setInt(3, number);
        
        ResultSet rs = ps.executeQuery(query);
        
        Assert.fail("The test case is a prototype.");
    }
}
