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
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        this.card = card;
        this.allEditable = allEditable;

        this.initComponents();

        this.cardBrandTextField.setText(this.card.getBrand());
        this.cardYearTextField.setValue(this.card.getYear());
        this.cardNumberTextField.setValue(this.card.getNumber());

        // TODO: This works, but the logic should be part of the JFormattedTextField, not the panel
        int value = this.card.getValue();
        int dollars = value / 100;
        int cents = value % 100;
        String centsStr = cents < 10 ? "0" + cents : "" + cents;
        String valueStr = "$" + dollars + "." + centsStr;

        this.cardValueTextField.setText(valueStr);
        this.cardCountTextField.setValue(this.card.getCount());
        this.playerNameTextField.setText(this.card.getPlayerName());
        this.playerPositionTextField.setText(this.card.getPlayerPosition());
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
        InputVerifier notEmpty = new NotEmptyInputVerifier();
        this.cardBrandTextField.selectAll();
        this.cardBrandTextField.requestFocusInWindow();
        if (!notEmpty.verify(this.cardBrandTextField)) {
            throw new InputException("Please enter a card brand.");
        }
        String cardBrand = this.cardBrandTextField.getText();

        // Validate card year
        this.cardYearTextField.selectAll();
        this.cardYearTextField.requestFocusInWindow();
        if (!notEmpty.verify(this.cardYearTextField)) {
            throw new InputException("Please enter a card year.");
        }
        if (!this.cardYearTextField.isEditValid()) {
            throw new InputException("Please enter a numerical year.");
        }
        int cardYear = Integer.parseInt(this.cardYearTextField.getText());
        // TODO: Delegate this to an InputVerifier?
        if (cardYear < 1000 || cardYear > 9999) {
            throw new InputException("Please enter a four-digit year.");
        }

        // Validate card number
        this.cardNumberTextField.selectAll();
        this.cardNumberTextField.requestFocusInWindow();
        if (!notEmpty.verify(this.cardNumberTextField)) {
            throw new InputException("Please enter a card number.");
        }
        if (!this.cardNumberTextField.isEditValid()) {
            throw new InputException("Please enter a number.");
        }
        int cardNumber = Integer.parseInt(this.cardNumberTextField.getText());
        // TODO: Delegate this to an InputVerifier?
        if (cardNumber < 0) {
            throw new InputException("Please enter a positive value for the card number.");
        }

        // Validate card value
        this.cardValueTextField.selectAll();
        this.cardValueTextField.requestFocusInWindow();
        if (!notEmpty.verify(this.cardValueTextField)) {
            throw new InputException("Please enter a card value.");
        }
        if (!this.cardValueTextField.isEditValid()) {
            throw new InputException("Please enter a valid monetary value.");
        }
        try {
            this.cardValueTextField.commitEdit();
        } catch (ParseException ex) {
            throw new InputException(ex);
        }
        double cardValueDbl = ((Number) this.cardValueTextField.getValue()).doubleValue();
        // TODO: Delegate this to an InputVerifier?
        if (cardValueDbl < 0.0) {
            throw new InputException("Please enter a positive card value.'");
        }
        int cardValue = (int) (cardValueDbl * 100);

        // Validate card count
        this.cardCountTextField.selectAll();
        this.cardCountTextField.requestFocusInWindow();
        if (!notEmpty.verify(this.cardCountTextField)) {
            throw new InputException("Please enter a card count.");
        }
        if (!this.cardCountTextField.isEditValid()) {
            throw new InputException("Please enter a numeric value for the card count.");
        }
        int cardCount = Integer.parseInt(this.cardCountTextField.getText());
        if (cardCount < 0) {
            throw new InputException("Please enter a positive value for the card count");
        }

        // Validate player name
        this.playerNameTextField.selectAll();
        this.playerNameTextField.requestFocusInWindow();
        if (!notEmpty.verify(this.playerNameTextField)) {
            throw new InputException("Please enter a player name.");
        }
        String playerName = this.playerNameTextField.getText();

        // Validate player position
        this.playerPositionTextField.selectAll();
        this.playerPositionTextField.requestFocusInWindow();
        if (!notEmpty.verify(this.playerPositionTextField)) {
            throw new InputException("Please enter a player position.");
        }
        String playerPosition = this.playerPositionTextField.getText();

        return new BaseballCard(cardBrand, cardYear, cardNumber, cardValue, cardCount, playerName, playerPosition);
    }

    /**
     * Reset all text fields to blank strings and set focus to the card brand
     * text field.
     */
    public void reset() {
        this.cardBrandTextField.setText("");
        this.cardCountTextField.setText("");
        this.cardNumberTextField.setText("");
        this.cardValueTextField.setText("");
        this.cardYearTextField.setText("");
        this.playerNameTextField.setText("");
        this.playerPositionTextField.setText("");
        this.cardBrandTextField.requestFocusInWindow();
    }

    /**
     * Set whether or not the instruction label in {@link BBCTFrame} should be
     * updated when focus changes between text fields.
     *
     * @param updateInstructions The flag indicating whether or not the
     * instruction label should be updated.
     */
    public void setUpdateInstructions(boolean updateInstructions) {
        this.updateInstructions = updateInstructions;
    }

    private void addFocusListeners() {
        this.cardBrandTextField.addFocusListener(new UpdateInstructionsFocusListener("Enter card brand name."));
        this.cardYearTextField.addFocusListener(new UpdateInstructionsFocusListener("Enter card year."));
        this.cardNumberTextField.addFocusListener(new UpdateInstructionsFocusListener("Enter card number."));
        this.cardValueTextField.addFocusListener(new UpdateInstructionsFocusListener("Enter card value."));
        this.cardCountTextField.addFocusListener(new UpdateInstructionsFocusListener("Enter card count."));
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
        cardBrandTextField = new javax.swing.JTextField();
        cardNumberTextField = new javax.swing.JFormattedTextField();
        cardYearTextField = new javax.swing.JFormattedTextField();
        cardValueTextField = new javax.swing.JFormattedTextField();
        cardCountTextField = new javax.swing.JFormattedTextField();
        javax.swing.JPanel playerDetailsPanel = new javax.swing.JPanel();
        javax.swing.JLabel playerNameLabel = new javax.swing.JLabel();
        javax.swing.JLabel playerPositionLabel = new javax.swing.JLabel();
        playerNameTextField = new javax.swing.JTextField();
        playerPositionTextField = new javax.swing.JTextField();

        setMinimumSize(new java.awt.Dimension(375, 350));
        setPreferredSize(new java.awt.Dimension(375, 350));
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

        cardBrandTextField.setEditable(this.allEditable);
        cardBrandTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        cardNumberTextField.setEditable(this.allEditable);
        cardNumberTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        cardNumberTextField.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);
        cardNumberTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        cardYearTextField.setEditable(this.allEditable);
        cardYearTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        cardYearTextField.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);
        cardYearTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        cardValueTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getCurrencyInstance())));
        cardValueTextField.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);
        cardValueTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        cardCountTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        cardCountTextField.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);
        cardCountTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

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
                            .addComponent(cardValueTextField)
                            .addComponent(cardCountTextField)))
                    .addGroup(cardDetailsPanelLayout.createSequentialGroup()
                        .addGroup(cardDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cardYearLabel)
                            .addComponent(cardBrandLabel)
                            .addComponent(cardNumberLabel))
                        .addGap(0, 25, 25)
                        .addGroup(cardDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cardNumberTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cardYearTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cardBrandTextField, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        cardDetailsPanelLayout.setVerticalGroup(
            cardDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardDetailsPanelLayout.createSequentialGroup()
                .addGroup(cardDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardBrandLabel)
                    .addComponent(cardBrandTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(cardDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardYearLabel)
                    .addComponent(cardYearTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(cardDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardNumberLabel)
                    .addComponent(cardNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(cardDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardValueLabel)
                    .addComponent(cardValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(cardDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardCountLabel)
                    .addComponent(cardCountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            this.cardBrandTextField.requestFocusInWindow();
        } else {
            this.cardValueTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_formAncestorAdded
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField cardBrandTextField;
    private javax.swing.JFormattedTextField cardCountTextField;
    private javax.swing.JFormattedTextField cardNumberTextField;
    private javax.swing.JFormattedTextField cardValueTextField;
    private javax.swing.JFormattedTextField cardYearTextField;
    private javax.swing.JTextField playerNameTextField;
    private javax.swing.JTextField playerPositionTextField;
    // End of variables declaration//GEN-END:variables
    private boolean allEditable = true;
    
    // TODO: Is this field necessary?
    private boolean updateInstructions = true;
    private BaseballCard card = null;

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
