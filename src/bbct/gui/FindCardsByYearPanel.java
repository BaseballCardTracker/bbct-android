package bbct.gui;

import bbct.data.BaseballCard;
import bbct.data.BaseballCardIO;
import bbct.exceptions.IOException;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

/**
 * TODO: yearTextFiled needs to be limited to 4 digits
 *
 * TODO: JavaDoc
 *
 * TODO: Tweak component placement and size
 *
 * TODO: Update instructions as user interacts with interface
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class FindCardsByYearPanel extends FindCardsByPanel {

    /**
     * Creates new {@link FindCardsByYearPanel}.
     *
     * @param bcio
     */
    public FindCardsByYearPanel(BaseballCardIO bcio) {
        this.bcio = bcio;
        this.initComponents();
    }

    /**
     * 
     * @return
     * @throws IOException
     */
    @Override
    protected List<BaseballCard> getBaseballCards() throws IOException {
        int year = Integer.parseInt(this.yearTextField.getText());
        
        return this.bcio.getBaseballCardsByYear(year);
    }

    private void initComponents() {
        this.setLayout(new FlowLayout());

        JLabel yearLabel = new JLabel("Card Year:");
        yearLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        this.add(yearLabel);

        this.yearTextField = new javax.swing.JFormattedTextField();
        this.yearTextField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
        this.yearTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        this.yearTextField.setColumns(10);
        this.add(this.yearTextField);
    }
    
    private JFormattedTextField yearTextField;
    private BaseballCardIO bcio = null;
    
    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("FindCardsByYearPanel Test");
        frame.add(new FindCardsByYearPanel(null));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
