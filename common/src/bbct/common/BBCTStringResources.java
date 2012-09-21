/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bbct.common;

/**
 *
 * @author lib_lab_ref08
 */
public class BBCTStringResources {

    public static class ErrorResources {

        public static final String DATABASE_INITIALIZATION_ERROR = "Unable to properly initialize database resources.";
        public static final String DATABASE_CLEANUP_ERROR = "Unable to properly cleanup database resources.";
        public static final String DATABASE_SELECT_ERROR = "Unable to retrieve cards from database.";
        public static final String DATABASE_UPDATE_ERROR = "Unable to update baseball card data.";

        private ErrorResources() {
        }
    }
}
