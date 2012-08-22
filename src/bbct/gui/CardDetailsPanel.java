package bbct.gui;

import bbct.data.BaseballCard;
import bbct.exceptions.InputException;

/**
 * TODO: cardValueTextField needs to be formatted as currency
 *
 * TODO: Figure out when to call this.cardBrandTextField.requestFocusInWindow();
 *
 * TODO: JavaDoc
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class CardDetailsPanel extends javax.swing.JPanel {

    /**
     * Creates new {@link CardDetailsPanel}.
     */
    public CardDetailsPanel() {
        initComponents();
    }

    /**
     * Creates new {@link CardDetailsPanel}.
     *
     * @param allEditable
     */
    public CardDetailsPanel(boolean allEditable) {
        this.allEditable = allEditable;
        initComponents();
    }

    /**
     * Creates new {@link CardDetailsPanel}.
     *
     * @param card
     * @param allEditable
     */
    public CardDetailsPanel(BaseballCard card, boolean allEditable) {
        this.card = card;
        this.allEditable = allEditable;

        initComponents();

        this.cardBrandTextField.setText(this.card.getBrand());
        this.cardYearTextField.setText(Integer.toString(this.card.getYear()));
        this.cardNumberTextField.setText(Integer.toString(this.card.getNumber()));

        // TODO: Fix formatting
        this.cardValueTextField.setText(Integer.toString(this.card.getValue()));
        this.cardCountTextField.setText(Integer.toString(this.card.getCount()));
        this.playerNameTextField.setText(this.card.getPlayerName());
        this.playerPositionTextField.setText(this.card.getPlayerPosition());
    }

    /**
     *
     * @return @throws InputException 
     * @throws InputException
     */
    public BaseballCard getBaseballCard() throws InputException {
        if (this.cardBrandTextField.getText().equals("")) {
            throw new InputException("Please enter a card brand.");
        }
        String cardBrand = this.cardBrandTextField.getText();

        if (this.cardYearTextField.getText().equals("")) {
            throw new InputException("Please enter a card year.");
        }
        int cardYear = Integer.parseInt(this.cardYearTextField.getText());

        if (this.cardNumberTextField.getText().equals("")) {
            throw new InputException("Please enter a card number.");
        }
        int cardNumber = Integer.parseInt(this.cardNumberTextField.getText());

        if (this.cardValueTextField.getText().equals("")) {
            throw new InputException("Please enter a card value.");
        }
        int cardValue = Integer.parseInt(this.cardValueTextField.getText());

        if (this.cardCountTextField.getText().equals("")) {
            throw new InputException("Please enter a card count.");
        }
        int cardCount = Integer.parseInt(this.cardCountTextField.getText());

        if (this.playerNameTextField.getText().equals("")) {
            throw new InputException("Please enter a player name.");
        }
        String playerName = this.playerNameTextField.getText();

        if (this.playerPositionTextField.getText().equals("")) {
            throw new InputException("Please enter a player position.");
        }
        String playerPosition = this.playerPositionTextField.getText();

        return new BaseballCard(cardBrand, cardYear, cardNumber, cardValue, cardCount, playerName, playerPosition);
    }

    /**
     * Reset all text fields to blank strings and set focus to {@link cardBrandTextField}.
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

        cardDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Card Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 18))); // NOI18N
        // card postinit
        cardBrandTextField.setEnabled(this.allEditable);
        cardNumberTextField.setEnabled(this.allEditable);
        cardYearTextField.setEnabled(this.allEditable);

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

        cardBrandTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cardBrandTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cardBrandTextFieldFocusGained(evt);
            }
        });

        cardNumberTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        cardNumberTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        cardYearTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        cardYearTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        cardValueTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        cardValueTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        cardCountTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
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
        // player post init
        playerNameTextField.setEnabled(this.allEditable);
        playerPositionTextField.setEditable(this.allEditable);

        playerNameLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        playerNameLabel.setText("Player Name:");

        playerPositionLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        playerPositionLabel.setText("Player Position:");

        playerNameTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

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

    private void cardBrandTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cardBrandTextFieldFocusGained
        BBCTFrame frame = (BBCTFrame) this.getTopLevelAncestor();
        frame.setInstructions("Enter card brand name.");
    }//GEN-LAST:event_cardBrandTextFieldFocusGained
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
    private BaseballCard card = null;
}
