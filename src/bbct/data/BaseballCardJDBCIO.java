package bbct.data;

import bbct.exceptions.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: JavaDoc
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardJDBCIO implements BaseballCardIO {
    
    /**
     * 
     */
    public static final String TABLE_NAME = "baseball_cards";
    /**
     * 
     */
    public static final String BRAND_COL_NAME = "brand";
    /**
     * 
     */
    public static final String YEAR_COL_NAME = "year";
    /**
     * 
     */
    public static final String NUMBER_COL_NAME = "number";
    /**
     * 
     */
    public static final String VALUE_COL_NAME = "value";
    /**
     * 
     */
    public static final String COUNT_COL_NAME = "card_count";
    /**
     * 
     */
    public static final String NAME_COL_NAME = "player_name";
    /**
     * 
     */
    public static final String POSITION_COL_NAME = "player_position";
    
    /**
     * 
     * @param url
     * @throws IOException
     */
    public BaseballCardJDBCIO(String url) throws IOException {
        try {
            this.conn = DriverManager.getConnection(url);
            
//            if (!this.tableExists()) {
                this.createTable();
//            }
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }
    
    /**
     * 
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        try {
            conn.close();
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }
    
    /**
     * 
     * @param card
     * @throws IOException
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
            stmtInsert.execute();
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }
    
    /**
     * 
     * @param year
     * @return
     * @throws IOException
     */
    @Override
    public List<BaseballCard> getBaseballCardsByYear(int year) throws IOException {
        try {
            String sqlQuery = "SELECT * " +
                              "  FROM " + TABLE_NAME +
                              " WHERE " + YEAR_COL_NAME + " = ?";
            PreparedStatement stmt = conn.prepareStatement(sqlQuery);
            stmt.setInt(1, year);
            
            ResultSet rs = stmt.executeQuery();
            
            return this.getBaseballCards(rs);
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    /**
     * 
     * @param number
     * @return
     * @throws IOException
     */
    @Override
    public List<BaseballCard> getBaseballCardsByNumber(int number) throws IOException {
        try {
            String sqlQuery = "SELECT * " +
                              "  FROM " + TABLE_NAME +
                              " WHERE " + NUMBER_COL_NAME + " = ?";
            PreparedStatement stmt = conn.prepareStatement(sqlQuery);
            stmt.setInt(1, number);
            
            ResultSet rs = stmt.executeQuery();
            return this.getBaseballCards(rs);
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    /**
     * 
     * @param year
     * @param number
     * @return
     * @throws IOException
     */
    @Override
    public List<BaseballCard> getBaseballCardsByYearAndNumber(int year, int number) throws IOException {
        try {
            String sqlQuery = "SELECT * " +
                              "  FROM " + TABLE_NAME +
                              " WHERE " + YEAR_COL_NAME + " = ?" +
                              "   AND " + NUMBER_COL_NAME + " = ?";
            PreparedStatement stmt = conn.prepareStatement(sqlQuery);
            stmt.setInt(1, year);
            stmt.setInt(2, number);
            
            ResultSet rs = stmt.executeQuery();
            return this.getBaseballCards(rs);
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    /**
     * 
     * @param playerName
     * @return
     * @throws IOException
     */
    @Override
    public List<BaseballCard> getBaseballCardsByPlayerName(String playerName) throws IOException {
        try {
            String sqlQuery = "SELECT * " +
                              "  FROM " + TABLE_NAME +
                              " WHERE " + NAME_COL_NAME + " = ?";
            PreparedStatement stmt = conn.prepareStatement(sqlQuery);
            stmt.setString(1, playerName);
            
            ResultSet rs = stmt.executeQuery();
            return this.getBaseballCards(rs);
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
        
        Statement stmt = conn.createStatement();
        stmt.execute(sqlCreate);
    }
    
    private Connection conn;

}
