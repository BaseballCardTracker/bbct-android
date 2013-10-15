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
package bbct.common.data;

import bbct.common.exceptions.BBCTIOException;
import java.util.List;

/**
 * This is an abstract class which implements the {@link BaseballCardIO}
 * interface. It provides a default implementation of
 * {@link BaseballCardIO#updateBaseballCards(java.util.List)} and
 * {@link BaseballCardIO#insertBaseballCards(java.util.List)} which iterate
 * through the list of cards and calls {@link #updateCard(BaseballCard)} or
 * {@link #insertBaseballCard(BaseballCard)} respectively on each card.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public abstract class AbstractBaseballCardIO implements BaseballCardIO {

    /**
     * Default implementation of
     * {@link BaseballCardIO#insertBaseballCards(List)} which iterates through
     * the list of cards and calls {@link #insertBaseballCard(BaseballCard)} on
     * each one.
     *
     * @param cards The list of cards to insert.
     * @throws BBCTIOException If any I/O errors occur while writing to the
     * underlying storage mechanism.
     */
    @Override
    public void insertBaseballCards(List<BaseballCard> cards) throws BBCTIOException {
        for (BaseballCard card : cards) {
            this.insertBaseballCard(card);
        }
    }

    /**
     * Default implementation of
     * {@link BaseballCardIO#updateBaseballCards(List)} which iterates through
     * the list of cards and calls {@link #updateBaseballCard(BaseballCard)} on
     * each one.
     *
     * @param cards The list of cards to update.
     * @throws BBCTIOException If any I/O errors occur while writing to the
     * underlying storage mechanism.
     */
    @Override
    public void updateBaseballCards(List<BaseballCard> cards) throws BBCTIOException {
        for (BaseballCard card : cards) {
            this.updateBaseballCard(card);
        }
    }

    /**
     * Default implementation of
     * {@link BaseballCardIO#removeBaseballCards(List)} which iterates through
     * the list of cards and calls {@link #removeBaseballCard(BaseballCard)} on
     * each one.
     *
     * @param cards The list of cards to remove.
     * @throws BBCTIOException If any I/O errors occur while writing to the
     * underlying storage mechanism.
     */
    @Override
    public void removeBaseballCards(List<BaseballCard> cards) throws BBCTIOException {
        for (BaseballCard card : cards) {
            this.removeBaseballCard(card);
        }
    }
}
