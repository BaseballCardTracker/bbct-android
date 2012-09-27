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
package bbct.swing.gui;

import bbct.common.data.BaseballCard;
import bbct.common.exceptions.InputException;
import bbct.swing.BBCTStringResources;
import bbct.swing.BBCTStringResources.TitleResources;
import bbct.swing.gui.event.UpdateInstructionsFocusListener;
import bbct.swing.gui.inputverifiers.CurrencyInputVerifier;
import bbct.swing.gui.inputverifiers.NotEmptyInputVerifier;
import bbct.swing.gui.inputverifiers.PositiveIntegerInputVerifier;
import bbct.swing.gui.inputverifiers.YearInputVerifier;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

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
 * TODO: Change player position text field to a combobox.
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
        this.playerPositionComboBox.setSelectedItem(card.getPlayerPosition());
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
            throw new InputException(BBCTStringResources.ErrorResources.CARD_BRAND_ERROR);
        }
        String brand = this.brandTextField.getText();

        // Validate year, number, value, and count
        int year = ((Number) this.verifyTextField(this.yearTextField, this.yearVerifier, BBCTStringResources.ErrorResources.CARD_YEAR_ERROR)).intValue();
        int number = ((Number) this.verifyTextField(this.numberTextField, this.numVerifier, BBCTStringResources.ErrorResources.CARD_NUMBER_ERROR)).intValue();

        double valueDbl = ((Number) this.verifyTextField(this.valueTextField, this.currencyVerifier, BBCTStringResources.ErrorResources.CARD_VALUE_ERROR)).doubleValue();
        int value = (int) (valueDbl * 100);
        int count = ((Number) this.verifyTextField(this.countTextField, this.numVerifier, BBCTStringResources.ErrorResources.CARD_COUNT_ERROR)).intValue();

        // Validate player name
        this.playerNameTextField.selectAll();
        this.playerNameTextField.requestFocusInWindow();
        if (!this.notEmptyVerifier.verify(this.playerNameTextField)) {
            throw new InputException(BBCTStringResources.ErrorResources.PLAYER_NAME_ERROR);
        }
        String playerName = this.playerNameTextField.getText();

        // Validate player position
        this.playerPositionComboBox.requestFocusInWindow();
        if (this.playerPositionComboBox.getSelectedIndex() == -1) {
            throw new InputException(BBCTStringResources.ErrorResources.PLAYER_POSITION_ERROR);
        }
        String playerPosition = (String) this.playerPositionComboBox.getSelectedItem();

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
        this.playerPositionComboBox.setSelectedIndex(-1);
        this.brandTextField.requestFocusInWindow();
    }

    private void initComponents() {
        final int topOutside = 5;
        final int bottomOutside = 5;
        final int rightOutside = 10;
        final int leftOutside = 10;
        final int inside = 10;
        Insets topLeftInsets = new Insets(topOutside, leftOutside, inside, inside);
        Insets topRightInsets = new Insets(topOutside, inside, inside, rightOutside);
        Insets leftInsets = new Insets(inside, leftOutside, inside, inside);
        Insets rightInsets = new Insets(inside, inside, inside, rightOutside);
        Insets bottomLeftInsets = new Insets(inside, leftOutside, bottomOutside, inside);
        Insets bottomRightInsets = new Insets(inside, inside, bottomOutside, rightOutside);

        JPanel cardDetailsPanel = new JPanel(new BorderLayout());
        Border emptyBorder = BorderFactory.createEmptyBorder(5, 10, 5, 10);
        TitledBorder titledCardDetailsBorder = BorderFactory.createTitledBorder(TitleResources.CARD_DETAILS_BORDER_TITLE);
        titledCardDetailsBorder.setTitleFont(new Font("Tahoma", 0, 18));
        Border cardDetailsBorder = BorderFactory.createCompoundBorder(emptyBorder, titledCardDetailsBorder);
        cardDetailsPanel.setBorder(cardDetailsBorder);

        JPanel cardDetailsInputPanel = new JPanel(new GridBagLayout());
        
        JLabel brandLabel = new JLabel(BBCTStringResources.LabelResources.CARD_BRAND_LABEL);
        brandLabel.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.insets = topLeftInsets;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        cardDetailsInputPanel.add(brandLabel, gbc);

        this.brandTextField = new JTextField();
        this.brandTextField.setEditable(this.allEditable);
        this.brandTextField.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        this.brandTextField.setColumns(CardDetailsPanel.TEXT_FIELD_COLUMNS);
        this.brandTextField.addFocusListener(new UpdateInstructionsFocusListener(BBCTStringResources.InstructionResources.CARD_BRAND_INSTRUCTIONS));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 2;
        gbc.insets = topRightInsets;
        cardDetailsInputPanel.add(this.brandTextField, gbc);

        JLabel yearLabel = new JLabel(BBCTStringResources.LabelResources.CARD_YEAR_LABEL);
        yearLabel.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.insets = leftInsets;
        cardDetailsInputPanel.add(yearLabel, gbc);

        this.yearTextField = new JFormattedTextField();
        this.yearTextField.setEditable(this.allEditable);
        this.yearTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        this.yearTextField.setFocusLostBehavior(JFormattedTextField.PERSIST);
        this.yearTextField.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        this.yearTextField.setColumns(CardDetailsPanel.TEXT_FIELD_COLUMNS);
        this.yearTextField.addFocusListener(new UpdateInstructionsFocusListener(BBCTStringResources.InstructionResources.CARD_YEAR_INSTRUCTIONS));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 2;
        gbc.insets = rightInsets;
        cardDetailsInputPanel.add(this.yearTextField, gbc);

        JLabel numberLabel = new JLabel(BBCTStringResources.LabelResources.CARD_NUMBER_LABEL);
        numberLabel.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.insets = leftInsets;
        cardDetailsInputPanel.add(numberLabel, gbc);

        this.numberTextField = new JFormattedTextField();
        this.numberTextField.setEditable(this.allEditable);
        this.numberTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        this.numberTextField.setFocusLostBehavior(JFormattedTextField.PERSIST);
        this.numberTextField.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        this.numberTextField.setColumns(CardDetailsPanel.TEXT_FIELD_COLUMNS);
        this.numberTextField.addFocusListener(new UpdateInstructionsFocusListener(BBCTStringResources.InstructionResources.CARD_NUMBER_INSTRUCTIONS));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 2;
        gbc.insets = rightInsets;
        cardDetailsInputPanel.add(this.numberTextField, gbc);

        JLabel valueLabel = new JLabel(BBCTStringResources.LabelResources.CARD_VALUE_LABEL);
        valueLabel.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1;
        gbc.insets = leftInsets;
        cardDetailsInputPanel.add(valueLabel, gbc);

        this.valueTextField = new JFormattedTextField();
        this.valueTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getCurrencyInstance())));
        this.valueTextField.setFocusLostBehavior(JFormattedTextField.PERSIST);
        this.valueTextField.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        this.valueTextField.setColumns(CardDetailsPanel.TEXT_FIELD_COLUMNS);
        this.valueTextField.addFocusListener(new UpdateInstructionsFocusListener(BBCTStringResources.InstructionResources.CARD_VALUE_INSTRUCTIONS));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 2;
        gbc.insets = rightInsets;
        cardDetailsInputPanel.add(this.valueTextField, gbc);

        JLabel countLabel = new JLabel(BBCTStringResources.LabelResources.CARD_COUNT_LABEL);
        countLabel.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1;
        gbc.insets = leftInsets;
        cardDetailsInputPanel.add(countLabel, gbc);

        this.countTextField = new JFormattedTextField();
        this.countTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        this.countTextField.setFocusLostBehavior(JFormattedTextField.PERSIST);
        this.countTextField.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        this.countTextField.setColumns(CardDetailsPanel.TEXT_FIELD_COLUMNS);
        this.countTextField.addFocusListener(new UpdateInstructionsFocusListener(BBCTStringResources.InstructionResources.CARD_COUNT_INSTRUCTIONS));
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 2;
        gbc.insets = rightInsets;
        cardDetailsInputPanel.add(this.countTextField, gbc);
        
        cardDetailsPanel.add(cardDetailsInputPanel, BorderLayout.PAGE_START);

        JPanel playerDetailsPanel = new JPanel(new BorderLayout());
        TitledBorder titledPlayerDetailsBorder = BorderFactory.createTitledBorder(TitleResources.PLAYER_DETAILS_BORDER_TITLE);
        titledPlayerDetailsBorder.setTitleFont(new Font("Tahoma", 0, 18));
        Border playerDetailsBorder = BorderFactory.createCompoundBorder(emptyBorder, titledPlayerDetailsBorder);
        playerDetailsPanel.setBorder(playerDetailsBorder);

        JPanel playerDetailsInputPanel = new JPanel(new GridBagLayout());
        
        JLabel playerNameLabel = new JLabel(BBCTStringResources.LabelResources.PLAYER_NAME_LABEL);
        playerNameLabel.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.insets = topLeftInsets;
        playerDetailsInputPanel.add(playerNameLabel, gbc);

        this.playerNameTextField = new JTextField();
        this.playerNameTextField.setEditable(this.allEditable);
        this.playerNameTextField.setColumns(CardDetailsPanel.TEXT_FIELD_COLUMNS);
        this.playerNameTextField.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        this.playerNameTextField.addFocusListener(new UpdateInstructionsFocusListener(BBCTStringResources.InstructionResources.PLAYER_NAME_INSTRUCTIONS));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 2;
        gbc.insets = topRightInsets;
        playerDetailsInputPanel.add(this.playerNameTextField, gbc);

        JLabel playerPositionLabel = new JLabel(BBCTStringResources.LabelResources.PLAYER_POSITION_LABEL);
        playerPositionLabel.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.insets = bottomLeftInsets;
        playerDetailsInputPanel.add(playerPositionLabel, gbc);

        this.playerPositionComboBox = new JComboBox<>(BBCTStringResources.ComboBoxResources.POSITIONS);
        this.playerPositionComboBox.setEditable(this.allEditable);
        this.playerPositionComboBox.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        this.playerPositionComboBox.addFocusListener(new UpdateInstructionsFocusListener(BBCTStringResources.InstructionResources.PLAYER_POSITION_INSTRUCTIONS));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 2;
        gbc.insets = bottomRightInsets;
        playerDetailsInputPanel.add(this.playerPositionComboBox, gbc);
        
        playerDetailsPanel.add(playerDetailsInputPanel, BorderLayout.PAGE_START);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(cardDetailsPanel);
        this.add(playerDetailsPanel);

        addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorMoved(AncestorEvent evt) {
            }

            @Override
            public void ancestorAdded(AncestorEvent evt) {
                if (CardDetailsPanel.this.allEditable) {
                    CardDetailsPanel.this.brandTextField.requestFocusInWindow();
                } else {
                    CardDetailsPanel.this.valueTextField.requestFocusInWindow();
                }
            }

            @Override
            public void ancestorRemoved(AncestorEvent evt) {
            }
        });
    }

    private Object verifyTextField(JFormattedTextField tf, InputVerifier v, String errorMessage) throws InputException {
        tf.selectAll();
        tf.requestFocusInWindow();
        try {
            tf.commitEdit();
        } catch (ParseException ex) {
            throw new InputException(errorMessage, ex);
        }
        if (!v.verify(tf)) {
            throw new InputException(errorMessage);
        }

        return tf.getValue();
    }
    private JTextField brandTextField;
    private JFormattedTextField countTextField;
    private JFormattedTextField numberTextField;
    private JTextField playerNameTextField;
    private JComboBox<String> playerPositionComboBox;
    private JFormattedTextField valueTextField;
    private JFormattedTextField yearTextField;
    private boolean allEditable = true;
    private InputVerifier notEmptyVerifier = new NotEmptyInputVerifier();
    private InputVerifier numVerifier = new PositiveIntegerInputVerifier();
    private InputVerifier yearVerifier = new YearInputVerifier();
    private InputVerifier currencyVerifier = new CurrencyInputVerifier();
    private static final int TEXT_FIELD_COLUMNS = 15;

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
