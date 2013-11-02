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
 */
public class BBCTStringResources {

    /**
     * String resources related to database access.
     */
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

    /**
     * String resources for frame titles.
     */
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
        public static final String ADD_CARDS_PANEL_TITLE = BASEBALL_FRAME_TITLE
                + " - Add Cards";
        /**
         * Title shown in the BBCT window when
         * {@link bbct.swing.gui.FindCardsMenuPanel} is visible.
         */
        public static final String FIND_CARDS_MENU_PANEL_TITLE = BASEBALL_FRAME_TITLE
                + " - Find Cards";
        /**
         * Title shown in the BBCT window when
         * {@link bbct.swing.gui.FindCardsByYearPanel} is visible.
         */
        public static final String FIND_CARDS_BY_YEAR_PANEL_TITLE = BASEBALL_FRAME_TITLE
                + " - Find Cards by Year";
        /**
         * Title shown in the BBCT window when
         * {@link bbct.swing.gui.FindCardsByNumberPanel} is visible.
         */
        public static final String FIND_CARDS_BY_NUMBER_PANEL_TITLE = BASEBALL_FRAME_TITLE
                + " - Find Cards by Number";
        /**
         * Title shown in the BBCT window when
         * {@link bbct.swing.gui.FindCardsByNumberPanel} is visible.
         */
        public static final String FIND_CARDS_BY_YEAR_AND_NUMBER_PANEL_TITLE = BASEBALL_FRAME_TITLE
                + " - Find Cards by Year and Number";
        /**
         * Title shown in the BBCT window when
         * {@link bbct.swing.gui.FindCardsByPlayerNamePanel} is visible.
         */
        public static final String FIND_CARDS_BY_PLAYER_NAME_PANEL_TITLE = BASEBALL_FRAME_TITLE
                + " - Find Cards by Player Name";
        /**
         * Title shown in the BBCT window when
         * {@link bbct.swing.gui.EditCardsPanel} is visible.
         */
        public static final String EDIT_CARD_PANEL_TITLE = BASEBALL_FRAME_TITLE
                + " - Edit Card";
        /**
         * Title shown in the BBCT window when {@link bbct.swing.gui.AboutPanel}
         * is visible.
         */
        public static final String ABOUT_PANEL_TITLE = BASEBALL_FRAME_TITLE
                + " - About";
        /**
         * Title in the Player Details section of the card details panel.
         */
        public static final String PLAYER_DETAILS_BORDER_TITLE = "Player Details";
        /**
         * Title in the Card Details section of the card details panel.
         */
        public static final String CARD_DETAILS_BORDER_TITLE = "Card Details";

