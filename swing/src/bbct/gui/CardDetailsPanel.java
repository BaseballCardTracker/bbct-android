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
package bbct.gui;

import bbct.data.BaseballCard;
import bbct.exceptions.InputException;
import bbct.gui.event.UpdateInstructionsFocusListener;
import bbct.gui.inputverifiers.CurrencyInputVerifier;
import bbct.gui.inputverifiers.NotEmptyInputVerifier;
import bbct.gui.inputverifiers.PositiveIntegerInputVerifier;
import bbct.gui.inputverifiers.YearInputVerifier;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * {@link CardDetailsPanel} contains labels and text fields for the data stored
 * in a {@link bbct.data.BaseballCard} model object. This panel can be used in
 * two modes. The first mode, which is set by using the default constructor
 * {@link #CardDetailsPanel()} or {@link #CardDetailsPanel(boolean)} with a
 * value of
 * <code>true</code>, allows editing of all of the text fields. The second mode,
 * set with {@link #CardDetailsPanel(boolean)} or
 * {@link #CardDetailsPanel(bbct.data.BaseballCard, boolean)} with a value of
 * <code>false</code> for the
 * <code>boolean </code> parameter, only allows editing of the value and count
 * text fields.
 *
 * TODO: Instructions should change depending on value of allEditable field.
 *
 * TODO: Add $ at the beginning of valueTextField's text if it is not present?
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class CardDetailsPanel extends JPanel {

    /**
     * Creates a new {@link CardDetailsPanel}.
     *
     * @param allEditable Whether or not all text fields are editable. The count
     * and value text fields will always be editable regardless of the value of
     * this flag.
     */
    public CardDetailsPanel(boolean allEditable) {
        this.allEditable = allEditable;
        this.initComponents();
    }

    /**
     * Creates a new {@link CardDetailsPanel}.
     *
     * @param card The {@link bbct.data.BaseballCard} object used to initialize
     * the values of the text fields in this panel.
     * @param allEditable Whether or not all text fields are editable. The count
     * and value text fields will always be editable regardless of the value of
     * this flag.
     */
    public CardDetailsPanel(BaseballCard card, boolean allEditable) {
        this.allEditable = allEditable;

        this.initComponents();

        this.brandTextField.setText(card.getBrand());
        this.yearTextField.setValue(card.getYear());
        this.numberTextField.setValue(card.getNumber());

        // TODO: This works, but the logic should be part of the JFormattedTextField, not the panel
        int value = card.getValue();
        int dollars = value / 100;
        int cents = value % 100;
        String centsStr = cents < 10 ? "0" + cents : "" + cents;
        String valueStr = "$" + dollars + "." + centsStr;

        this.valueTextField.setText(valueStr);
        this.countTextField.setValue(card.getCount());
        this.playerNameTextField.setText(card.getPlayerName());
        this.playerPositionTextField.setText(card.getPlayerPosition());
    }

    /**
     * Creates a {@link bbct.data.BaseballCard} from the data in the text fields
     * in this panel.
     *
     * @return A {@link bbct.data.BaseballCard} initialized with data from the
     * text fields in this panel.
     * @throws InputException If any text field is blank or contains text with
     * invalid formatting.
     */
    public BaseballCard getBaseballCard() throws InputException {
        // TODO: Thoroughly test all error handling code.

        // Validate card brand
        this.brandTextField.selectAll();
        this.brandTextField.requestFocusInWindow();
        if (!this.notEmptyVerifier.verify(this.brandTextField)) {
            throw new InputException("Please enter a card brand.");
        }
        String brand = this.brandTextField.getText();

        // Validate card year
        this.yearTextField.selectAll();
        this.yearTextField.requestFocusInWindow();
        try {
            this.yearTextField.commitEdit();
        } catch (ParseException ex) {
            throw new InputException("Please enter a valid four-digit year.", ex);
        }
        if (!this.yearVerifier.verify(this.yearTextField)) {
            throw new InputException("Please enter a valid four-digit year.");
        }
        int year = Integer.parseInt(this.yearTextField.getText());

        // Validate card number
        this.numberTextField.selectAll();
        this.numberTextField.requestFocusInWindow();
        try {
            this.numberTextField.commitEdit();
        } catch (ParseException ex) {
            throw new InputException("Please enter a valid card number. (The number must be positive).", ex);
        }
        if (!numVerifier.verify(this.numberTextField)) {
            throw new InputException("Please enter a valid card number. (The number must be positive).");
        }
        int number = Integer.parseInt(this.numberTextField.getText());

        // Validate card value
        this.valueTextField.selectAll();
        this.valueTextField.requestFocusInWindow();
        try {
            this.valueTextField.commitEdit();
        } catch (ParseException ex) {
            throw new InputException("Please enter a valid dollar amount.", ex);
        }
        if (!this.currencyVerifier.verify(this.valueTextField)) {
            throw new InputException("Please enter a valid dollar amount.");
        }
        double valueDbl = ((Number) this.valueTextField.getValue()).doubleValue();
        int value = (int) (valueDbl * 100);

        // Validate card count
        this.countTextField.selectAll();
        this.countTextField.requestFocusInWindow();
        if (!this.notEmptyVerifier.verify(this.countTextField)) {
            throw new InputException("Please enter a card count.");
        }
        if (!this.countTextField.isEditValid()) {
            throw new InputException("Please enter a numeric value for the card count.");
        }
        int count = Integer.parseInt(this.countTextField.getText());
        if (count < 0) {
            throw new InputException("Please enter a positive value for the card count");
        }

        // Validate player name
        this.playerNameTextField.selectAll();
        this.playerNameTextField.requestFocusInWindow();
        if (!this.notEmptyVerifier.verify(this.playerNameTextField)) {
            throw new InputException("Please enter a player name.");
        }
        String playerName = this.playerNameTextField.getText();

        // Validate player position
        this.playerPositionTextField.selectAll();
        this.playerPositionTextField.requestFocusInWindow();
        if (!this.notEmptyVerifier.verify(this.playerPositionTextField)) {
            throw new InputException("Please enter a player position.");
        }
        String playerPosition = this.playerPositionTextField.getText();

        return new BaseballCard(brand, year, number, value, count, playerName, playerPosition);
    }

    /**
     * Reset all text fields to blank strings and set focus to the card brand
     * text field.
     */
    public void reset() {
        this.brandTextField.setText("");
        this.countTextField.setText("");
        this.numberTextField.setText("");
        this.valueTextField.setText("");
        this.yearTextField.setText("");
        this.playerNameTextField.setText("");
        this.playerPositionTextField.setText("");
        this.brandTextField.requestFocusInWindow();
    }

    private void initComponents() {
        JPanel cardDetailsPanel = new JPanel();
        cardDetailsPanel.setBorder(BorderFactory.createTitledBorder(null, "Card Details", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Tahoma", 0, 18))); // NOI18N
        cardDetailsPanel.setLayout(new GridLayout(5, 2));

        JLabel cardBrandLabel = new JLabel("Card Brand:");
        cardBrandLabel.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        cardDetailsPanel.add(cardBrandLabel);
        this.brandTextField = new JTextField();
        this.brandTextField.setEditable(this.allEditable);
        this.brandTextField.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        this.brandTextField.addFocusListener(new UpdateInstructionsFocusListener("Enter card brand name."));
        cardDetailsPanel.add(this.brandTextField);

        JLabel cardYearLabel = new JLabel("Card Year:");
        cardYearLabel.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        cardDetailsPanel.add(cardYearLabel);
        this.yearTextField = new JFormattedTextField();
        this.yearTextField.setEditable(this.allEditable);
        this.yearTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        this.yearTextField.setFocusLostBehavior(JFormattedTextField.PERSIST);
        this.yearTextField.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        this.yearTextField.addFocusListener(new UpdateInstructionsFocusListener("Enter card year."));
        cardDetailsPanel.add(this.yearTextField);

        JLabel cardNumberLabel = new JLabel("Card Number:");
        cardNumberLabel.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        cardDetailsPanel.add(cardNumberLabel);
        this.numberTextField = new JFormattedTextField();
        this.numberTextField.setEditable(this.allEditable);
        this.numberTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        this.numberTextField.setFocusLostBehavior(JFormattedTextField.PERSIST);
        this.numberTextField.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        this.numberTextField.addFocusListener(new UpdateInstructionsFocusListener("Enter card number."));
        cardDetailsPanel.add(this.numberTextField);

        JLabel cardValueLabel = new JLabel("Card Value:");
        cardValueLabel.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        cardDetailsPanel.add(cardValueLabel);
        this.valueTextField = new JFormattedTextField();
        this.valueTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getCurrencyInstance())));
        this.valueTextField.setFocusLostBehavior(JFormattedTextField.PERSIST);
        this.valueTextField.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        this.valueTextField.addFocusListener(new UpdateInstructionsFocusListener("Enter card value."));
        cardDetailsPanel.add(this.valueTextField);

        JLabel cardCountLabel = new JLabel("Card Count:");
        cardCountLabel.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        cardDetailsPanel.add(cardCountLabel);
        this.countTextField = new JFormattedTextField();
        this.countTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        this.countTextField.setFocusLostBehavior(JFormattedTextField.PERSIST);
        this.countTextField.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        this.countTextField.addFocusListener(new UpdateInstructionsFocusListener("Enter card count."));
        cardDetailsPanel.add(this.countTextField);

        JPanel playerDetailsPanel = new JPanel();
        playerDetailsPanel.setBorder(BorderFactory.createTitledBorder(null, "Player Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new Font("Tahoma", 0, 18))); // NOI18N
        playerDetailsPanel.setLayout(new GridLayout(2, 2));

        JLabel playerNameLabel = new JLabel("Player Name:");
        playerNameLabel.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        playerDetailsPanel.add(playerNameLabel);
        this.playerNameTextField = new JTextField();
        this.playerNameTextField.addFocusListener(new UpdateInstructionsFocusListener("Enter player name."));
        this.playerNameTextField.setEditable(this.allEditable);
        this.playerNameTextField.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        playerDetailsPanel.add(this.playerNameTextField);

        JLabel playerPositionLabel = new JLabel("Player Position:");
        playerPositionLabel.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        playerDetailsPanel.add(playerPositionLabel);
        this.playerPositionTextField = new JTextField();
        this.playerPositionTextField.addFocusListener(new UpdateInstructionsFocusListener("Enter player position."));
        this.playerPositionTextField.setEditable(this.allEditable);
        this.playerPositionTextField.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        playerDetailsPanel.add(this.playerPositionTextField);
        
        this.setLayout(new GridLayout(2, 1));
        this.add(cardDetailsPanel);
        this.add(playerDetailsPanel);

        addAncestorListener(new javax.swing.event.AncestorListener() {
            @Override
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }

            @Override
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                if (CardDetailsPanel.this.allEditable) {
                    CardDetailsPanel.this.brandTextField.requestFocusInWindow();
                } else {
                    CardDetailsPanel.this.valueTextField.requestFocusInWindow();
                }
            }

            @Override
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
    }
    private JTextField brandTextField;
    private JFormattedTextField countTextField;
    private JFormattedTextField numberTextField;
    private JTextField playerNameTextField;
    private JTextField playerPositionTextField;
    private JFormattedTextField valueTextField;
    private JFormattedTextField yearTextField;
    // End of variables declaration//GEN-END:variables
    private boolean allEditable = true;
    private InputVerifier notEmptyVerifier = new NotEmptyInputVerifier();
    private InputVerifier numVerifier = new PositiveIntegerInputVerifier();
    private InputVerifier yearVerifier = new YearInputVerifier();
    private InputVerifier currencyVerifier = new CurrencyInputVerifier();

    private static BaseballCard createBaseballCard() {
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
     * This is a test function for {@link CardDetailsPanel}. It simply creates a
     * {@link javax.swing.JFrame} in which to display the panel.
     *
     * @param args The command-line arguments (ignored).
     */
    public static void main(String[] args) {
        // TODO: Add a way to test getBaseballCard()

        final JFrame frame = new JFrame("CardDetailsPanel Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JPanel cardPanel = new JPanel();
        final CardLayout cl = new CardLayout();
        cardPanel.setLayout(cl);

        final CardDetailsPanel editablePanel = new CardDetailsPanel(true);
        cardPanel.add(editablePanel, "editablePanel");
        cardPanel.add(new CardDetailsPanel(false), "uneditablePanel");
        cardPanel.add(new CardDetailsPanel(createBaseballCard(), false));

        JPanel buttonPanel = new JPanel();
        JButton nextButton = new JButton("Next");

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                cl.next(cardPanel);
            }
        });

        buttonPanel.add(nextButton);

        JButton getCardButton = new JButton("Get Card");

        getCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    // TODO: How do I get the currently visible CardDetailsPanel?
//                    CardDetailsPanel panel = (CardDetailsPanel)cl.
                    BaseballCard card = editablePanel.getBaseballCard();

                    JOptionPane.showMessageDialog(frame, card, "Baseball Card", JOptionPane.INFORMATION_MESSAGE);
                } catch (InputException ex) {
                    JOptionPane.showMessageDialog(frame, ex, "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonPanel.add(getCardButton);

        frame.setLayout(new BorderLayout());
        frame.add(cardPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }
}
