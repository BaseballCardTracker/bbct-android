/*
 * This file is part of BBCT.
 *
 * Copyright 2012-14 codeguru <codeguru@users.sourceforge.net>
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
import bbct.common.data.BaseballCardIO;
import bbct.common.exceptions.BBCTIOException;
import bbct.common.exceptions.InputException;
import bbct.swing.BBCTStringResources;
import bbct.swing.FontResources;
import bbct.swing.gui.event.UpdateInstructionsFocusListener;
import bbct.swing.gui.event.UpdateTitleAncestorListener;
import bbct.swing.gui.inputverifiers.PositiveIntegerInputVerifier;
import bbct.swing.gui.inputverifiers.YearInputVerifier;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
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
 */
@SuppressWarnings("serial")
public class FindCardsByYearAndNumberPanel extends FindCardsByPanel {
    private JFormattedTextField numberTextField;
    private JFormattedTextField yearTextField;
    private BaseballCardIO bcio = null;
    private InputVerifier yearVerifier = new YearInputVerifier();
    private InputVerifier numberVerifier = new PositiveIntegerInputVerifier();

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
        this.initComponents();
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
            throw new InputException(BBCTStringResources.ErrorResources.CARD_YEAR_ERROR, ex);
        }
        if (!this.yearVerifier.verify(this.yearTextField)) {
            throw new InputException(BBCTStringResources.ErrorResources.CARD_YEAR_ERROR);
        }
        int year = Integer.parseInt(this.yearTextField.getText());

        // Validate card number
        this.numberTextField.selectAll();
        this.numberTextField.requestFocusInWindow();
        try {
            this.numberTextField.commitEdit();
        } catch (ParseException ex) {
            throw new InputException(BBCTStringResources.ErrorResources.CARD_NUMBER_ERROR, ex);
        }
        if (!this.numberVerifier.verify(this.numberTextField)) {
            throw new InputException(BBCTStringResources.ErrorResources.CARD_NUMBER_ERROR);
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

        JLabel yearLabel = new JLabel(BBCTStringResources.LabelResources.CARD_YEAR_LABEL);
        yearLabel.setFont(FontResources.DEFAULT_FONT);

        GridBagConstraints yearLabelConstraints = new GridBagConstraints();
        yearLabelConstraints.gridx = 0;
        yearLabelConstraints.gridy = 0;
        yearLabelConstraints.weightx = 1;
        yearLabelConstraints.anchor = GridBagConstraints.WEST;
        yearLabelConstraints.insets = new Insets(20, 25, 10, 10);
        inputPanel.add(yearLabel, yearLabelConstraints);

        this.yearTextField = new JFormattedTextField();
        this.yearTextField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
        this.yearTextField.setFont(FontResources.DEFAULT_FONT);
        this.yearTextField.setColumns(10);
        this.yearTextField.addFocusListener(new UpdateInstructionsFocusListener(BBCTStringResources.InstructionResources.CARD_YEAR_INSTRUCTIONS));

        GridBagConstraints yearTextFieldConstraints = new GridBagConstraints();
        yearTextFieldConstraints.gridx = 1;
        yearTextFieldConstraints.gridy = 0;
        yearTextFieldConstraints.weightx = 4;
        yearTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        yearTextFieldConstraints.insets = new Insets(20, 10, 10, 25);
        inputPanel.add(this.yearTextField, yearTextFieldConstraints);

        JLabel cardNumberLabel = new JLabel(BBCTStringResources.LabelResources.CARD_NUMBER_LABEL);
        cardNumberLabel.setFont(FontResources.DEFAULT_FONT);

        GridBagConstraints numberLabelConstraints = new GridBagConstraints();
        numberLabelConstraints.gridx = 0;
        numberLabelConstraints.gridy = 1;
        numberLabelConstraints.weightx = 1;
        numberLabelConstraints.anchor = GridBagConstraints.WEST;
        numberLabelConstraints.insets = new Insets(10, 25, 0, 10);
        inputPanel.add(cardNumberLabel, numberLabelConstraints);

        this.numberTextField = new JFormattedTextField();
        this.numberTextField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
        this.numberTextField.setFont(FontResources.DEFAULT_FONT);
        this.numberTextField.setColumns(10);
        this.numberTextField.addFocusListener(new UpdateInstructionsFocusListener(BBCTStringResources.InstructionResources.CARD_NUMBER_INSTRUCTIONS));

        GridBagConstraints numberTextFieldConstraints = new GridBagConstraints();
        numberTextFieldConstraints.gridx = 1;
        numberTextFieldConstraints.gridy = 1;
        numberTextFieldConstraints.weightx = 4;
        numberTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        numberTextFieldConstraints.insets = new Insets(10, 10, 0, 25);
        inputPanel.add(this.numberTextField, numberTextFieldConstraints);

        this.add(inputPanel, BorderLayout.PAGE_START);
        this.addAncestorListener(new UpdateTitleAncestorListener(BBCTStringResources.TitleResources.FIND_CARDS_BY_YEAR_AND_NUMBER_PANEL_TITLE));
    }

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
