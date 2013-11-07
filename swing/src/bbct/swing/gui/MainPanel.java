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

import bbct.common.data.BaseballCardIO;
import java.awt.CardLayout;

/**
 * {@link MainPanel} contains all the other {@link javax.swing.JPanel}s used in
 * the BBCT application. A {@link java.awt.CardLayout} selects which
 * {@link javax.swing.JPanel} is displayed.
 *
 * @see MenuPanel
 * @see AddCardsPanel
 * @see FindCardsPanel
 * @see FindCardsByPanel
 * @see FindCardsByYearPanel
 * @see FindCardsByNumberPanel
 * @see FindCardsByYearAndNumberPanel
 * @see FindCardsByPlayerNamePanel
 * @see AboutPanel
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
@SuppressWarnings("serial")
public class MainPanel extends javax.swing.JPanel {

    /**
     * Creates a new {@link MainPanel}.
     *
     * @param bcio The connection to the underlying persistent storage
     * mechanism.
     */
    public MainPanel(BaseballCardIO bcio) {
        this.bcio = bcio;
        this.initComponents();

    }

    private void initComponents() {
        MenuPanel menuPanel = new MenuPanel();
        AddCardsPanel addCardsPanel = new AddCardsPanel(this.bcio);
        FindCardsMenuPanel findCardsMenuPanel = new FindCardsMenuPanel();
        AboutPanel aboutPanel = new AboutPanel();
        FindCardsByPanel yearInputPanel = new FindCardsByYearPanel(this.bcio);
        FindCardsPanel findCardsByYearPanel = new FindCardsPanel(bcio, yearInputPanel);
        FindCardsByPanel numberInputPanel = new FindCardsByNumberPanel(this.bcio);
        FindCardsPanel findCardsByNumberPanel = new FindCardsPanel(bcio, numberInputPanel);
        FindCardsByPanel yearAndNumberInputPanel = new FindCardsByYearAndNumberPanel(this.bcio);
        FindCardsPanel findCardsByYearAndNumberPanel = new FindCardsPanel(bcio, yearAndNumberInputPanel);
        FindCardsByPanel playerNameInputPanel = new FindCardsByPlayerNamePanel(this.bcio);
        FindCardsPanel findCardsByPlayerNamePanel = new FindCardsPanel(bcio, playerNameInputPanel);

        this.setLayout(new CardLayout());
        this.add(menuPanel, BBCTFrame.MENU_CARD_NAME);
        this.add(addCardsPanel, BBCTFrame.ADD_CARDS_CARD_NAME);
        this.add(findCardsMenuPanel, BBCTFrame.FIND_CARDS_MENU_CARD_NAME);
        this.add(aboutPanel, BBCTFrame.ABOUT_CARD_NAME);
        this.add(findCardsByYearPanel, BBCTFrame.FIND_CARDS_BY_YEAR_CARD_NAME);
        this.add(findCardsByNumberPanel, BBCTFrame.FIND_CARDS_BY_NUMBER_CARD_NAME);
        this.add(findCardsByYearAndNumberPanel, BBCTFrame.FIND_CARDS_BY_YEAR_AND_NUMBER_CARD_NAME);
        this.add(findCardsByPlayerNamePanel, BBCTFrame.FIND_CARDS_BY_PLAYER_NAME_CARD_NAME);
    }
    private BaseballCardIO bcio = null;
}
