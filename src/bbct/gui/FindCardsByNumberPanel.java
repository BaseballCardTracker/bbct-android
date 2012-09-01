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
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
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
 *
 * TODO: Error handling.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class FindCardsByNumberPanel extends FindCardsByPanel {

    /**
     * Creates new {@link FindCardsByNumberPanel}.
     *
     * @param bcio The {@link BaseballCardIO} object which is used to search for
     * baseball cards with number input by the user.
     */
    public FindCardsByNumberPanel(BaseballCardIO bcio) {
        this.bcio = bcio;
        initComponents();
    }

    /**
     *
     * @return @throws IOException
     */
    @Override
    protected List<BaseballCard> getBaseballCards() throws IOException {
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

        JLabel numberLabel = new JLabel("Card Number:");
        numberLabel.setFont(new Font("Tahoma", 0, 14)); // NOI18N

        GridBagConstraints numberLabelConstraints = new GridBagConstraints();
        numberLabelConstraints.gridx = 0;
        numberLabelConstraints.gridy = 0;
        numberLabelConstraints.weightx = 1;
        numberLabelConstraints.weighty = 1;
        numberLabelConstraints.anchor = GridBagConstraints.WEST;
        numberLabelConstraints.insets = new Insets(20, 25, 0, 10);
        inputPanel.add(numberLabel, numberLabelConstraints);

        this.numberTextField = new JFormattedTextField();
        this.numberTextField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
        this.numberTextField.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        this.numberTextField.setColumns(10);
        this.numberTextField.addFocusListener(new UpdateInstructionsFocusListener("Enter card number."));

        GridBagConstraints numberTextFieldConstraints = new GridBagConstraints();
        numberTextFieldConstraints.gridx = 1;
        numberTextFieldConstraints.gridy = 0;
        numberTextFieldConstraints.weightx = 2;
        numberTextFieldConstraints.weighty = 1;
        numberTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        numberTextFieldConstraints.insets = new Insets(20, 10, 0, 25);
        inputPanel.add(this.numberTextField, numberTextFieldConstraints);

        this.add(inputPanel, BorderLayout.PAGE_START);
        this.addAncestorListener(new UpdateTitleAncestorListener(GUIResources.FIND_CARDS_BY_NUMBER_PANEL_TITLE));
    }
    private JFormattedTextField numberTextField;
    private BaseballCardIO bcio = null;

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
