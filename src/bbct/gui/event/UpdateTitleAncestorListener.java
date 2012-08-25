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
package bbct.gui.event;

import bbct.gui.FindCardsByNumberPanel;
import bbct.gui.GUIResources;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * TODO: JavaDoc
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class UpdateTitleAncestorListener implements AncestorListener {

    public UpdateTitleAncestorListener(String title) {
        this.title = title;
    }
    
    @Override
    public void ancestorAdded(AncestorEvent ae) {
        JFrame frame = (JFrame) ((JComponent)ae.getComponent()).getTopLevelAncestor();
        frame.setTitle(title);
    }

    @Override
    public void ancestorMoved(AncestorEvent ae) {
    }

    @Override
    public void ancestorRemoved(AncestorEvent ae) {
    }
    
    private String title = null;
}
