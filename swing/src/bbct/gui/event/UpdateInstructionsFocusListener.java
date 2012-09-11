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

import bbct.gui.BBCTFrame;
import java.awt.Container;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 * {@link UpdateInstructionsFocusListener} changes the text of the instruction
 * label in {@link bbct.gui.BBCTFrame} when a Swing component gains focus. This
 * is typically used for {@link javax.swing.JTextField}s.
 *
 * @see bbct.gui.BBCTFrame#setInstructions(java.lang.String)
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class UpdateInstructionsFocusListener extends FocusAdapter {

    /**
     * Creates a new {@link UpdateInstructionsFocusListener} that will update
     * the instruction label in {@link bbct.gui.BBCTFrame} to the given text.
     *
     * @param instr The instruction text to use.
     */
    public UpdateInstructionsFocusListener(String instr) {
        this.instr = instr;
    }

    /**
     * Updates the instruction label in {@link bbct.gui.BBCTFrame} when a
     * focusGained event occurs.
     *
     * @param fe The {@link java.awt.event.FocusEvent} which triggered this method call.
     */
    @Override
    public void focusGained(FocusEvent fe) {
        Container topLevelAncestor = ((JComponent) fe.getComponent()).getTopLevelAncestor();

        if (topLevelAncestor instanceof BBCTFrame) {
            ((BBCTFrame) topLevelAncestor).setInstructions(instr);
        }
    }
    private String instr = null;
}
