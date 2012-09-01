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
 * {@link FindCardsByYearPanel} allows the user to input a card year. This
 * value is used as the parameters when searching the underlying storage
 * mechanism for cards with the given year.
 *
 * TODO: yearTextFiled needs to be limited to 4 digits
 *
 * TODO: Tweak component placement and size
 *
 * TODO: Error handling.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class FindCardsByYearPanel extends FindCardsByPanel {

    /**
     * Creates new {@link FindCardsByYearPanel}.
     *
     * @param bcio The {@link BaseballCardIO} object which is used to search for
     * baseball cards with the year input by the user.
     */
    public FindCardsByYearPanel(BaseballCardIO bcio) {
        this.bcio = bcio;
        this.initComponents();
    }

    /**
     *
     * @return @throws IOException
     */
    @Override
    protected List<BaseballCard> getBaseballCards() throws IOException {
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

        this.add(inputPanel, BorderLayout.PAGE_START);
        this.addAncestorListener(new UpdateTitleAncestorListener(GUIResources.FIND_CARDS_BY_YEAR_PANEL_TITLE));
    }
    private JFormattedTextField yearTextField;
    private BaseballCardIO bcio = null;

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
