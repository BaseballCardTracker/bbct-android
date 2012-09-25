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
import bbct.common.data.JDBCBaseballCardIO;
import bbct.common.exceptions.BBCTIOException;
import bbct.swing.BBCTStringResources;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private void initComponents() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle(BBCTStringResources.TitleResources.BASEBALL_FRAME_TITLE);
        this.setResizable(false);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent evt) {
                try {
                    BBCTFrame.this.bcio.close();
                } catch (BBCTIOException ex) {
                    Logger.getLogger(BBCTFrame.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                    JOptionPane.showMessageDialog(BBCTFrame.this, ex.getMessage(), BBCTStringResources.ErrorResources.CLOSE_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        this.mainPanel = new MainPanel(this.bcio);
        this.getContentPane().add(mainPanel, BorderLayout.CENTER);

        JPanel instructionPanel = new JPanel();
        this.instructionLabel = new JLabel(BBCTStringResources.InstructionResources.DUMMY_INSTRUCTIONS);
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

    /**
     * Tests for {@link BBCTFrame}. Creates a
     * {@link bbct.common.data.JDBCBaseballCardIO} and populates it with data
     * before displaying the {@link BBCTFrame}.
     *
     * @param args Command-line arguments. (ignored)
     */
    public static void main(String[] args) throws BBCTIOException, FileNotFoundException, IOException {
        String db_url = "jdbc:hsqldb:mem:db/bbct.db";
        BaseballCardIO bbcio = new JDBCBaseballCardIO(db_url);
        BBCTFrame.insertCards(bbcio);

        // TODO: Figure out how to give some indication that this is a test, possibly in the title.
        JFrame f = new BBCTFrame(bbcio);
        f.setVisible(true);
    }

    private static void insertCards(BaseballCardIO bbcio) throws BBCTIOException, FileNotFoundException, IOException {
        String fileName = "util/cards.csv";
        List<BaseballCard> cards = BBCTFrame.readCards(fileName);

        // TODO: Replace with bbcio.insertCards() when implemented.
        for (BaseballCard card : cards) {
            bbcio.insertBaseballCard(card);
        }
    }

    private static List<BaseballCard> readCards(String fileName) throws FileNotFoundException, IOException {
        List<BaseballCard> cards = new ArrayList<>();
        BufferedReader in = new BufferedReader(new FileReader(fileName));

        // Throw away first line with headings
        String line = in.readLine();
        while (in.ready()) {
            line = in.readLine();
            String[] data = line.split(",");
            String brand = data[0];
            int year = Integer.parseInt(data[1]);
            int number = Integer.parseInt(data[2]);
            int value = number * 10;
            int count = 1;
            String playerName = data[3];
            String playerPosition = data[4];
            BaseballCard card = new BaseballCard(brand, year, number, value, count, playerName, playerPosition);
            
            cards.add(card);
        }

        return cards;
    }
}
