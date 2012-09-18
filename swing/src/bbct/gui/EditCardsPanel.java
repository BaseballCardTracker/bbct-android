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
import bbct.exceptions.BBCTIOException;
import bbct.exceptions.InputException;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * {@link EditCardsPanel} allows the user to edit the value and count of
 * {@link bbct.data.BaseballCard} objects. It uses one {@link CardDetailsPanel}
 * for each {@link bbct.data.BaseballCard}.
 *
 * TODO: What if 1,000 cards match a search? Scrolling through one at a time
 * will be painful!
 *
 * TODO: Enable/disable the "Previous" and "Next" buttons depending on whether
 * or not card details are available for display in the given direction.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class EditCardsPanel extends JPanel {

    /**
     * Creates a new {@link EditCardsPanel} with a {@link CardDetailsPanel} for
     * each card in the given list.
     *
     * @param cards The list of cards to be edited.
     * @param bcio The {@link BaseballCardIO} object which is used to update
     * data in the underlying persistent storage mechanism.
     */
    public EditCardsPanel(List<BaseballCard> cards, BaseballCardIO bcio) {
        this.bcio = bcio;

        this.initComponents();

        for (BaseballCard card : cards) {
            JPanel cardDetailsPanel = new CardDetailsPanel(card, false);

            this.allCardDetailsPanel.add(cardDetailsPanel);
        }
    }

    private void initComponents() {

        JPanel buttonsPanel = new JPanel();

        this.setLayout(new BorderLayout());

        JButton prevButton = new JButton("<-- Previous");
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                CardLayout layout = (CardLayout) EditCardsPanel.this.allCardDetailsPanel.getLayout();
                layout.previous(EditCardsPanel.this.allCardDetailsPanel);
            }
        });
        buttonsPanel.add(prevButton);

        JButton nextButton = new JButton("Next -->");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                CardLayout layout = (CardLayout) EditCardsPanel.this.allCardDetailsPanel.getLayout();
                layout.next(EditCardsPanel.this.allCardDetailsPanel);
            }
        });
        buttonsPanel.add(nextButton);

        final JButton doneButton = new JButton("Done");
        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    List<BaseballCard> cards = new ArrayList<>();

                    synchronized (EditCardsPanel.this.getTreeLock()) {
                        Component[] panels = EditCardsPanel.this.allCardDetailsPanel.getComponents();

                        for (Component panel : panels) {
                            cards.add(((CardDetailsPanel) panel).getBaseballCard());
                        }
                    }

                    try {
                        EditCardsPanel.this.bcio.updateCards(cards);
                    } catch (BBCTIOException ex) {
                        Logger.getLogger(EditCardsPanel.class.getName()).log(Level.SEVERE, "Unable to update baseball card data.", ex);
                        JOptionPane.showMessageDialog(EditCardsPanel.this, ex.getMessage(), "I/O Error", JOptionPane.ERROR_MESSAGE);
                    }

                    Container parent = EditCardsPanel.this.getParent();
                    CardLayout layout = (CardLayout) parent.getLayout();

                    parent.remove(EditCardsPanel.this);
                    layout.show(parent, BBCTFrame.FIND_CARDS_MENU_CARD_NAME);
                } catch (InputException ex) {
                    Logger.getLogger(EditCardsPanel.class.getName()).log(Level.INFO, "Invalid input.", ex);
                    JOptionPane.showMessageDialog(EditCardsPanel.this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonsPanel.add(doneButton);

        this.add(buttonsPanel, BorderLayout.SOUTH);

        this.allCardDetailsPanel = new JPanel();
        this.allCardDetailsPanel.setLayout(new CardLayout());
        this.add(allCardDetailsPanel, BorderLayout.CENTER);

        this.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorMoved(AncestorEvent evt) {
            }

            @Override
            public void ancestorAdded(AncestorEvent evt) {
                Container topLevelAncestor = EditCardsPanel.this.getTopLevelAncestor();

                if (topLevelAncestor instanceof BBCTFrame) {
                    BBCTFrame frame = (BBCTFrame) topLevelAncestor;
                    frame.setTitle(GUIResources.EDIT_CARD_PANEL_TITLE);
                    frame.setDefaultButton(doneButton);
                }
            }

            @Override
            public void ancestorRemoved(AncestorEvent evt) {
            }
        });
    }

    private JPanel allCardDetailsPanel;
    BaseballCardIO bcio = null;
}
