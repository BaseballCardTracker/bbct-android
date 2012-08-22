package bbct.gui;

import bbct.data.BaseballCard;
import bbct.exceptions.IOException;
import java.util.List;
import javax.swing.JPanel;

/**
 * TODO: JavaDoc
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public abstract class FindCardsByPanel extends JPanel {

    /**
     * 
     * @return
     * @throws IOException
     */
    protected abstract List<BaseballCard> getBaseballCards() throws IOException;
}
