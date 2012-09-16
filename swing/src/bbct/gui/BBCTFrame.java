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

import bbct.data.BaseballCardIO;
import bbct.exceptions.BBCTIOException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * The main window for the BBCT application.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BBCTFrame extends JFrame {

    /**
     * Name used for {@link MainPanel} card.
     */
    public static final String MENU_CARD_NAME = "menu";
    /**
     * Name used for {@link AddCardsPanel} card.
     */
    public static final String ADD_CARDS_CARD_NAME = "addCards";
    /**
     * Name used for {@link FindCardsMenuPanel} card.
     */
    public static final String FIND_CARDS_MENU_CARD_NAME = "findCardsMenu";
    /**
     * Name used for {@link EditCardsPanel} card.
     */
    public static final String EDIT_CARDS_PANEL_NAME = "editCards";
    /**
     * Name used for {@link FindCardsByYearPanel} card.
     */
    public static final String FIND_CARDS_BY_YEAR_CARD_NAME = "findCardsByYear";
    /**
     * Name used for {@link FindCardsByNumberPanel} card.
     */
    public static final String FIND_CARDS_BY_NUMBER_CARD_NAME = "findCardsByNumber";
    /**
     * Name used for {@link FindCardsByYearAndNumberPanel} card.
     */
    public static final String FIND_CARDS_BY_YEAR_AND_NUMBER_CARD_NAME = "findCardsByYearAndNumber";
    /**
     * Name used for {@link FindCardsByPlayerNamePanel} card.
     */
    public static final String FIND_CARDS_BY_PLAYER_NAME_CARD_NAME = "findCardsByPlayerName";
    /**
     * Name used for {@link AboutPanel} card.
     */
    public static final String ABOUT_CARD_NAME = "about";

    /**
     * Creates a new {@link BBCTFrame}.
     *
     * @param bcio The connection to the underlying persistent storage
     * mechanism.
     */
    public BBCTFrame(BaseballCardIO bcio) {
        this.bcio = bcio;
        this.initComponents();
    }

    /**
     * Set instructions for the user shown at the top of the window.
     *
     * @param instructions The instructions to display to the user.
     */
    public void setInstructions(String instructions) {
        this.instructionLabel.setText(instructions);
    }

    /**
     * Set the default button which will be activated when the Enter key is
     * pressed.
     *
     * @param button The button to set as the default.
     */
    public void setDefaultButton(JButton button) {
        this.getRootPane().setDefaultButton(button);
    }

    private void initComponents() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("Baseball Card Tracker");
        this.setResizable(false);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent evt) {
                try {
                    BBCTFrame.this.bcio.close();
                } catch (BBCTIOException ex) {
                    Logger.getLogger(BBCTFrame.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                    JOptionPane.showMessageDialog(BBCTFrame.this, ex.getMessage(), "Clean Up Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        this.mainPanel = new MainPanel(this.bcio);
        this.getContentPane().add(mainPanel, BorderLayout.CENTER);

        JPanel instructionPanel = new JPanel();
        this.instructionLabel = new JLabel("Just Starting!");
        this.instructionLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        instructionPanel.add(this.instructionLabel);

        this.getContentPane().add(instructionPanel, BorderLayout.NORTH);

        this.pack();

        // Center JFrame
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);

        this.setLocation(x, y);
    }
    private JLabel instructionLabel;
    private MainPanel mainPanel;
    private BaseballCardIO bcio = null;
}
