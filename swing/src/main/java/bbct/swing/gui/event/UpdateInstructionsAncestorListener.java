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

import bbct.swing.gui.BBCTFrame;
import java.awt.Container;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * {@link UpdateInstructionsAncestorListener} changes the text of the
 * instruction label in {@link bbct.gui.BBCTFrame} when a Swing component is
 * added to the component tree.
 *
 * @see bbct.gui.BBCTFrame#setInstructions(java.lang.String)
 */
public class UpdateInstructionsAncestorListener implements AncestorListener {

    /**
     * Creates a new {@link UpdateInstructionsAncestorListener} that will update
     * the instruction label in {@link bbct.gui.BBCTFrame} to the given text.
     *
     * @param instr The instruction text to use.
     */
    public UpdateInstructionsAncestorListener(String instr) {
        this.instr = instr;
    }

    @Override
    public void ancestorAdded(AncestorEvent ae) {
        Container topLevelAncestor = ae.getComponent().getTopLevelAncestor();

        if (topLevelAncestor instanceof BBCTFrame) {
            BBCTFrame frame = (BBCTFrame) topLevelAncestor;
            frame.setInstructions(this.instr);
        }
    }

    @Override
    public void ancestorRemoved(AncestorEvent ae) {
    }

    @Override
    public void ancestorMoved(AncestorEvent ae) {
    }
    private String instr = null;
}
