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

import bbct.gui.event.SetDefaultButtonAncestorListener;
import bbct.gui.event.ShowCardActionListener;
import bbct.gui.event.UpdateInstructionsAncestorListener;
import bbct.gui.event.UpdateTitleAncestorListener;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * {@link MenuPanel} contains buttons that specify the primary actions a user
 * can take in the BBCT application. These are Add Cards, Find Cards, About
 * BBCT, and Exit.
 *
 * @see AddCardsPanel
 * @see FindCardsMenuPanel
 * @see AboutPanel
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class MenuPanel extends JPanel {

    /**
     * Creates a new {@link MenuPanel}.
     */
    public MenuPanel() {
        this.initComponents();
    }

    private void initComponents() {
        this.setLayout(new GridLayout(4, 1, 0, 30));
        this.setBorder(BorderFactory.createEmptyBorder(75, 110, 75, 110));

        JButton addCardsButton = new JButton("Add Cards");
        addCardsButton.setFont(new Font("Tahoma", 0, 24)); // NOI18N
        addCardsButton.addActionListener(new ShowCardActionListener(this, BBCTFrame.ADD_CARDS_CARD_NAME));
        this.add(addCardsButton);

        JButton findCardsButton = new JButton("Find Cards");
        findCardsButton.setFont(new Font("Tahoma", 0, 24)); // NOI18N
        findCardsButton.addActionListener(new ShowCardActionListener(this, BBCTFrame.FIND_CARDS_MENU_CARD_NAME));
        this.add(findCardsButton);

        JButton aboutButton = new JButton("About");
        aboutButton.setFont(new Font("Tahoma", 0, 24)); // NOI18N
        aboutButton.addActionListener(new ShowCardActionListener(this, BBCTFrame.ABOUT_CARD_NAME));
        this.add(aboutButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Tahoma", 0, 24)); // NOI18N
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                System.exit(0);
            }
        });
        this.add(exitButton);

        this.addAncestorListener(new UpdateTitleAncestorListener(GUIResources.MAIN_PANEL_TITLE));
        this.addAncestorListener(new UpdateInstructionsAncestorListener("Choose an option:"));
        this.addAncestorListener(new SetDefaultButtonAncestorListener(null));
    }
}
