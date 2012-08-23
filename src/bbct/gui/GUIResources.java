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
 * TODO: JavaDoc
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class GUIResources {

    /**
     * 
     */
    public static String DB_URL = "jdbc:hsqldb:file:db/baseball_cards.db";
    /**
     * 
     */
    public static int DB_ERROR = 1;
    /**
     * 
     */
    public static final String BASEBALL_FRAME_TITLE = "Baseball Card Tracker";
    /**
     * 
     */
    public static final String MAIN_PANEL_TITLE = BASEBALL_FRAME_TITLE;
    /**
     * 
     */
    public static final String ADD_CARDS_PANEL_TITLE = BASEBALL_FRAME_TITLE + " - Add Cards";
    /**
     * 
     */
    public static final String FIND_CARDS_PANEL_TITLE = BASEBALL_FRAME_TITLE + " - Find Cards";
    /**
     * 
     */
    public static final String FIND_CARDS_BY_YEAR_PANEL_TITLE = BASEBALL_FRAME_TITLE + " - Find Cards by Year";
    /**
     * 
     */
    public static final String FIND_CARDS_PANEL_BY_NUMBER_TITLE = BASEBALL_FRAME_TITLE + " - Find Cards by Number";
    /**
     * 
     */
    public static final String FIND_CARDS_BY_YEAR_AND_NUMBER_PANEL_TITLE = BASEBALL_FRAME_TITLE + " - Find Cards by Year and Number";
    /**
     * 
     */
    public static final String FIND_CARDS_BY_PLAYER_NAME_PANEL_TITLE = BASEBALL_FRAME_TITLE + " - Find Cards by Player Name";
    /**
     * 
     */
    public static final String EDIT_CARD_PANEL_TITLE = BASEBALL_FRAME_TITLE + " - Edit Card";
    /**
     * 
     */
    public static final String ABOUT_PANEL_TITLE = BASEBALL_FRAME_TITLE + " - About";

    private GUIResources() {
    }
}
