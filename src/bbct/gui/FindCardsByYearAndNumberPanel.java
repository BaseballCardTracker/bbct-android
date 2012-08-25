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
import bbct.exceptions.IOException;
import bbct.gui.event.UpdateInstructionsFocusListener;
import bbct.gui.event.UpdateTitleAncestorListener;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

/**
 * {@link FindCardsByYearAndNumberPanel} contains two JLabels and two
 * JFormattedTextFields which take integer input. These values are used as the
 * baseball card number and year when searching the underlying storage mechanism
 * for cards which match the user's input.
 *
 * TODO: Tweak component placement and size
 *
 * TODO: yearTextFiled needs to be limited to 4 digits
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class FindCardsByYearAndNumberPanel extends FindCardsByPanel {

    /**
     * Creates new {@link FindCardsByYearAndNumberPanel} with two JLabels and
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

    @Override
    protected List<BaseballCard> getBaseballCards() throws IOException {
        int year = Integer.parseInt(this.yearTextField.getText());
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
        // TODO: GridLayout?
        this.setLayout(new FlowLayout());

        JLabel cardYearLabel = new JLabel("Card Year:");
        cardYearLabel.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        this.add(cardYearLabel);

        this.yearTextField = new JFormattedTextField();
        this.yearTextField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
        this.yearTextField.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        this.yearTextField.setColumns(10);
        this.yearTextField.addFocusListener(new UpdateInstructionsFocusListener("Enter card year."));
        this.add(this.yearTextField);

        JLabel cardNumberLabel = new JLabel("Card Number:");
        cardNumberLabel.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        this.add(cardNumberLabel);

        this.numberTextField = new JFormattedTextField();
        this.numberTextField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
        this.numberTextField.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        this.numberTextField.setColumns(10);
        this.numberTextField.addFocusListener(new UpdateInstructionsFocusListener("Enter card number."));
        this.add(this.numberTextField);

        this.addAncestorListener(new UpdateTitleAncestorListener(GUIResources.FIND_CARDS_BY_YEAR_AND_NUMBER_PANEL_TITLE));
    }
    private JFormattedTextField numberTextField;
    private JFormattedTextField yearTextField;
    private BaseballCardIO bcio = null;

    /**
     * This is a test function for {@link FindCardsByYearAndNumberPanel}. It
     * simply creates a {@link javax.swing.JFrame} in which to display the
     * panel.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("FindCardsByNumberPanel Test");
        frame.add(new FindCardsByYearAndNumberPanel(null));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
