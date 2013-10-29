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
import bbct.common.data.BaseballCardIO;
import bbct.common.exceptions.BBCTIOException;
import bbct.common.exceptions.InputException;
import bbct.swing.BBCTStringResources;
import bbct.swing.FontResources;
import bbct.swing.gui.event.UpdateInstructionsFocusListener;
import bbct.swing.gui.event.UpdateTitleAncestorListener;
import bbct.swing.gui.inputverifiers.PositiveIntegerInputVerifier;
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
 * {@link FindCardsByNumberPanel} allows the user to input a card number. This
 * value is used as the parameters when searching the underlying storage
 * mechanism for cards with the given number.
 */
public class FindCardsByNumberPanel extends FindCardsByPanel {

    /**
     * Creates a new {@link FindCardsByNumberPanel}.
     *
     * @param bcio The {@link BaseballCardIO} object which is used to search for
     * baseball cards with number input by the user.
     */
    public FindCardsByNumberPanel(BaseballCardIO bcio) {
        this.bcio = bcio;
        this.initComponents();
    }

    /**
     * Searches the underlying storage mechanism for baseball card records which
     * have the number given in the text field. The method also checks that the
     * input is valid: i.e. that it is a positive integer.
     *
     * @return A list of {@link BaseballCard}s which have the number given by
     * the user.
     * @throws BBCTIOException If there is an error reading the underlying
     * storage mechanism.
     * @throws InputException If the input is invalid.
     */
    @Override
    protected List<BaseballCard> getBaseballCards() throws BBCTIOException, InputException {
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

        return this.bcio.getBaseballCardsByNumber(number);
    }

    @Override
    protected void setFocus() {
        // TODO: Is this the correct place to clear the text field?
        this.numberTextField.setText("");
        this.numberTextField.requestFocusInWindow();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());

        JLabel numberLabel = new JLabel(BBCTStringResources.LabelResources.CARD_NUMBER_LABEL);
        numberLabel.setFont(FontResources.DEFAULT_FONT);

        GridBagConstraints numberLabelConstraints = new GridBagConstraints();
        numberLabelConstraints.gridx = 0;
        numberLabelConstraints.gridy = 0;
        numberLabelConstraints.weightx = 1;
        numberLabelConstraints.anchor = GridBagConstraints.WEST;
        numberLabelConstraints.insets = new Insets(20, 25, 0, 10);
        inputPanel.add(numberLabel, numberLabelConstraints);

        this.numberTextField = new JFormattedTextField();
        this.numberTextField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
        this.numberTextField.setFont(FontResources.DEFAULT_FONT);
        this.numberTextField.setColumns(10);
        this.numberTextField.addFocusListener(new UpdateInstructionsFocusListener(BBCTStringResources.InstructionResources.CARD_NUMBER_INSTRUCTIONS));

        GridBagConstraints numberTextFieldConstraints = new GridBagConstraints();
        numberTextFieldConstraints.gridx = 1;
        numberTextFieldConstraints.gridy = 0;
        numberTextFieldConstraints.weightx = 4;
        numberTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        numberTextFieldConstraints.insets = new Insets(20, 10, 0, 25);
        inputPanel.add(this.numberTextField, numberTextFieldConstraints);

        this.add(inputPanel, BorderLayout.PAGE_START);
        this.addAncestorListener(new UpdateTitleAncestorListener(BBCTStringResources.TitleResources.FIND_CARDS_BY_NUMBER_PANEL_TITLE));
    }
    private JFormattedTextField numberTextField;
    private BaseballCardIO bcio = null;
    private InputVerifier numberVerifier = new PositiveIntegerInputVerifier();

    /**
     * This is a test function for {@link FindCardsByNumberPanel}. It simply
     * creates a {@link javax.swing.JFrame} in which to display the panel.
     *
     * @param args Command-line arguments. (ignored)
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("FindCardsByNumberPanel Test");
        frame.add(new FindCardsByNumberPanel(null));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 425);
        frame.setVisible(true);
    }
}
