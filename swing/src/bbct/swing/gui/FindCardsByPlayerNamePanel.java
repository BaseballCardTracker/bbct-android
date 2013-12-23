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
import bbct.swing.gui.inputverifiers.NotEmptyInputVerifier;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.InputVerifier;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * {@link FindCardsByPlayerNamePanel} allows the user to input the player's
 * name. This value is used as the parameters when searching the underlying
 * storage mechanism for cards for the player.
 *
 * TODO: Add support for wild cards.
 */
@SuppressWarnings("serial")
public class FindCardsByPlayerNamePanel extends FindCardsByPanel {

    /**
     * Creates a new {@link FindCardsByPlayerNamePanel}.
     *
     * @param bcio The {@link BaseballCardIO} object which is used to search for
     * baseball cards with player name input by the user.
     */
    public FindCardsByPlayerNamePanel(BaseballCardIO bcio) {
        this.bcio = bcio;
        this.initComponents();
    }

    @Override
    protected List<BaseballCard> getBaseballCards() throws BBCTIOException, InputException {
        this.playerNameTextField.selectAll();
        this.playerNameTextField.requestFocusInWindow();
        if (!this.notEmptyVerifier.verify(this.playerNameTextField)) {
            throw new InputException(BBCTStringResources.ErrorResources.PLAYER_NAME_ERROR);
        }
        String playerName = this.playerNameTextField.getText();

        return this.bcio.getBaseballCardsByPlayerName(playerName);
    }

    @Override
    protected void setFocus() {
        // TODO: Is this the correct place to clear the text field?
        this.playerNameTextField.setText("");
        this.playerNameTextField.requestFocusInWindow();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());

        JLabel playerNameLabel = new JLabel(BBCTStringResources.LabelResources.PLAYER_NAME_LABEL);
        playerNameLabel.setFont(FontResources.DEFAULT_FONT);

        GridBagConstraints playerNameLabelConstraints = new GridBagConstraints();
        playerNameLabelConstraints.gridx = 0;
        playerNameLabelConstraints.gridy = 0;
        playerNameLabelConstraints.weightx = 1;
        playerNameLabelConstraints.anchor = GridBagConstraints.WEST;
        playerNameLabelConstraints.insets = new Insets(20, 25, 0, 10);
        inputPanel.add(playerNameLabel, playerNameLabelConstraints);

        this.playerNameTextField = new JTextField();
        this.playerNameTextField.setFont(FontResources.DEFAULT_FONT);
        this.playerNameTextField.setColumns(10);
        this.playerNameTextField.addFocusListener(new UpdateInstructionsFocusListener(BBCTStringResources.InstructionResources.PLAYER_NAME_INSTRUCTIONS));

        GridBagConstraints playerNameTextFieldConstraints = new GridBagConstraints();
        playerNameTextFieldConstraints.gridx = 1;
        playerNameTextFieldConstraints.gridy = 0;
        playerNameTextFieldConstraints.weightx = 4;
        playerNameTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        playerNameTextFieldConstraints.insets = new Insets(20, 10, 0, 25);
        inputPanel.add(this.playerNameTextField, playerNameTextFieldConstraints);

        this.add(inputPanel, BorderLayout.PAGE_START);
        this.addAncestorListener(new UpdateTitleAncestorListener(BBCTStringResources.TitleResources.FIND_CARDS_BY_PLAYER_NAME_PANEL_TITLE));
    }
    private JTextField playerNameTextField;
    private BaseballCardIO bcio = null;
    private InputVerifier notEmptyVerifier = new NotEmptyInputVerifier();

    /**
     * This is a test function for {@link FindCardsByPlayerNamePanel}. It simply
     * creates a {@link javax.swing.JFrame} in which to display the panel.
     *
     * @param args Command-line arguments. (ignored)
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("FindCardsByPlayerNamePanel Test");
        frame.add(new FindCardsByPlayerNamePanel(null));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 425);
        frame.setVisible(true);
    }
}
