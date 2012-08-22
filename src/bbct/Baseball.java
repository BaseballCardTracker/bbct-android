package bbct;

import bbct.data.BaseballCardIO;
import bbct.data.BaseballCardJDBCIO;
import bbct.exceptions.IOException;
import bbct.gui.BBCTFrame;
import bbct.gui.GUIResources;
import javax.swing.JOptionPane;

/**
 * TODO: Licensing info in every file
 * 
 * This is the driver class for the Baseball Card Tracker program.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class Baseball {

    /**
     * Starts the Baseball Card Tracker by creating and showing the initial window.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            BaseballCardIO bcio = new BaseballCardJDBCIO(GUIResources.DB_URL);
            
            new BBCTFrame(bcio).setVisible(true);
        } catch (IOException ex) {
             JOptionPane.showMessageDialog(null, ex.getMessage(), "Initialization Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
