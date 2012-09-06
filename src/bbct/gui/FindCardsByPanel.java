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

import bbct.data.BaseballCard;
import bbct.exceptions.IOException;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * {@link FindCardsByPanel} provides the contract for a {@link javax.swing.JPanel}
 * which allows user input that is used while searching the underlying storage
 * mechanism for baseball card data. Typically this panel provides GUI controls
 * for the user to enter parameter values for the search, such as the year of a
 * card or the name of the player on the card. Subclasses of {@link FindCardsByPanel}
 * are required to override the {@link #getBaseballCards()} method to implement
 * the specific search depending on the subclasses exact criteria.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public abstract class FindCardsByPanel extends JPanel {

    /**
     * Create a new {@link FindCardsByPanel}. This constructor ensures that the
     * correct GUI control will have focus when the panel is displayed.
     */
    public FindCardsByPanel() {
        this.addAncestorListener(new AncestorListener() {

            @Override
            public void ancestorAdded(AncestorEvent ae) {
                FindCardsByPanel.this.setFocus();
            }

            @Override
            public void ancestorRemoved(AncestorEvent ae) {
            }

            @Override
            public void ancestorMoved(AncestorEvent ae) {
            }
        });
    }

    /**
     * Returns a list of {@link BaseballCard}s which match the search criteria
     * determined by a subclass of {@link FindCardsByPanel}.
     *
     * @return A list of {@link BaseballCard}s which match the search criteria
     * determined by a subclass of {@link FindCardsByPanel}.
     * @throws IOException If an error occurs in the underlying storage
     * mechanism while searching for baseball card data.
     */
    protected abstract List<BaseballCard> getBaseballCards() throws IOException;

    /**
     * Subclasses override this method to ensure that the correct control has
     * focus when the panel is first displayed.
     */
    protected abstract void setFocus();
}
