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

import bbct.swing.BBCTStringResources;
import bbct.swing.FontResources;
import bbct.swing.gui.event.SetDefaultButtonAncestorListener;
import bbct.swing.gui.event.ShowCardActionListener;
import bbct.swing.gui.event.UpdateInstructionsAncestorListener;
import bbct.swing.gui.event.UpdateTitleAncestorListener;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

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

        JButton findCardsByYearButton = new JButton(BBCTStringResources.ButtonResources.FIND_CARDS_BY_YEAR_BUTTON);
        findCardsByYearButton.setFont(FontResources.BUTTON_FONT);
        findCardsByYearButton.addActionListener(new ShowCardActionListener(this, BBCTFrame.FIND_CARDS_BY_YEAR_CARD_NAME));
        this.add(findCardsByYearButton);

        JButton findCardsByNumberButton = new JButton(BBCTStringResources.ButtonResources.FIND_CARDS_BY_NUMBER_BUTTON);
        findCardsByNumberButton.setFont(FontResources.BUTTON_FONT);
        findCardsByNumberButton.addActionListener(new ShowCardActionListener(this, BBCTFrame.FIND_CARDS_BY_NUMBER_CARD_NAME));
        this.add(findCardsByNumberButton);

        JButton findCardsByYearAndNumberButton = new JButton(BBCTStringResources.ButtonResources.FIND_CARDS_BY_YEAR_AND_NUMBER_BUTTON);
        findCardsByYearAndNumberButton.setFont(FontResources.BUTTON_FONT);
        findCardsByYearAndNumberButton.addActionListener(new ShowCardActionListener(this, BBCTFrame.FIND_CARDS_BY_YEAR_AND_NUMBER_CARD_NAME));
        this.add(findCardsByYearAndNumberButton);

        JButton findCardsByPlayerNameButton = new JButton(BBCTStringResources.ButtonResources.FIND_CARDS_BY_PLAYER_NAME_BUTTON);
        findCardsByPlayerNameButton.setFont(FontResources.BUTTON_FONT);
        findCardsByPlayerNameButton.addActionListener(new ShowCardActionListener(this, BBCTFrame.FIND_CARDS_BY_PLAYER_NAME_CARD_NAME));
        this.add(findCardsByPlayerNameButton);

        JButton backButton = new JButton(BBCTStringResources.ButtonResources.BACK_BUTTON);
        backButton.setFont(FontResources.BUTTON_FONT);
        backButton.addActionListener(new ShowCardActionListener(this, BBCTFrame.MENU_CARD_NAME));
        this.add(backButton);

        this.addAncestorListener(new UpdateTitleAncestorListener(BBCTStringResources.TitleResources.FIND_CARDS_MENU_PANEL_TITLE));
        this.addAncestorListener(new UpdateInstructionsAncestorListener(BBCTStringResources.InstructionResources.FIND_CARDS_MENU_INSTRUCTIONS));
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