        private TitleResources() {
        }
    }

    /**
     * String resources for the About box.
     */
    public static class AboutResources {

        /**
         * Title bar in the About box.
         */
        public static final String ABOUT_TITLE = "Baseball Card Tracker";
        /**
         * Version string.
         */
        public static final String ABOUT_VERSION = "Version 0.5.3 (beta)";
        /**
         * Copyright notice.
         */
        public static final String ABOUT_COPYRIGHT = "<html>&copy 2012 codeguru</html>";
        /**
         * Website address.
         */
        public static final String ABOUT_WEBSITE = "Project website: http://sf.net/p/bbct";
        /**
         * GPL license information.
         */
        public static final String ABOUT_LICENSE = "<html>This software is licensed under the GPL. Visit<br> http://www.gnu.org/licenses/ for a copy of the license.</html>";

        private AboutResources() {
        }
    }

    /**
     * Labels used in the Card Details panel.
     */
    public static class LabelResources {

        /**
         * Card brand.
         */
        public static final String CARD_BRAND_LABEL = "Card Brand:";
        /**
         * Card year.
         */
        public static final String CARD_YEAR_LABEL = "Card Year:";
        /**
         * Card number.
         */
        public static final String CARD_NUMBER_LABEL = "Card Number:";
        /**
         * Card value.
         */
        public static final String CARD_VALUE_LABEL = "Card Value:";
        /**
         * Card count.
         */
        public static final String CARD_COUNT_LABEL = "Card Count:";
        /**
         * Player name.
         */
        public static final String PLAYER_NAME_LABEL = "Player Name:";
        /**
         * Player position.
         */
        public static final String PLAYER_POSITION_LABEL = "Player Position:";
        /**
         * Label for Delete Card checkbox.
         */
        public static final String DELETE_CARD_LABEL = "Delete this card";

        private LabelResources() {
        }
    }

    /**
     * Text shown in buttons.
     */
    public static class ButtonResources {

        /**
         * Text for the OK button.
         */
        public static final String OK_BUTTON = "OK";
        /**
         * Text for the Add Card button.
         */
        public static final String ADD_CARD_BUTTON = "Add Card";
        /**
         * Text for the Previous button.
         */
        public static final String PREVIOUS_BUTTON = "<-- Previous";
        /**
         * Text for the Next button.
         */
        public static final String NEXT_BUTTON = "Next -->";
        /**
         * Text for the Done button.
         */
        public static final String DONE_BUTTON = "Done";
        /**
         * Text for the Find Cards by Year button.
         */
        public static final String FIND_CARDS_BY_YEAR_BUTTON = "Find Cards By Year";
        /**
         * Text for the Find Cards by Number button.
         */
        public static final String FIND_CARDS_BY_NUMBER_BUTTON = "Find Cards By Number";
        /**
         * Text for the Find Cards by Year and Number button.
         */
        public static final String FIND_CARDS_BY_YEAR_AND_NUMBER_BUTTON = "Find Cards By Year and Number";
        /**
         * Text for the Find Cards by Player Name button.
         */
        public static final String FIND_CARDS_BY_PLAYER_NAME_BUTTON = "Find Cards By Player Name";
        /**
         * Text for the Back button.
         */
        public static final String BACK_BUTTON = "Back";
        /**
         * Text for the Find button.
         */
        public static final String FIND_BUTTON = "Find";
        /**
         * Text for the Add Cards button.
         */
        public static final String ADD_CARDS_BUTTON = "Add Cards";
        /**
         * Text for the Find Cards button.
         */
        public static final String FIND_CARDS_BUTTON = "Find Cards";
        /**
         * Text for the About button.
         */
        public static final String ABOUT_BUTTON = "About";
        /**
         * Text for the Exit button.
         */
        public static final String EXIT_BUTTON = "Exit";
        /**
         * Text for the Delete button.
         */
        public static final String DELETE_BUTTON = "Delete";

        private ButtonResources() {
        }
    }

    /**
     * Instructions to the user shown at the top of the frame.
     */
    public static class InstructionResources {

        /**
         * Default instruction used to initialize the instruction label.
         */
        public static final String DUMMY_INSTRUCTIONS = "Just Starting!";
        /**
         * Tell the user that the card was added successfully and prompt for the
         * brand name for the next card.
         */
        public static final String CARD_ADDED_INSTRUCTIONS = "Card added. Enter brand name for another card.";
        /**
         * Prompt the user for the card's brand.
         */
        public static final String CARD_BRAND_INSTRUCTIONS = "Enter card brand name.";
        /**
         * Prompt the user for the card's year.
         */
        public static final String CARD_YEAR_INSTRUCTIONS = "Enter card year.";
        /**
         * Prompt the user for the card's number.
         */
        public static final String CARD_NUMBER_INSTRUCTIONS = "Enter card number.";
        /**
         * Prompt the user for the card's value.
         */
        public static final String CARD_VALUE_INSTRUCTIONS = "Enter card value.";
        /**
         * Prompt the user for the card count.
         */
        public static final String CARD_COUNT_INSTRUCTIONS = "Enter card count.";
        /**
         * Prompt the user for the name of the player on the card.
         */
        public static final String PLAYER_NAME_INSTRUCTIONS = "Enter player name.";
        /**
         * Prompt the user for the position played by the player on the card.
         */
        public static final String PLAYER_POSITION_INSTRUCTIONS = "Select player position.";
        /**
         * Prompt the user to choose an option for how they want to search for
         * cards.
         */
        public static final String FIND_CARDS_MENU_INSTRUCTIONS = "Choose an option:";
        /**
         * Prompt the user to choose an option from the main menu.
         */
        public static final String MENU_INSTRUCTIONS = "Choose an option:";
        /**
         * Tell the user how to close the About window.
         */
        public static final String ABOUT_INSTRUCTIONS = "Click OK when ready.";

        private InstructionResources() {
        }
    }

    /**
     * Error messages.
     */
    public static class ErrorResources {

        /**
         * Error when the user fails to provide a card brand.
         */
        public static final String CARD_BRAND_ERROR = "Please enter a card brand.";
        /**
         * Error when the user fails to provide a card year.
         */
        public static final String CARD_YEAR_ERROR = "Please enter a valid four-digit year.";
        /**
         * Error when the user fails to provide a card number.
         */
        public static final String CARD_NUMBER_ERROR = "Please enter a positive integer for the card number.";
        /**
         * Error when the user fails to provide a card value.
         */
        public static final String CARD_VALUE_ERROR = "Please enter a valid dollar amount.";
        /**
         * Error when the user fails to provide a card count.
         */
        public static final String CARD_COUNT_ERROR = "Please enter a positive integer for the card count.";
        /**
         * Error when the user fails to provide a player name.
         */
        public static final String PLAYER_NAME_ERROR = "Please enter a player name.";
        /**
         * Error when the user fails to provide a player position.
         */
        public static final String PLAYER_POSITION_ERROR = "Please select a player position.";
        /**
         * Error when a search returns an empty data set.
         */
        public static final String NO_CARDS_FOUND_ERROR = "No cards found.";
        /**
         * Error when the application encounters an error while closing.
         */
        public static final String CLOSE_ERROR_TITLE = "Clean Up Error";
        /**
         * Error when an error occurs with an I/O operation.
         */
        public static final String IO_ERROR_TITLE = "I/O Error";
        /**
         * Error indicating that the input was invalid.
         */
        public static final String INPUT_ERROR_TITLE = "Invalid Input";
        /**
         * Title for a dialog when no cards are found from a search.
         */
        public static final String NO_CARDS_FOUND_ERROR_TITLE = "No Cards Found";
        /**
         * Title for a dialog when there is an error at start up.
         */
        public static final String INITIALIZATION_ERROR_TITLE = "Initialization Error";
        /**
         * Title for a catch-all dialog for errors that should never occur.
         */
        public static final String UNEXPECTED_EXCEPTION_ERROR_TITLE = "Unexpected Exception";

        private ErrorResources() {
        }
    }

    /**
     * Strings displayed in a message dialog.
     */
    public static class DialogResources {

        /**
         * Prompt the user when deleting multiple cards.
         */
        public static final String DELETE_MULTIPLE_CARD_PROMPT = "You are about to delete %d cards. Are you sure?";
        /**
         * Prompt the user when deleting a single card.
         */
        public static final String DELETE_SINGLE_CARD_PROMPT = "You are about to delete this card. Are you sure?";

        private DialogResources() {
        }
    }

    /**
     * Text used in any ComboBoxes.
     */
    public static class ComboBoxResources {

        /**
         * The choices for a player's position.
         */
        public static final String[] POSITIONS = { "Pitcher", "Catcher",
                "First Base", "Second Base", "Third Base", "Shortstop",
                "Left Field", "Center Field", "Right Field" };

        private ComboBoxResources() {
        }
    }

    private BBCTStringResources() {
    }
}
