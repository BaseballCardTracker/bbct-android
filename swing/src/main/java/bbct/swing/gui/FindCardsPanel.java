/*
 * This file is part of BBCT.
 *
 * Copyright 2012-14 codeguru <codeguru@users.sourceforge.net>
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
import bbct.swing.gui.event.SetDefaultButtonAncestorListener;
import bbct.swing.gui.event.ShowCardActionListener;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * {@link FindCardsPanel} contains a {@link FindCardsByPanel} and two buttons,
 * "Find" and "Back". The {@link FindCardsByPanel} defines the criteria and
 * parameters used to find baseball card data in the underlying storage
 * mechanism. The "Find" button executes
 * {@link FindCardsByPanel#getBaseballCards()} and displays the returned data in
 * a {@link EditCardsPanel}. The "Back" button returns to the
 * {@link FindCardsMenuPanel}.
 */
@SuppressWarnings("serial")
public class FindCardsPanel extends JPanel {

    /**
     * Creates a new {@link FindCardsPanel}.
     *
     * @param bcio The {@link BaseballCardIO} object which is used to search for
     * baseball cards using the criteria input by the user.
     * @param inputPanel The panel containing input controls which vary
     * depending on the exact criteria used to search for cards.
     */
    public FindCardsPanel(BaseballCardIO bcio, FindCardsByPanel inputPanel) {
        this.bcio = bcio;
        this.inputPanel = inputPanel;
        this.initComponents();
    }

    private void initComponents() {
        this.setLayout(new java.awt.BorderLayout());

        this.add(this.inputPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();

        final JButton findButton = new JButton(BBCTStringResources.ButtonResources.FIND_BUTTON);
        findButton.setFont(FontResources.BUTTON_FONT);
        findButton.addActionListener(evt -> {
            try {
                List<BaseballCard> cards = FindCardsPanel.this.inputPanel.getBaseballCards();

                if (cards.isEmpty()) {
                    JOptionPane.showMessageDialog(FindCardsPanel.this, BBCTStringResources.ErrorResources.NO_CARDS_FOUND_ERROR, BBCTStringResources.ErrorResources.NO_CARDS_FOUND_ERROR_TITLE, JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JPanel editCardsPanel = new EditCardsPanel(cards, FindCardsPanel.this.bcio);
                    Container parent1 = FindCardsPanel.this.getParent();
                    CardLayout cl = (CardLayout) parent1.getLayout();

                    parent1.add(editCardsPanel, BBCTFrame.EDIT_CARDS_PANEL_NAME);
                    cl.show(parent1, BBCTFrame.EDIT_CARDS_PANEL_NAME);
                }
            } catch (InputException ex) {
                JOptionPane.showMessageDialog(FindCardsPanel.this, ex.getMessage(), BBCTStringResources.ErrorResources.INPUT_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(FindCardsPanel.class.getName()).log(Level.INFO, null, ex);
            } catch (BBCTIOException ex) {
                Logger.getLogger(FindCardsPanel.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                JOptionPane.showMessageDialog(FindCardsPanel.this, ex.getMessage(), BBCTStringResources.ErrorResources.IO_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonsPanel.add(findButton);

        JButton backButton = new JButton(BBCTStringResources.ButtonResources.BACK_BUTTON);
        backButton.setFont(FontResources.BUTTON_FONT);
        backButton.addActionListener(new ShowCardActionListener(this, BBCTFrame.FIND_CARDS_MENU_CARD_NAME));
        buttonsPanel.add(backButton);

        this.add(buttonsPanel, java.awt.BorderLayout.SOUTH);
        this.addAncestorListener(new SetDefaultButtonAncestorListener(findButton));
    }
    private BaseballCardIO bcio = null;
    private FindCardsByPanel inputPanel = null;
}
