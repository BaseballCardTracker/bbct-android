/*
 * This file is part of BBCT.
 *
 * Copyright &copy; 2012 codeguru <codeguru@users.sourceforge.net>
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

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * {@link UpdateTitleAncestorListener} changes the title of
 * {@link bbct.gui.BBCTFrame} when a component is added to the component tree.
 * This is typically used for the {@link javax.swing.JPanel} subclasses in the
 * {@link bbct.gui} package.
 *
 * @see bbct.gui.FindCardsByYearPanel
 * @see bbct.gui.FindCardsByNumberPanel
 * @see bbct.gui.FindCardsByYearAndNumberPanel
 * @see bbct.gui.FindCardsByPlayerNamePanel
 */
public class UpdateTitleAncestorListener implements AncestorListener {

    /**
     * Creates a new {@link UpdateTitleAncestorListener} which will change the
     * title of {@link bbct.gui.BBCTFrame} to the given text.
     *
     * @param title The text to set in the title of {@link bbct.gui.BBCTFrame}.
     */
    public UpdateTitleAncestorListener(String title) {
        this.title = title;
    }

    /**
     * Changes the title of {@link bbct.gui.BBCTFrame} when a component is added
     * tot he component tree.
     *
     * @param ae The {@link javax.swing.event.AncestorEvent} which triggered
     * this method call.
     */
    @Override
    public void ancestorAdded(AncestorEvent ae) {
        JFrame frame = (JFrame) ae.getComponent().getTopLevelAncestor();
        frame.setTitle(title);
    }

    /**
     * Empty method.
     *
     * @param ae The {@link javax.swing.event.AncestorEvent} which triggered
     * this method call.
     */
    @Override
    public void ancestorMoved(AncestorEvent ae) {
    }

    /**
     * Empty method.
     *
     * @param ae The {@link javax.swing.event.AncestorEvent} which triggered
     * this method call.
     */
    @Override
    public void ancestorRemoved(AncestorEvent ae) {
    }
    private String title = null;
}
