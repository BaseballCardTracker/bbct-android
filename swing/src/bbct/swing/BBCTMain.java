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

import bbct.common.BBCTStringResources.ErrorResources;
import bbct.common.data.BaseballCardIO;
import bbct.common.data.JDBCBaseballCardIO;
import bbct.common.exceptions.BBCTIOException;
import bbct.swing.BBCTStringResources.DatabaseResources;
import bbct.swing.gui.BBCTFrame;
import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.JOptionPane;

/**
 * This is the driver class for the Baseball Card Tracker program (BBCT).
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BBCTMain {

    /**
     * Starts the BBCTMain Card Tracker by creating and showing the initial
     * window.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            BBCTMain.initLogger();
            Logger logger = Logger.getLogger(BBCTMain.class.getName());
            logger.log(Level.INFO, "Fixing to create a BaseballCardIO object.");
            BaseballCardIO bcio = new JDBCBaseballCardIO(DatabaseResources.DB_URL);

            logger.log(Level.INFO, "Fixing to show a new frame.");
            new BBCTFrame(bcio).setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(BBCTMain.class.getName()).log(Level.SEVERE, "Unable to initialize logger.", ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        } catch (BBCTIOException ex) {
            Logger.getLogger(BBCTMain.class.getName()).log(Level.SEVERE, "Unable to initialize storage.", ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), BBCTStringResources.ErrorResources.INITIALIZATION_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void initLogger() throws IOException {
        File logFolder = new File(BBCTMain.LOG_FOLDER_NAME);

        if (!logFolder.exists()) {
            logFolder.mkdir();
        }

        // TODO: Configure logger using a property file.
        boolean append = true;
        Handler handler = new FileHandler(BBCTMain.LOG_FILE_NAME, append);
        handler.setFormatter(new SimpleFormatter());

        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.INFO);
        logger.addHandler(handler);

        // TODO: Should I set this in a property file?
        System.setProperty("hsqldb.reconfig_logging", "false");
    }
    private static final String LOG_FOLDER_NAME = "log";
    private static final String LOG_FILE_NAME = LOG_FOLDER_NAME + "/bbct.log";
}
