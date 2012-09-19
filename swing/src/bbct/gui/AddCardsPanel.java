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
import bbct.gui.event.ShowCardActionListener;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * This panel contains controls which allow the user to add data for baseball
 * cards to the underlying storage. The labels and text fields are delegated to
 * a [@link CardDetailsPanel}. This panel also contains buttons to add the
 * baseball card data to storage and to go back to the main menu of the BBCT
 * application.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 *
 * TODO: Instructions not correct after clicking Add Card button
 *
 */
public class AddCardsPanel extends JPanel {

    /**
     * Creates a new {@link AddCardsPanel}.
     *
     * @param bcio The {@link BaseballCardIO} object which is used to add
     * baseball card data to the underlying persistent storage mechanism.
     */
    public AddCardsPanel(BaseballCardIO bcio) {
        this.bcio = bcio;
        this.initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());

        this.cardDetailsPanel = new CardDetailsPanel(true);
        this.cardDetailsPanel.setMinimumSize(new Dimension(375, 500));
        this.cardDetailsPanel.setPreferredSize(new Dimension(375, 500));
        this.add(cardDetailsPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        
        final JButton addCardButton = new JButton("Add Card");
        addCardButton.setFont(new Font("Tahoma", 0, 16)); // NOI18N
        addCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    BaseballCard card = AddCardsPanel.this.cardDetailsPanel.getBaseballCard();
                    AddCardsPanel.this.bcio.insertBaseballCard(card);
                    AddCardsPanel.this.cardDetailsPanel.reset();

                    BBCTFrame frame = (BBCTFrame) AddCardsPanel.this.getTopLevelAncestor();
                    frame.setInstructions("Card added. Enter brand name for another card.");
                } catch (BBCTIOException ex) {
                    Logger.getLogger(AddCardsPanel.class.getName()).log(Level.SEVERE, "Storage I/O error.", ex);
                    JOptionPane.showMessageDialog(AddCardsPanel.this, ex.getMessage(), ex.getMessage(), JOptionPane.ERROR_MESSAGE);
                } catch (InputException ex) {
                    Logger.getLogger(AddCardsPanel.class.getName()).log(Level.INFO, "Invalid input", ex);
                    JOptionPane.showMessageDialog(AddCardsPanel.this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    Logger.getLogger(AddCardsPanel.class.getName()).log(Level.SEVERE, "Unexpected Exception", ex);
                    JOptionPane.showMessageDialog(AddCardsPanel.this, ex, "Unexpected Exception", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonsPanel.add(addCardButton);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Tahoma", 0, 16)); // NOI18N
        backButton.addActionListener(new ShowCardActionListener(this, BBCTFrame.MENU_CARD_NAME));
        buttonsPanel.add(backButton);

        this.add(buttonsPanel, BorderLayout.SOUTH);
        
        this.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorMoved(AncestorEvent evt) {
            }

            @Override
            public void ancestorAdded(AncestorEvent evt) {
                Container topLevelAncestor = AddCardsPanel.this.getTopLevelAncestor();

                if (topLevelAncestor instanceof BBCTFrame) {
                    BBCTFrame frame = (BBCTFrame) topLevelAncestor;
                    frame.setTitle(GUIResources.ADD_CARDS_PANEL_TITLE);
                    frame.setDefaultButton(addCardButton);
                }
            }

            @Override
            public void ancestorRemoved(AncestorEvent evt) {
            }
        });
    }
    private CardDetailsPanel cardDetailsPanel;
    private BaseballCardIO bcio = null;
}
