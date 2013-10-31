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
 * {@link FindCardsByYearPanel} allows the user to input a card year. This value
 * is used as the parameters when searching the underlying storage mechanism for
 * cards with the given year.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
@SuppressWarnings("serial")
public class FindCardsByYearPanel extends FindCardsByPanel {

    /**
     * Creates a new {@link FindCardsByYearPanel}.
     *
     * @param bcio The {@link BaseballCardIO} object which is used to search for
     * baseball cards with the year input by the user.
     */
    public FindCardsByYearPanel(BaseballCardIO bcio) {
        this.bcio = bcio;
        this.initComponents();
    }

    /**
     * Searches the underlying storage mechanism for baseball card records from
     * the year given in the text field. The method also checks that the input
     * is valid: i.e. that the year is a positive, four-digit integer.
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

        return this.bcio.getBaseballCardsByYear(year);
    }

    @Override
    protected void setFocus() {
        // TODO: Is this the correct place to clear the text field?
        this.yearTextField.setText("");
        this.yearTextField.requestFocusInWindow();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());

        JLabel yearLabel = new JLabel("Card Year:");
        yearLabel.setFont(FontResources.DEFAULT_FONT);

        GridBagConstraints yearLabelConstraints = new GridBagConstraints();
        yearLabelConstraints.gridx = 0;
        yearLabelConstraints.gridy = 0;
        yearLabelConstraints.weightx = 1;
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

        this.add(inputPanel, BorderLayout.PAGE_START);
        this.addAncestorListener(new UpdateTitleAncestorListener(BBCTStringResources.TitleResources.FIND_CARDS_BY_YEAR_PANEL_TITLE));
    }
    private JFormattedTextField yearTextField;
    private BaseballCardIO bcio = null;
    private InputVerifier yearVerifier = new YearInputVerifier();

    /**
     * This is a test function for {@link FindCardsByYearPanel}. It simply
     * creates a {@link javax.swing.JFrame} in which to display the panel.
     *
     * @param args Command-line arguments. (ignored)
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("FindCardsByYearPanel Test");
        frame.add(new FindCardsByYearPanel(null));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 425);
        frame.setVisible(true);
    }
}
