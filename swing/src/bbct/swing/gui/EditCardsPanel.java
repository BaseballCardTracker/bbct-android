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
import bbct.swing.gui.event.SetDefaultButtonAncestorListener;
import bbct.swing.gui.event.UpdateTitleAncestorListener;
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

/**
 * {@link EditCardsPanel} allows the user to edit the value and count of
 * {@link bbct.data.BaseballCard} objects. It uses one {@link CardDetailsPanel}
 * for each {@link bbct.data.BaseballCard}.
 *
 * TODO: What if 1,000 cards match a search? Scrolling through one at a time
 * will be painful!
 *
 * TODO: Enable/disable the "Previous" and "Next" buttons if there is only one card?
 * 
 * TODO: Uneditable text fields should not be in the focus order when tabbing.
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
        this.setLayout(new BorderLayout());

        JPanel buttonsPanel = new JPanel();

        JButton prevButton = new JButton(BBCTStringResources.ButtonResources.PREVIOUS_BUTTON);
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                CardLayout layout = (CardLayout) EditCardsPanel.this.allCardDetailsPanel.getLayout();
                layout.previous(EditCardsPanel.this.allCardDetailsPanel);
            }
        });
        buttonsPanel.add(prevButton);

        JButton nextButton = new JButton(BBCTStringResources.ButtonResources.NEXT_BUTTON);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                CardLayout layout = (CardLayout) EditCardsPanel.this.allCardDetailsPanel.getLayout();
                layout.next(EditCardsPanel.this.allCardDetailsPanel);
            }
        });
        buttonsPanel.add(nextButton);

        final JButton doneButton = new JButton(BBCTStringResources.ButtonResources.DONE_BUTTON);
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
                        JOptionPane.showMessageDialog(EditCardsPanel.this, ex.getMessage(), BBCTStringResources.ErrorResources.IO_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                    }

                    Container parent = EditCardsPanel.this.getParent();
                    CardLayout layout = (CardLayout) parent.getLayout();

                    parent.remove(EditCardsPanel.this);
                    layout.show(parent, BBCTFrame.FIND_CARDS_MENU_CARD_NAME);
                } catch (InputException ex) {
                    Logger.getLogger(EditCardsPanel.class.getName()).log(Level.INFO, "Invalid input.", ex);
                    JOptionPane.showMessageDialog(EditCardsPanel.this, ex.getMessage(), BBCTStringResources.ErrorResources.INPUT_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonsPanel.add(doneButton);

        this.add(buttonsPanel, BorderLayout.SOUTH);

        this.allCardDetailsPanel = new JPanel();
        this.allCardDetailsPanel.setLayout(new CardLayout());
        this.add(allCardDetailsPanel, BorderLayout.CENTER);

        this.addAncestorListener(new UpdateTitleAncestorListener(BBCTStringResources.TitleResources.EDIT_CARD_PANEL_TITLE));
        this.addAncestorListener(new SetDefaultButtonAncestorListener(doneButton));
    }

    private JPanel allCardDetailsPanel;
    BaseballCardIO bcio = null;
}
