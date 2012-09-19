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
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * {@link FindCardsMenuPanel} has four buttons which allow the user to choose
 * which criteria to use when searching for baseball card data. The four options
 * are "Find Cards by Year", "Find Cards by Number", "Find Cards by Year and
 * Number", and "Find Cards by Player Name".
 *
 * @see FindCardsByYearPanel
 * @see FindCardsByNumberPanel
 * @see FindCardsByYearAndNumberPanel
 * @see FindCardsByPlayerNamePanel
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class FindCardsMenuPanel extends JPanel {

    /**
     * Creates a new {@link FindCardsMenuPanel}.
     */
    public FindCardsMenuPanel() {
        this.initComponents();
    }

    private void initComponents() {
        this.setLayout(new GridLayout(5, 1, 0, 30));
        this.setBorder(BorderFactory.createEmptyBorder(40, 70, 40, 70));
        
        JButton findCardsByYearButton = new JButton("Find Cards By Year");
        findCardsByYearButton.setFont(new Font("Tahoma", 0, 16)); // NOI18N
        findCardsByYearButton.addActionListener(new ShowCardActionListener(this, BBCTFrame.FIND_CARDS_BY_YEAR_CARD_NAME));
        this.add(findCardsByYearButton);

        JButton findCardsByNumberButton = new JButton("Find Cards By Number");
        findCardsByNumberButton.setFont(new Font("Tahoma", 0, 16)); // NOI18N
        findCardsByNumberButton.addActionListener(new ShowCardActionListener(this, BBCTFrame.FIND_CARDS_BY_NUMBER_CARD_NAME));
        this.add(findCardsByNumberButton);

        JButton findCardsByYearAndNumberButton = new JButton("Find Cards By Year and Number");
        findCardsByYearAndNumberButton.setFont(new Font("Tahoma", 0, 16)); // NOI18N
        findCardsByYearAndNumberButton.addActionListener(new ShowCardActionListener(this, BBCTFrame.FIND_CARDS_BY_YEAR_AND_NUMBER_CARD_NAME));
        this.add(findCardsByYearAndNumberButton);

        JButton findCardsByPlayerNameButton = new JButton("Find Cards By Player Name");
        findCardsByPlayerNameButton.setFont(new Font("Tahoma", 0, 16)); // NOI18N
        findCardsByPlayerNameButton.addActionListener(new ShowCardActionListener(this, BBCTFrame.FIND_CARDS_BY_PLAYER_NAME_CARD_NAME));
        this.add(findCardsByPlayerNameButton);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Tahoma", 0, 16)); // NOI18N
        backButton.addActionListener(new ShowCardActionListener(this, BBCTFrame.MENU_CARD_NAME));
        this.add(backButton);
        
        this.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorMoved(AncestorEvent evt) {
            }

            @Override
            public void ancestorAdded(AncestorEvent evt) {
                Container topLevelAncestor = FindCardsMenuPanel.this.getTopLevelAncestor();

                if (topLevelAncestor instanceof BBCTFrame) {
                    BBCTFrame frame = (BBCTFrame) topLevelAncestor;
                    frame.setTitle(GUIResources.FIND_CARDS_MENU_PANEL_TITLE);
                    frame.setInstructions("Chose an option:");
                }
            }

            @Override
            public void ancestorRemoved(AncestorEvent evt) {
            }
        });
        
        this.addAncestorListener(new SetDefaultButtonAncestorListener(null));
    }

    /**
     * Tests for {@link FindCardsMenuPanel}. Simply creates a
     * {@link javax.swing.JFrame} in which to display it.
     *
     * @param args Command-line arguments. (ignored)
     */
    public static void main(String[] args) {
        JFrame f = new JFrame("FindCardsMenuPanel Test");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new FindCardsMenuPanel());
        f.pack();
        f.setVisible(true);
    }
}
