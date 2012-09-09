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
package bbct;

import bbct.data.BaseballCardIO;
import bbct.data.BaseballCardJDBCIO;
import bbct.exceptions.BBCTIOException;
import bbct.gui.BBCTFrame;
import bbct.gui.GUIResources;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * This is the driver class for the Baseball Card Tracker program.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class Baseball {

    /**
     * Starts the Baseball Card Tracker by creating and showing the initial
     * window.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Baseball.initLogger();
            BaseballCardIO bcio = new BaseballCardJDBCIO(GUIResources.DB_URL);

            new BBCTFrame(bcio).setVisible(true);
        } catch (BBCTIOException | IOException ex) {
            Logger.getLogger(Baseball.class.getName()).log(Level.SEVERE, "Unable to initialize storage.", ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Initialization Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void initLogger() throws IOException {
        FileHandler handler = null;
        
        try {
            boolean append = true;
            handler = new FileHandler("log/bbct.log", append);
            
            Logger logger = Logger.getLogger("");
            logger.setLevel(Level.ALL);
            logger.addHandler(handler);
        } finally {
            handler.close();
        }
    }
}
