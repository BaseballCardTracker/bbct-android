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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
public class CardDetailsPanel extends javax.swing.JPanel {

    /**
     * Creates a new {@link CardDetailsPanel}.
     */
    public CardDetailsPanel() {
        this.initComponents();
    }

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
        this.addFocusListeners();
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

    private void addFocusListeners() {
        this.brandTextField.addFocusListener(new UpdateInstructionsFocusListener("Enter card brand name."));
        this.yearTextField.addFocusListener(new UpdateInstructionsFocusListener("Enter card year."));
        this.numberTextField.addFocusListener(new UpdateInstructionsFocusListener("Enter card number."));
        this.valueTextField.addFocusListener(new UpdateInstructionsFocusListener("Enter card value."));
        this.countTextField.addFocusListener(new UpdateInstructionsFocusListener("Enter card count."));
        this.playerNameTextField.addFocusListener(new UpdateInstructionsFocusListener("Enter player name."));
        this.playerPositionTextField.addFocusListener(new UpdateInstructionsFocusListener("Enter player position."));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JPanel cardDetailsPanel = new javax.swing.JPanel();
        javax.swing.JLabel cardNumberLabel = new javax.swing.JLabel();
        javax.swing.JLabel cardBrandLabel = new javax.swing.JLabel();
        javax.swing.JLabel cardYearLabel = new javax.swing.JLabel();
        javax.swing.JLabel cardValueLabel = new javax.swing.JLabel();
        javax.swing.JLabel cardCountLabel = new javax.swing.JLabel();
        brandTextField = new javax.swing.JTextField();
        numberTextField = new javax.swing.JFormattedTextField();
        yearTextField = new javax.swing.JFormattedTextField();
        valueTextField = new javax.swing.JFormattedTextField();
        countTextField = new javax.swing.JFormattedTextField();
        javax.swing.JPanel playerDetailsPanel = new javax.swing.JPanel();
        javax.swing.JLabel playerNameLabel = new javax.swing.JLabel();
        javax.swing.JLabel playerPositionLabel = new javax.swing.JLabel();
        playerNameTextField = new javax.swing.JTextField();
        playerPositionTextField = new javax.swing.JTextField();

        addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                formAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        cardDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Card Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 18))); // NOI18N

        cardNumberLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cardNumberLabel.setText("Card Number:");

        cardBrandLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cardBrandLabel.setText("Card Brand:");

        cardYearLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cardYearLabel.setText("Card Year:");

        cardValueLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cardValueLabel.setText("Card Value:");

        cardCountLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cardCountLabel.setText("Card Count:");

        brandTextField.setEditable(this.allEditable);
        brandTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        numberTextField.setEditable(this.allEditable);
        numberTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        numberTextField.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);
        numberTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        yearTextField.setEditable(this.allEditable);
        yearTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        yearTextField.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);
        yearTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        valueTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getCurrencyInstance())));
        valueTextField.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);
        valueTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        countTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        countTextField.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);
        countTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        javax.swing.GroupLayout cardDetailsPanelLayout = new javax.swing.GroupLayout(cardDetailsPanel);
        cardDetailsPanel.setLayout(cardDetailsPanelLayout);
        cardDetailsPanelLayout.setHorizontalGroup(
            cardDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cardDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cardDetailsPanelLayout.createSequentialGroup()
                        .addGroup(cardDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cardCountLabel)
                            .addComponent(cardValueLabel))
                        .addGap(36, 36, 36)
                        .addGroup(cardDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(valueTextField)
                            .addComponent(countTextField)))
                    .addGroup(cardDetailsPanelLayout.createSequentialGroup()
                        .addGroup(cardDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cardYearLabel)
                            .addComponent(cardBrandLabel)
                            .addComponent(cardNumberLabel))
                        .addGap(0, 25, 25)
                        .addGroup(cardDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(numberTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(yearTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(brandTextField, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        cardDetailsPanelLayout.setVerticalGroup(
            cardDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardDetailsPanelLayout.createSequentialGroup()
                .addGroup(cardDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardBrandLabel)
                    .addComponent(brandTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(cardDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardYearLabel)
                    .addComponent(yearTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(cardDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardNumberLabel)
                    .addComponent(numberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(cardDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardValueLabel)
                    .addComponent(valueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(cardDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardCountLabel)
                    .addComponent(countTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        playerDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Player Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 18))); // NOI18N

        playerNameLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        playerNameLabel.setText("Player Name:");

        playerPositionLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        playerPositionLabel.setText("Player Position:");

        playerNameTextField.setEditable(this.allEditable);
        playerNameTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        playerPositionTextField.setEditable(this.allEditable);
        playerPositionTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        javax.swing.GroupLayout playerDetailsPanelLayout = new javax.swing.GroupLayout(playerDetailsPanel);
        playerDetailsPanel.setLayout(playerDetailsPanelLayout);
        playerDetailsPanelLayout.setHorizontalGroup(
            playerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(playerDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(playerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(playerPositionLabel)
                    .addComponent(playerNameLabel))
                .addGap(18, 18, 18)
                .addGroup(playerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(playerPositionTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                    .addComponent(playerNameTextField))
                .addContainerGap())
        );
        playerDetailsPanelLayout.setVerticalGroup(
            playerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(playerDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(playerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(playerNameLabel)
                    .addComponent(playerNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(playerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(playerPositionLabel)
                    .addComponent(playerPositionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cardDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(playerDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(cardDetailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(playerDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_formAncestorAdded
        if (this.allEditable) {
            this.brandTextField.requestFocusInWindow();
        } else {
            this.valueTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_formAncestorAdded
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField brandTextField;
    private javax.swing.JFormattedTextField countTextField;
    private javax.swing.JFormattedTextField numberTextField;
    private javax.swing.JTextField playerNameTextField;
    private javax.swing.JTextField playerPositionTextField;
    private javax.swing.JFormattedTextField valueTextField;
    private javax.swing.JFormattedTextField yearTextField;
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

        final CardDetailsPanel editablePanel = new CardDetailsPanel();
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
