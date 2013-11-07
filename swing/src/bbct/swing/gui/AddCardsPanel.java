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
import bbct.swing.gui.event.SetDefaultButtonAncestorListener;
import bbct.swing.gui.event.ShowCardActionListener;
import bbct.swing.gui.event.UpdateTitleAncestorListener;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
@SuppressWarnings("serial")
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
        this.add(cardDetailsPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();

        final JButton addCardButton = new JButton(BBCTStringResources.ButtonResources.ADD_CARD_BUTTON);
        addCardButton.setFont(FontResources.BUTTON_FONT);
        addCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    BaseballCard card = AddCardsPanel.this.cardDetailsPanel.getBaseballCard();
                    AddCardsPanel.this.bcio.insertBaseballCard(card);
                    AddCardsPanel.this.cardDetailsPanel.reset();

                    BBCTFrame frame = (BBCTFrame) AddCardsPanel.this.getTopLevelAncestor();
                    frame.setInstructions(BBCTStringResources.InstructionResources.CARD_ADDED_INSTRUCTIONS);
                } catch (BBCTIOException ex) {
                    Logger.getLogger(AddCardsPanel.class.getName()).log(Level.SEVERE, "Storage I/O error.", ex);
                    JOptionPane.showMessageDialog(AddCardsPanel.this, ex.getMessage(), ex.getMessage(), JOptionPane.ERROR_MESSAGE);
                } catch (InputException ex) {
                    Logger.getLogger(AddCardsPanel.class.getName()).log(Level.INFO, "Invalid input", ex);
                    JOptionPane.showMessageDialog(AddCardsPanel.this, ex.getMessage(), BBCTStringResources.ErrorResources.INPUT_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    Logger.getLogger(AddCardsPanel.class.getName()).log(Level.SEVERE, "Unexpected exception", ex);
                    JOptionPane.showMessageDialog(AddCardsPanel.this, ex, BBCTStringResources.ErrorResources.UNEXPECTED_EXCEPTION_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonsPanel.add(addCardButton);

        JButton backButton = new JButton(BBCTStringResources.ButtonResources.BACK_BUTTON);
        backButton.setFont(FontResources.BUTTON_FONT);
        backButton.addActionListener(new ShowCardActionListener(this, BBCTFrame.MENU_CARD_NAME));
        buttonsPanel.add(backButton);

        this.add(buttonsPanel, BorderLayout.SOUTH);

        this.addAncestorListener(new UpdateTitleAncestorListener(BBCTStringResources.TitleResources.ADD_CARDS_PANEL_TITLE));
        this.addAncestorListener(new SetDefaultButtonAncestorListener(addCardButton));
    }
    private CardDetailsPanel cardDetailsPanel;
    private BaseballCardIO bcio = null;

    /**
     * Tests for {@link AddCardsPanel}. Simply creates a
     * {@link javax.swing.JFrame} in which to display it.
     *
     * @param args Command-line arguments. (ignored)
     */
    public static void main(String[] args) {
        JFrame f = new JFrame("AddCardsPanel Test");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new AddCardsPanel(null));
        f.pack();
        f.setVisible(true);
    }
}
