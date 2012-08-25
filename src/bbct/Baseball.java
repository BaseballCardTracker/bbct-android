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
import bbct.exceptions.IOException;
import bbct.gui.BBCTFrame;
import bbct.gui.GUIResources;
import javax.swing.JOptionPane;

/**
 * This is the driver class for the Baseball Card Tracker program.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class Baseball {

    /**
     * Starts the Baseball Card Tracker by creating and showing the initial window.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            BaseballCardIO bcio = new BaseballCardJDBCIO(GUIResources.DB_URL);
            
            new BBCTFrame(bcio).setVisible(true);
        } catch (IOException ex) {
             JOptionPane.showMessageDialog(null, ex.getMessage(), "Initialization Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
