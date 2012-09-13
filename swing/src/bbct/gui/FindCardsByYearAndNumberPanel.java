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
import bbct.data.BaseballCardIO;
import bbct.exceptions.BBCTIOException;
import bbct.exceptions.InputException;
import bbct.gui.event.UpdateInstructionsFocusListener;
import bbct.gui.event.UpdateTitleAncestorListener;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.InputVerifier;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

/**
 * {@link FindCardsByYearAndNumberPanel} allows the user to input a card number
 * and year. These values are used as the parameters when searching the
 * underlying storage mechanism for cards with the given year and number.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class FindCardsByYearAndNumberPanel extends FindCardsByPanel {

    /**
     * Creates a new {@link FindCardsByYearAndNumberPanel} with two JLabels and
     * two JFormattedTextFields. The text fields validate input for integer
     * values which are used as the baseball card year and number when searching
     * for cards in the underlying storage.
     *
     * @param bcio The {@link BaseballCardIO} object which is used to search for
     * baseball cards with the year and number input by the user.
     */
    public FindCardsByYearAndNumberPanel(BaseballCardIO bcio) {
        this.bcio = bcio;
        initComponents();
    }

    /**
     * Searches the underlying storage mechanism for baseball card records from
     * the year and with the card number given in the text fields. The method
     * also checks that the input is valid: i.e. that the year is a positive,
     * four-digit integer and that the number is a is a positive integer.
     *
     * @return A list of {@link BaseballCard}s which have the number given by
     * the user.
     * @throws BBCTIOException If there is an error reading the underlying
     * storage mechanism.
     * @throws InputException If the input is invalid.
     */
    @Override
    protected List<BaseballCard> getBaseballCards() throws BBCTIOException, InputException {
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
        if (!this.numberVerifier.verify(this.numberTextField)) {
            throw new InputException("Please enter a valid card number. (The number must be positive).");
        }
        int number = Integer.parseInt(this.numberTextField.getText());

        return this.bcio.getBaseballCardsByYearAndNumber(year, number);
    }

    @Override
    protected void setFocus() {
        // TODO: Is this the correct place to clear the text fields?
        this.yearTextField.setText("");
        this.numberTextField.setText("");
        this.yearTextField.requestFocusInWindow();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());

        JLabel yearLabel = new JLabel("Card Year:");
        yearLabel.setFont(new Font("Tahoma", 0, 14)); // NOI18N

        GridBagConstraints yearLabelConstraints = new GridBagConstraints();
        yearLabelConstraints.gridx = 0;
        yearLabelConstraints.gridy = 0;
        yearLabelConstraints.weightx = 1;
        yearLabelConstraints.weighty = 1;
        yearLabelConstraints.anchor = GridBagConstraints.WEST;
        yearLabelConstraints.insets = new Insets(20, 25, 10, 10);
        inputPanel.add(yearLabel, yearLabelConstraints);

        this.yearTextField = new JFormattedTextField();
        this.yearTextField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
        this.yearTextField.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        this.yearTextField.setColumns(10);
        this.yearTextField.addFocusListener(new UpdateInstructionsFocusListener("Enter card year."));

        GridBagConstraints yearTextFieldConstraints = new GridBagConstraints();
        yearTextFieldConstraints.gridx = 1;
        yearTextFieldConstraints.gridy = 0;
        yearTextFieldConstraints.weightx = 2;
        yearTextFieldConstraints.weighty = 1;
        yearTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        yearTextFieldConstraints.insets = new Insets(20, 10, 10, 25);
        inputPanel.add(this.yearTextField, yearTextFieldConstraints);

        JLabel cardNumberLabel = new JLabel("Card Number:");
        cardNumberLabel.setFont(new Font("Tahoma", 0, 14)); // NOI18N

        GridBagConstraints numberLabelConstraints = new GridBagConstraints();
        numberLabelConstraints.gridx = 0;
        numberLabelConstraints.gridy = 1;
        numberLabelConstraints.weightx = 1;
        numberLabelConstraints.weighty = 1;
        numberLabelConstraints.anchor = GridBagConstraints.WEST;
        numberLabelConstraints.insets = new Insets(10, 25, 0, 10);
        inputPanel.add(cardNumberLabel, numberLabelConstraints);

        this.numberTextField = new JFormattedTextField();
        this.numberTextField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
        this.numberTextField.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        this.numberTextField.setColumns(10);
        this.numberTextField.addFocusListener(new UpdateInstructionsFocusListener("Enter card number."));

        GridBagConstraints numberTextFieldConstraints = new GridBagConstraints();
        numberTextFieldConstraints.gridx = 1;
        numberTextFieldConstraints.gridy = 1;
        numberTextFieldConstraints.weightx = 2;
        numberTextFieldConstraints.weighty = 1;
        numberTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        numberTextFieldConstraints.insets = new Insets(10, 10, 0, 25);
        inputPanel.add(this.numberTextField, numberTextFieldConstraints);

        this.add(inputPanel, BorderLayout.PAGE_START);
        this.addAncestorListener(new UpdateTitleAncestorListener(GUIResources.FIND_CARDS_BY_YEAR_AND_NUMBER_PANEL_TITLE));
    }
    private JFormattedTextField numberTextField;
    private JFormattedTextField yearTextField;
    private BaseballCardIO bcio = null;
    private InputVerifier yearVerifier = new YearInputVerifier();
    private InputVerifier numberVerifier = new PositiveIntegerInputVerifier();


    /**
     * This is a test function for {@link FindCardsByYearAndNumberPanel}. It
     * simply creates a {@link javax.swing.JFrame} in which to display the
     * panel.
     *
     * @param args Command-line arguments. (ignored)
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("FindCardsByNumberPanel Test");
        frame.add(new FindCardsByYearAndNumberPanel(null));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 425);
        frame.setVisible(true);
    }
}
