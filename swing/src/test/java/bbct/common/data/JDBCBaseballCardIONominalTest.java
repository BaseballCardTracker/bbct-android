/*
 * This file is part of BBCT.
 *
 * Copyright 2012-14 codeguru <codeguru@users.sourceforge.net>
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Nominal tests for {@link JDBCBaseballCardIO}.
 *
 * TODO: JavaDoc
 *
 * TODO: testGetBaseballCardByX() methods should be extended to find multiple
 * cards
 */
public class JDBCBaseballCardIONominalTest {
    private String url = "jdbc:hsqldb:mem:baseball_cards.db";
    private JDBCBaseballCardIO instance = null;
    private Connection conn = null;
    private Statement stmt = null;
    private BaseballCard card = null;

    /**
     * Set up the fixture for this test case.
     *
     * @throws BBCTIOException
     *             If an error occurs while creating a
     *             {@link JDBCBaseballCardIO} object to test.
     * @throws SQLException
     *             If an error occurs while connecting to the database.
     */
    @Before
    public void setUp() throws BBCTIOException, SQLException {
        this.instance = new JDBCBaseballCardIO(this.url);

        // TODO: Do I need to pass in a user name and password for HSQLDB?
        this.conn = DriverManager.getConnection(this.url);
        this.stmt = this.conn.createStatement();

        this.card = createBaseballCard();
    }

    /**
     * Return the system to its initial state by dropping the database table
     * created by this test case.
     *
     * @throws BBCTIOException
     *             If an error occurs while closing the
     *             {@link JDBCBaseballCardIO} object being tested.
     * @throws SQLException
     *             If an error occurs while executing the DROP query.
     */
    @After
    public void tearDown() throws BBCTIOException, SQLException {
        // Drop the table. The tests in this class are for a single insert only.
        String drop = "DROP TABLE " + JDBCBaseballCardIO.TABLE_NAME;
        this.stmt.execute(drop);

        this.instance.close();
    }

    /**
     * Test for {@link JDBCBaseballCardIO#JDBCBaseballCardIO}. Asserts that the
     * database table was created.
     *
     * @throws SQLException
     *             If an error occurs while executing the query to check that
     *             the database table was created.
     */
    @Test
    public void testConstructor() throws SQLException {
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
     * @throws BBCTIOException
     */
    @Test
    public void testClose() throws BBCTIOException {
        instance.close();
    }

    /**
     * Test for {@link JDBCBaseballCardIO#insertBaseballCard(BaseballCard)}.
     *
     * @throws BBCTIOException
     *             If an error occurs when calling
     *             {@link JDBCBaseballCardIO#insertBaseballCard(BaseballCard)}.
     * @throws SQLException
     *             If an error occurs while checking that the card was inserted
     *             in to the database table.
     */
    @Test
    public void testInsertBaseballCard() throws BBCTIOException, SQLException {
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
        Assert.assertEquals(card.getPlayerPosition(),
                rs.getString("player_position"));
        Assert.assertFalse(rs.next());
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
     * @throws BBCTIOException
     *             If an error occurs during this test.
     */
    @Test
    public void testGetBaseballCardsByYear() throws BBCTIOException {
        this.instance.insertBaseballCard(this.card);

        List<BaseballCard> expResult = new ArrayList<>();
        expResult.add(this.card);

        List<BaseballCard> result = instance.getBaseballCardsByYear(this.card
                .getYear());
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test for {@link JDBCBaseballCardIO#getBaseballCardsByNumber(int)}.
     *
     * @throws BBCTIOException
     *             If an error occurs during this test.
     */
    @Test
    public void testGetBaseballCardsByNumber() throws BBCTIOException {
        this.instance.insertBaseballCard(this.card);

        List<BaseballCard> expResult = new ArrayList<>();
        expResult.add(this.card);

        List<BaseballCard> result = instance.getBaseballCardsByNumber(this.card
                .getNumber());
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test for
     * {@link JDBCBaseballCardIO#getBaseballCardsByYearAndNumber(int, int)}.
     *
     * @throws BBCTIOException
     *             If an error occurs during this test.
     */
    @Test
    public void testGetBaseballCardsByYearAndNumber() throws BBCTIOException {
        this.instance.insertBaseballCard(this.card);

        List<BaseballCard> expResult = new ArrayList<>();
        expResult.add(this.card);

        List<BaseballCard> result = instance.getBaseballCardsByYearAndNumber(
                this.card.getYear(), this.card.getNumber());
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test for {@link JDBCBaseballCardIO#getBaseballCardsByPlayerName(String)}.
     *
     * @throws BBCTIOException
     *             If an error occurs during this test.
     */
    @Test
    public void testGetBaseballCardsByPlayerName() throws BBCTIOException {
        this.instance.insertBaseballCard(this.card);

        List<BaseballCard> expResult = new ArrayList<>();
        expResult.add(this.card);

        List<BaseballCard> result = instance
                .getBaseballCardsByPlayerName(this.card.getPlayerName());
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test for {@link JDBCBaseballCardIO#updateBaseballCard(BaseballCard)}.
     *
     * @throws BBCTIOException
     *             If an error occurs during this test.
     * @throws SQLException
     *             If an error occurs while connecting to the database.
     */
    @Test
    public void testUpdateCard() throws BBCTIOException, SQLException {
        this.instance.insertBaseballCard(this.card);
        int newValue = 20000;
        int newCount = 3;
        card.setValue(newValue);
        card.setCount(newCount);
        instance.updateBaseballCard(card);

        String brand = card.getBrand();
        int year = card.getYear();
        int number = card.getNumber();
        String query = "SELECT * " + "  FROM " + JDBCBaseballCardIO.TABLE_NAME
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

    /**
     * Test for
     * {@link JDBCBaseballCardIO#removeupdateBaseballCard(BaseballCard)}. Actual
     * result is a boolean indicating whether the current pointer in ResultSet
     * is positioned after the last row (false value if it is). Expected result
     * is therefore a boolean value of false, which indicates that no rows were
     * returned.
     *
     * @throws BBCTIOException
     *             If an error occurs during this test.
     * @throws SQLException
     *             If an error occurs while connecting to the database.
     */
    @Test
    public void testRemoveCard() throws BBCTIOException, SQLException {
        this.instance.insertBaseballCard(this.card);

        String playerName = card.getPlayerName();
        int year = card.getYear();
        int number = card.getNumber();
        String query = "SELECT * " + "  FROM " + JDBCBaseballCardIO.TABLE_NAME
                + " WHERE " + JDBCBaseballCardIO.NAME_COL_NAME + " = ? "
                + "   AND " + JDBCBaseballCardIO.YEAR_COL_NAME + " = ? "
                + "   AND " + JDBCBaseballCardIO.NUMBER_COL_NAME + " = ?";

        PreparedStatement ps = this.conn.prepareStatement(query);
        ps.setString(1, playerName);
        ps.setInt(2, year);
        ps.setInt(3, number);

        this.instance.removeBaseballCard(this.card);
        ResultSet rs = ps.executeQuery();
        boolean result = rs.next();

        Assert.assertEquals(false, result);
    }
}
