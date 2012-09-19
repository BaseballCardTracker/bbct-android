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
package bbct.gui.event;

import java.awt.Container;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * {@link SetDefaultButtonAncestorListener} sets the default button of a
 * {@link javax.swing.JFrame}.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class SetDefaultButtonAncestorListener implements AncestorListener {

    public SetDefaultButtonAncestorListener(JButton button) {
        this.button = button;
    }

    @Override
    public void ancestorAdded(AncestorEvent ae) {
        Container topLevelAncestor = ae.getComponent().getTopLevelAncestor();

        if (topLevelAncestor instanceof JFrame) {
            JFrame frame = (JFrame) topLevelAncestor;
            frame.getRootPane().setDefaultButton(this.button);
        }
        // TODO: Should I throw an exception if topLevelAncestor isn't a JFrame?
    }

    @Override
    public void ancestorRemoved(AncestorEvent ae) {
    }

    @Override
    public void ancestorMoved(AncestorEvent ae) {
    }
    
    private JButton button = null;
}
