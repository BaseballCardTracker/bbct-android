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

/**
 * {@link GUIResources} provides resources for the Swing GUI for BBCT. Most of
 * these are Strings which will be displayed in various parts of the interface.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class GUIResources {

    /**
     * URL for the database which stores baseball card data. This is used by
     * JDBC to load the correct driver and open a connection to the database.
     */
    public static final String DB_URL = "jdbc:hsqldb:file:db/baseball_cards.db";
    /**
     * Base title shown in the BBCT window.
     */
    public static final String BASEBALL_FRAME_TITLE = "Baseball Card Tracker";
    /**
     * Title shown in the BBCT window when {@link MainPanel} is visible.
     */
    public static final String MAIN_PANEL_TITLE = BASEBALL_FRAME_TITLE;
    /**
     * Title shown in the BBCT window when {@link AddCardsPanel} is visible.
     */
    public static final String ADD_CARDS_PANEL_TITLE = BASEBALL_FRAME_TITLE + " - Add Cards";
    /**
     * Title shown in the BBCT window when {@link FindCardsMenuPanel} is
     * visible.
     */
    public static final String FIND_CARDS_MENU_PANEL_TITLE = BASEBALL_FRAME_TITLE + " - Find Cards";
    /**
     * Title shown in the BBCT window when {@link FindCardsByYearPanel} is
     * visible.
     */
    public static final String FIND_CARDS_BY_YEAR_PANEL_TITLE = BASEBALL_FRAME_TITLE + " - Find Cards by Year";
    /**
     * Title shown in the BBCT window when {@link FindCardsByNumberPanel} is
     * visible.
     */
    public static final String FIND_CARDS_BY_NUMBER_PANEL_TITLE = BASEBALL_FRAME_TITLE + " - Find Cards by Number";
    /**
     * Title shown in the BBCT window when {@link FindCardsByNumberPanel} is
     * visible.
     */
    public static final String FIND_CARDS_BY_YEAR_AND_NUMBER_PANEL_TITLE = BASEBALL_FRAME_TITLE + " - Find Cards by Year and Number";
    /**
     * Title shown in the BBCT window when {@link FindCardsByPlayerNamePanel} is
     * visible.
     */
    public static final String FIND_CARDS_BY_PLAYER_NAME_PANEL_TITLE = BASEBALL_FRAME_TITLE + " - Find Cards by Player Name";
    /**
     * Title shown in the BBCT window when {@link EditCardsPanel} is visible.
     */
    public static final String EDIT_CARD_PANEL_TITLE = BASEBALL_FRAME_TITLE + " - Edit Card";
    /**
     * Title shown in the BBCT window when {@link AboutPanel} is visible.
     */
    public static final String ABOUT_PANEL_TITLE = BASEBALL_FRAME_TITLE + " - About";

    private GUIResources() {
    }
}
