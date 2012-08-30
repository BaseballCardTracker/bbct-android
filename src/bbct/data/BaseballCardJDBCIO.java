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
import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of {@link BaseballCardIO} which uses a database table as
 * the underlying persistent storage mechanism.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardJDBCIO extends AbstractBaseballCardIO {

    /**
     * The table name to use in the underlying database.
     */
    public static final String TABLE_NAME = "baseball_cards";
    /**
     * The column name for the card brand.
     */
    public static final String BRAND_COL_NAME = "brand";
    /**
     * The column name for the card year.
     */
    public static final String YEAR_COL_NAME = "year";
    /**
     * The column name for the card number.
     */
    public static final String NUMBER_COL_NAME = "number";
    /**
     * The column name for the card value.
     */
    public static final String VALUE_COL_NAME = "value";
    /**
     * The column name for the card count.
     */
    public static final String COUNT_COL_NAME = "card_count";
    /**
     * The column name for the player's name.
     */
    public static final String NAME_COL_NAME = "player_name";
    /**
     * The column name for the player's position.
     */
    public static final String POSITION_COL_NAME = "player_position";

    /**
     * Creates a new {@link BaseballCardJDBCIO} which connects to a database at
     * the given JDBC URL. A connection to the database is opened and a new
     * table is created if it does not already exist.
     *
     * @param url The JDBC URL that gives the location of the database.
     * @throws IOException If an error occurs while opening a JDBC connection or
     * creating the table.
     */
    public BaseballCardJDBCIO(String url) throws IOException {
        try {
            this.conn = DriverManager.getConnection(url);
            this.createTable();
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    /**
     * Closes the JDBC database connection.
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        try {
            this.conn.close();
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    /**
     * Insert the data stored in the {@link BaseballCard} to the underlying
     * database.
     *
     * @param card The {@link BaseballCard} containing the data to be inserted.
     * @throws IOException If an error occurs while inserting data into the
     * database.
     */
    @Override
    public void insertBaseballCard(BaseballCard card) throws IOException {
        String sqlInsert = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmtInsert = this.conn.prepareStatement(sqlInsert)) {
            stmtInsert.setString(1, card.getBrand());
            stmtInsert.setInt(2, card.getYear());
            stmtInsert.setInt(3, card.getNumber());
            stmtInsert.setInt(4, card.getValue());
            stmtInsert.setInt(5, card.getCount());
            stmtInsert.setString(6, card.getPlayerName());
            stmtInsert.setString(7, card.getPlayerPosition());
            stmtInsert.executeUpdate();
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    /**
     * Executes a SELECT query to get all database records where the year column
     * contains the given year value.
     *
     * @param year The year of cards to search for.
     * @return A List of {@link BaseballCard}s from the given year.
     * @throws IOException If an error occurs while executing the SELECT query.
     */
    @Override
    public List<BaseballCard> getBaseballCardsByYear(int year) throws IOException {
        try {
            String sqlQuery = "SELECT * "
                    + "  FROM " + TABLE_NAME
                    + " WHERE " + YEAR_COL_NAME + " = ?";
            PreparedStatement stmt = this.conn.prepareStatement(sqlQuery);
            stmt.setInt(1, year);

            ResultSet rs = stmt.executeQuery();

            return this.getBaseballCards(rs);
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    /**
     * Executes a SELECT query to get all database records where the number
     * column contains the given number value.
     *
     * @param number The number on the cards.
     * @return A List of {@link BaseballCard}s with the given number.
     * @throws IOException If an error occurs while executing the SELECT query.
     */
    @Override
    public List<BaseballCard> getBaseballCardsByNumber(int number) throws IOException {
        try {
            String sqlQuery = "SELECT * "
                    + "  FROM " + TABLE_NAME
                    + " WHERE " + NUMBER_COL_NAME + " = ?";
            PreparedStatement stmt = this.conn.prepareStatement(sqlQuery);
            stmt.setInt(1, number);

            ResultSet rs = stmt.executeQuery();
            return this.getBaseballCards(rs);
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    /**
     * Executes a SELECT query to get all database records where the year column
     * contains the given year value and the number column contains the given
     * number value.
     *
     * @param year The year of cards to search for.
     * @param number The number on the cards.
     * @return A List of {@link BaseballCard}s from the given year with the
     * given number.
     * @throws IOException If an error occurs while executing the SELECT query.
     */
    @Override
    public List<BaseballCard> getBaseballCardsByYearAndNumber(int year, int number) throws IOException {
        try {
            String sqlQuery = "SELECT * "
                    + "  FROM " + TABLE_NAME
                    + " WHERE " + YEAR_COL_NAME + " = ?"
                    + "   AND " + NUMBER_COL_NAME + " = ?";
            PreparedStatement stmt = this.conn.prepareStatement(sqlQuery);
            stmt.setInt(1, year);
            stmt.setInt(2, number);

            ResultSet rs = stmt.executeQuery();
            return this.getBaseballCards(rs);
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    /**
     * Executes a SELECT query to get all database records where the name column
     * contains the given player's name.
     *
     * @param playerName The name of the player on the cards.
     * @return A List of {@link BaseballCard}s for the given player.
     * @throws IOException If an error occurs while executing the SELECT query.
     */
    @Override
    public List<BaseballCard> getBaseballCardsByPlayerName(String playerName) throws IOException {
        try {
            String sqlQuery = "SELECT * "
                    + "  FROM " + TABLE_NAME
                    + " WHERE " + NAME_COL_NAME + " = ?";
            PreparedStatement stmt = this.conn.prepareStatement(sqlQuery);
            stmt.setString(1, playerName);

            ResultSet rs = stmt.executeQuery();
            return this.getBaseballCards(rs);
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    /**
     * Executes an UPDATE query to update the count and value of the record
     * containing the brand, year, and number from the given {@link BaseballCard}.
     *
     * @param card The card to update.
     * @throws IOException If any I/O errors occur while writing to the
     * underlying storage mechanism.
     */
    @Override
    public void updateCard(BaseballCard card) throws IOException {
        try {
            String brand = card.getBrand();
            int year = card.getYear();
            int number = card.getNumber();
            int count = card.getCount();
            int value = card.getValue();
            String sqlQuery = "UPDATE " + TABLE_NAME
                    + "   SET " + COUNT_COL_NAME + " = ?, " + VALUE_COL_NAME + " = ?"
                    + " WHERE " + BRAND_COL_NAME + " = ?"
                    + "   AND " + YEAR_COL_NAME + " = ?"
                    + "   AND " + NUMBER_COL_NAME + " = ?";
            PreparedStatement stmt = this.conn.prepareStatement(sqlQuery);
            stmt.setInt(1, count);
            stmt.setInt(2, value);
            stmt.setString(3, brand);
            stmt.setInt(4, year);
            stmt.setInt(5, number);

            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    private List<BaseballCard> getBaseballCards(ResultSet rs) throws SQLException {
        List<BaseballCard> cards = new ArrayList<>();

        while (rs.next()) {
            String brand = rs.getString(BRAND_COL_NAME);
            int year = rs.getInt(YEAR_COL_NAME);
            int num = rs.getInt(NUMBER_COL_NAME);
            int val = rs.getInt(VALUE_COL_NAME);
            int count = rs.getInt(COUNT_COL_NAME);
            String name = rs.getString(NAME_COL_NAME);
            String pos = rs.getString(POSITION_COL_NAME);
            BaseballCard card = new BaseballCard(brand, year, num, val, count, name, pos);

            cards.add(card);
        }

        return cards;
    }

    private void createTable() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "(" + BRAND_COL_NAME + " VARCHAR(10), "
                + YEAR_COL_NAME + " INTEGER, "
                + NUMBER_COL_NAME + " INTEGER, "
                + VALUE_COL_NAME + " INTEGER, "
                + COUNT_COL_NAME + " INTEGER, "
                + NAME_COL_NAME + " VARCHAR(50), "
                + POSITION_COL_NAME + " VARCHAR(20))";

        Statement stmt = this.conn.createStatement();
        stmt.execute(sqlCreate);
    }
    private Connection conn;
}
