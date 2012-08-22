package bbct.data;

import bbct.exceptions.IOException;
import java.util.List;

/**
 * TODO: JavaDoc
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public interface BaseballCardIO {
    
    /**
     * 
     * @throws IOException
     */
    public void close() throws IOException;
    
    /**
     * 
     * @param card
     * @throws IOException
     */
    public void insertBaseballCard(BaseballCard card) throws IOException;
    
    /**
     * 
     * @param year
     * @return
     * @throws IOException
     */
    public List<BaseballCard> getBaseballCardsByYear(int year) throws IOException;
    
    /**
     * 
     * @param number
     * @return
     * @throws IOException
     */
    public List<BaseballCard> getBaseballCardsByNumber(int number) throws IOException;
    
    /**
     * 
     * @param year
     * @param number
     * @return
     * @throws IOException
     */
    public List<BaseballCard> getBaseballCardsByYearAndNumber(int year, int number) throws IOException;

    /**
     * 
     * @param playerName
     * @return
     * @throws IOException
     */
    public List<BaseballCard> getBaseballCardsByPlayerName(String playerName) throws IOException;
}
