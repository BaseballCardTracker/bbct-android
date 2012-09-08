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
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * {@link FindCardsByPlayerNamePanel} allows the user to input the player's
 * name. This value is used as the parameters when searching the underlying
 * storage mechanism for cards for the player.
 *
 * TODO: Error handling.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class FindCardsByPlayerNamePanel extends FindCardsByPanel {

    /**
     * Creates a new {@link FindCardsByPlayerNamePanel}.
     *
     * @param bcio The {@link BaseballCardIO} object which is used to search for
     * baseball cards with player name input by the user.
     */
    public FindCardsByPlayerNamePanel(BaseballCardIO bcio) {
        this.bcio = bcio;
        initComponents();
    }

    @Override
    protected List<BaseballCard> getBaseballCards() throws IOException {
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

        JLabel playerNameLabel = new JLabel("Player Name:");
        playerNameLabel.setFont(new Font("Tahoma", 0, 14)); // NOI18N

        GridBagConstraints playerNameLabelConstraints = new GridBagConstraints();
        playerNameLabelConstraints.gridx = 0;
        playerNameLabelConstraints.gridy = 0;
        playerNameLabelConstraints.weightx = 1;
        playerNameLabelConstraints.weighty = 1;
        playerNameLabelConstraints.anchor = GridBagConstraints.WEST;
        playerNameLabelConstraints.insets = new Insets(20, 25, 0, 10);
        inputPanel.add(playerNameLabel, playerNameLabelConstraints);

        this.playerNameTextField = new JTextField();
        this.playerNameTextField.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        this.playerNameTextField.setColumns(10);
        this.playerNameTextField.addFocusListener(new UpdateInstructionsFocusListener("Enter player name."));

        GridBagConstraints playerNameTextFieldConstraints = new GridBagConstraints();
        playerNameTextFieldConstraints.gridx = 1;
        playerNameTextFieldConstraints.gridy = 0;
        playerNameTextFieldConstraints.weightx = 2;
        playerNameTextFieldConstraints.weighty = 1;
        playerNameTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        playerNameTextFieldConstraints.insets = new Insets(20, 10, 0, 25);
        inputPanel.add(this.playerNameTextField, playerNameTextFieldConstraints);

        this.add(inputPanel, BorderLayout.PAGE_START);
        this.addAncestorListener(new UpdateTitleAncestorListener(GUIResources.FIND_CARDS_BY_PLAYER_NAME_PANEL_TITLE));
    }
    private JTextField playerNameTextField;
    private BaseballCardIO bcio = null;

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
