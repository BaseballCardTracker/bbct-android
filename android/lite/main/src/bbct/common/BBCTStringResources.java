/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2012 codeguru <codeguru@users.sourceforge.net>
 *
 * BBCT for Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BBCT for Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bbct.common;

/**
 * String resources used in the {@link bbct.common} package.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BBCTStringResources {

    /**
     * String resources for error messages.
     */
    public static class ErrorResources {

        /**
         * Database initialization error message.
         */
        public static final String DATABASE_INITIALIZATION_ERROR = "Unable to properly initialize database resources.";
        /**
         * Database cleanup error message.
         */
        public static final String DATABASE_CLEANUP_ERROR = "Unable to properly cleanup database resources.";
        /**
         * SELECT statement error message.
         */
        public static final String DATABASE_SELECT_ERROR = "Unable to retrieve cards from database.";
        /**
         * UPDATE statement error message.
         */
        public static final String DATABASE_UPDATE_ERROR = "Unable to update baseball card data.";
        
        public static final String CARD_ALREADY_EXISTS_ERROR = "A %1$d card from %2$s with number %3$d already exists.";

        private ErrorResources() {
        }
    }
}
