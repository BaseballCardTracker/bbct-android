package bbct.data;

import bbct.exceptions.IOException;
import java.util.List;

/**
 * TODO: JavaDoc
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public interface BaseballCardIO {
    
    public void close() throws IOException;
    
    public void insertBaseballCard(BaseballCard card) throws IOException;
    
    public List<BaseballCard> getBaseballCardByYear(int year) throws IOException;
    
    public List<BaseballCard> getBaseballCardByNumber(int number) throws IOException;
    
    public List<BaseballCard> getBaseballCardByYearAndNumber(int year, int number) throws IOException;

    public List<BaseballCard> getBaseballCardByPlayerName(String playerName) throws IOException;
}
