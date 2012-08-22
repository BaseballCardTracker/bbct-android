package bbct.gui;

import bbct.data.BaseballCard;
import bbct.data.BaseballCardIO;
import bbct.exceptions.IOException;
import java.awt.FlowLayout;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

/**
 * TODO: JavaDoc
 *
 * TODO: Tweak component placement
 *
 * TODO: yearTextFiled needs to be limited to 4 digits
 *
 * TODO: Update instructions as user interacts with interface
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class FindCardsByYearAndNumberPanel extends FindCardsByPanel {

    /**
     * Creates new {@link FindCardsByYearAndNumberPanel}.
     * @param bcio 
     */
    public FindCardsByYearAndNumberPanel(BaseballCardIO bcio) {
        this.bcio = bcio;
        initComponents();
    }


    @Override
    protected List<BaseballCard> getBaseballCards() throws IOException {
        int year = Integer.parseInt(this.yearTextField.getText());
        int number = Integer.parseInt(this.numberTextField.getText());
        
        return this.bcio.getBaseballCardsByYearAndNumber(year, number);
    }

    private void initComponents() {
        // TODO: GridLayout?
        this.setLayout(new FlowLayout());
        
        JLabel cardYearLabel = new JLabel("Card Year:");
        cardYearLabel.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        this.add(cardYearLabel);

        this.yearTextField = new JFormattedTextField();
        this.yearTextField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
        this.yearTextField.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        this.yearTextField.setColumns(10);
        this.add(this.yearTextField);
        
        JLabel cardNumberLabel = new JLabel("Card Number:");
        cardNumberLabel.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        this.add(cardNumberLabel);
        
        this.numberTextField = new JFormattedTextField();
        this.numberTextField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
        this.numberTextField.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        this.numberTextField.setColumns(10);
        this.add(this.numberTextField);
    }

    private JFormattedTextField numberTextField;
    private JFormattedTextField yearTextField;
    private BaseballCardIO bcio = null;
    
    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("FindCardsByNumberPanel Test");
        frame.add(new FindCardsByYearAndNumberPanel(null));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
