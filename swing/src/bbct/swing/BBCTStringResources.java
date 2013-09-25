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
package bbct.swing;

/**
 * {@link BBCTStringResources} provides resources for the Swing GUI for BBCT.
 * Most of these are Strings which will be displayed in various parts of the
 * interface.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BBCTStringResources {

    public static class DatabaseResources {

        /**
         * URL for the database which stores baseball card data. This is used by
         * JDBC to load the correct driver and open a connection to the
         * database.
         */
        public static final String DB_URL = "jdbc:hsqldb:file:db/bbct.db";

        private DatabaseResources() {
        }
    }

    public static class TitleResources {

        /**
         * Base title shown in the BBCT window.
         */
        public static final String BASEBALL_FRAME_TITLE = "Baseball Card Tracker";
        /**
         * Title shown in the BBCT window when {@link bbct.swing.gui.MainPanel}
         * is visible.
         */
        public static final String MAIN_PANEL_TITLE = BASEBALL_FRAME_TITLE;
        /**
         * Title shown in the BBCT window when
         * {@link bbct.swing.gui.AddCardsPanel} is visible.
         */
        public static final String ADD_CARDS_PANEL_TITLE = BASEBALL_FRAME_TITLE + " - Add Cards";
        /**
         * Title shown in the BBCT window when
         * {@link bbct.swing.gui.FindCardsMenuPanel} is visible.
         */
        public static final String FIND_CARDS_MENU_PANEL_TITLE = BASEBALL_FRAME_TITLE + " - Find Cards";
        /**
         * Title shown in the BBCT window when
         * {@link bbct.swing.gui.FindCardsByYearPanel} is visible.
         */
        public static final String FIND_CARDS_BY_YEAR_PANEL_TITLE = BASEBALL_FRAME_TITLE + " - Find Cards by Year";
        /**
         * Title shown in the BBCT window when
         * {@link bbct.swing.gui.FindCardsByNumberPanel} is visible.
         */
        public static final String FIND_CARDS_BY_NUMBER_PANEL_TITLE = BASEBALL_FRAME_TITLE + " - Find Cards by Number";
        /**
         * Title shown in the BBCT window when
         * {@link bbct.swing.gui.FindCardsByNumberPanel} is visible.
         */
        public static final String FIND_CARDS_BY_YEAR_AND_NUMBER_PANEL_TITLE = BASEBALL_FRAME_TITLE + " - Find Cards by Year and Number";
        /**
         * Title shown in the BBCT window when
         * {@link bbct.swing.gui.FindCardsByPlayerNamePanel} is visible.
         */
        public static final String FIND_CARDS_BY_PLAYER_NAME_PANEL_TITLE = BASEBALL_FRAME_TITLE + " - Find Cards by Player Name";
        /**
         * Title shown in the BBCT window when
         * {@link bbct.swing.gui.EditCardsPanel} is visible.
         */
        public static final String EDIT_CARD_PANEL_TITLE = BASEBALL_FRAME_TITLE + " - Edit Card";
        /**
         * Title shown in the BBCT window when {@link bbct.swing.gui.AboutPanel}
         * is visible.
         */
        public static final String ABOUT_PANEL_TITLE = BASEBALL_FRAME_TITLE + " - About";
        public static final String PLAYER_DETAILS_BORDER_TITLE = "Player Details";
        public static final String CARD_DETAILS_BORDER_TITLE = "Card Details";

        private TitleResources() {
        }
    }

    public static class AboutResources {

        public static final String ABOUT_TITLE = "Baseball Card Tracker";
        public static final String ABOUT_VERSION = "Version 0.5.3 (beta)";
        public static final String ABOUT_COPYRIGHT = "<html>&copy 2012 codeguru</html>";
        public static final String ABOUT_WEBSITE = "Project website: http://sf.net/p/bbct";
        public static final String ABOUT_LICENSE = "<html>This software is licensed under the GPL. Visit<br> http://www.gnu.org/licenses/ for a copy of the license.</html>";

        private AboutResources() {
        }
    }

    public static class LabelResources {

        public static final String CARD_BRAND_LABEL = "Card Brand:";
        public static final String CARD_YEAR_LABEL = "Card Year:";
        public static final String CARD_NUMBER_LABEL = "Card Number:";
        public static final String CARD_VALUE_LABEL = "Card Value:";
        public static final String CARD_COUNT_LABEL = "Card Count:";
        public static final String PLAYER_NAME_LABEL = "Player Name:";
        public static final String PLAYER_POSITION_LABEL = "Player Position:";
        public static final String DELETE_CARD_LABEL = "Delete this card";

        private LabelResources() {
        }
    }

    public static class ButtonResources {

        public static final String OK_BUTTON = "OK";
        public static final String ADD_CARD_BUTTON = "Add Card";
        public static final String PREVIOUS_BUTTON = "<-- Previous";
        public static final String NEXT_BUTTON = "Next -->";
        public static final String DONE_BUTTON = "Done";
        public static final String FIND_CARDS_BY_YEAR_BUTTON = "Find Cards By Year";
        public static final String FIND_CARDS_BY_NUMBER_BUTTON = "Find Cards By Number";
        public static final String FIND_CARDS_BY_YEAR_AND_NUMBER_BUTTON = "Find Cards By Year and Number";
        public static final String FIND_CARDS_BY_PLAYER_NAME_BUTTON = "Find Cards By Player Name";
        public static final String BACK_BUTTON = "Back";
        public static final String FIND_BUTTON = "Find";
        public static final String ADD_CARDS_BUTTON = "Add Cards";
        public static final String FIND_CARDS_BUTTON = "Find Cards";
        public static final String ABOUT_BUTTON = "About";
        public static final String EXIT_BUTTON = "Exit";
        public static final String DELETE_BUTTON = "Delete";

        private ButtonResources() {
        }
    }

    public static class InstructionResources {

        public static final String DUMMY_INSTRUCTIONS = "Just Starting!";
        public static final String CARD_ADDED_INSTRUCTIONS = "Card added. Enter brand name for another card.";
        public static final String CARD_BRAND_INSTRUCTIONS = "Enter card brand name.";
        public static final String CARD_YEAR_INSTRUCTIONS = "Enter card year.";
        public static final String CARD_NUMBER_INSTRUCTIONS = "Enter card number.";
        public static final String CARD_VALUE_INSTRUCTIONS = "Enter card value.";
        public static final String CARD_COUNT_INSTRUCTIONS = "Enter card count.";
        public static final String PLAYER_NAME_INSTRUCTIONS = "Enter player name.";
        public static final String PLAYER_POSITION_INSTRUCTIONS = "Select player position.";
        public static final String FIND_CARDS_MENU_INSTRUCTIONS = "Choose an option:";
        public static final String MENU_INSTRUCTIONS = "Choose an option:";
        public static final String ABOUT_INSTRUCTIONS = "Click OK when ready.";

        private InstructionResources() {
        }
    }

    public static class ErrorResources {

        public static final String CARD_BRAND_ERROR = "Please enter a card brand.";
        public static final String CARD_YEAR_ERROR = "Please enter a valid four-digit year.";
        public static final String CARD_NUMBER_ERROR = "Please enter a positive integer for the card number.";
        public static final String CARD_VALUE_ERROR = "Please enter a valid dollar amount.";
        public static final String CARD_COUNT_ERROR = "Please enter a positive integer for the card count.";
        public static final String PLAYER_NAME_ERROR = "Please enter a player name.";
        public static final String PLAYER_POSITION_ERROR = "Please select a player position.";
        public static final String NO_CARDS_FOUND_ERROR = "No cards found.";
        public static final String CLOSE_ERROR_TITLE = "Clean Up Error";
        public static final String IO_ERROR_TITLE = "I/O Error";
        public static final String INPUT_ERROR_TITLE = "Invalid Input";
        public static final String NO_CARDS_FOUND_ERROR_TITLE = "No cards found.";

        private ErrorResources() {
        }
    }
    
    public static class DialogResources {
        
        public static final String DELETE_MULTIPLE_CARD_PROMPT = "You are about to delete %d cards. Are you sure?";
        public static final String DELETE_SINGLE_CARD_PROMPT = "You are about to delete this card. Are you sure?";
        
        private DialogResources() {
        }
    }

    public static class ComboBoxResources {

        public static final String[] POSITIONS = {
            "Pitcher",
            "Catcher",
            "First Base",
            "Second Base",
            "Third Base",
            "Shortstop",
            "Left Field",
            "Center Field",
            "Right Field"
        };

        private ComboBoxResources() {
        }
    }

    private BBCTStringResources() {
    }
}
