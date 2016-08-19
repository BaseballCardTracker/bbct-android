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
package bbct.swing.gui.inputverifiers;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;

/**
 * {@link CurrencyInputVerifier} verifies that a
 * {@link javax.swing.JFormattedTextField} contains a positive dollar amount.
 */
public class CurrencyInputVerifier extends InputVerifier {
    private InputVerifier notEmptyVerifier = new NotEmptyInputVerifier();

    /**
     * Verify that a {@link javax.swing.JFormattedTextField} contains a positive
     * dollar amount.
     *
     * @param jc The {@link javax.swing.JFormattedTextField} with input to
     * verify.
     * @return <code>true</code> if the {@link javax.swing.JFormattedTextField}
     * contains valid a positive dollar amount., <code>false</code> otherwise.
     */
    @Override
    public boolean verify(JComponent jc) {
        if (jc instanceof JFormattedTextField && this.notEmptyVerifier.verify(jc)) {
            JFormattedTextField currencyTextField = (JFormattedTextField) jc;
            if (currencyTextField.isEditValid()) {
                double valueDbl = ((Number) currencyTextField.getValue()).doubleValue();
                int value = (int) (valueDbl * 100);

                return value >= 0;
            }
        }

        return false;
    }
}
