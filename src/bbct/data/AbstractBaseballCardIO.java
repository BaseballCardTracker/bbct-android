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
package bbct.data;

import bbct.exceptions.BBCTIOException;
import java.util.List;

/**
 * This is an abstract class which implements the {@link BaseballCardIO}
 * interface. It provides a default implementation of {@link #updateCards(List)}
 * which iterates through the list of cards and calls
 * {@link #updateCard(BaseballCard)} on each one.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public abstract class AbstractBaseballCardIO implements BaseballCardIO {

    /**
     * Default implementation of {@link #updateCards(List)} which iterates
     * through the list of cards and calls {@link #updateCard(BaseballCard)} on
     * each one.
     *
     * @param cards The list of cards to update.
     * @throws IOException If any I/O errors occur while writing to the
     * underlying storage mechanism.
     */
    @Override
    public void updateCards(List<BaseballCard> cards) throws BBCTIOException {
        for (BaseballCard card : cards) {
            this.updateCard(card);
        }
    }
}
