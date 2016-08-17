/*
 * This file is part of BBCT.
 *
 * Copyright 2012-14 codeguru <codeguru@users.sourceforge.net>
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
package bbct.swing.gui.event;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * {@link ShowCardActionListener} is used for buttons which change the currently
 * visible card in a {@link java.awt.Container} using a
 * {@link java.awt.CardLayout}.
 */
public class ShowCardActionListener implements ActionListener {

    /**
     * Create a new {@link ShowCardActionListener} that shows the card with the
     * given name.
     *
     * @param c
     * @param cardName The name of the card to show when an action occurs.
     */
    public ShowCardActionListener(Container c, String cardName) {
        this.c = c;
        this.cardName = cardName;
    }

    /**
     * Shows the card with the name associated with this
     * {@link ShowCardActionListener}.
     *
     * @param ae The {@link java.awt.event.ActionEvent} which caused this method
     * to be called.
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        Container parent = this.c.getParent();
        LayoutManager lm = this.c.getParent().getLayout();

        if (lm instanceof CardLayout) {
            CardLayout cl = (CardLayout) lm;
            cl.show(parent, this.cardName);
        }
        // Should I throw an exception if lm isn't a CardLayout?
    }
    private Container c = null;
    private String cardName = null;
}
