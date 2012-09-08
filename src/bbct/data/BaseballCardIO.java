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
package bbct.data;

import bbct.exceptions.IOException;
import java.util.List;

/**
 * The {@link BaseballCardIO} interface provides the contract for all I/O
 * operations for the BBCT application. These provide persistence for
 * {@link BaseballCard} objects.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public interface BaseballCardIO {

    /**
     * Close the connection to the underlying persistent storage. This method
     * should dispose of all resources used for any I/O operations.
     *
     * @throws IOException If an error occurs while closing the underlying
     * storage mechanism.
     */
    public void close() throws IOException;

    /**
     * Insert a {@link BaseballCard} to the underlying persistent storage.
     *
     * @param card The {@link BaseballCard} to insert.
     * @throws IOException If any I/O errors occur while inserting.
     */
    public void insertBaseballCard(BaseballCard card) throws IOException;

    /**
     * Search for {@link BaseballCard}s matching the specified year.
     *
     * @param year The year of {@link BaseballCard}s to look for.
     * @return A list of {@link BaseballCard}s which match the specified year.
     * @throws IOException If any I/O errors occur while reading the underlying
     * storage mechanism.
     */
    public List<BaseballCard> getBaseballCardsByYear(int year) throws IOException;

    /**
     * Search for {@link BaseballCard}s matching the specified number.
     *
     * @param number The number of {@link BaseballCard}s to look for.
     * @return A list of {@link BaseballCard}s which match the specified number.
     * @throws IOException If any I/O errors occur while reading the underlying
     * storage mechanism.
     */
    public List<BaseballCard> getBaseballCardsByNumber(int number) throws IOException;

    /**
     * Search for {@link BaseballCard}s matching the specified number.
     *
     * @param year The year of {@link BaseballCard}s to look for.
     * @param number The number of {@link BaseballCard}s to look for.
     * @return A list of {@link BaseballCard}s which match the specified year
     * and number.
     * @throws IOException If any I/O errors occur while reading the underlying
     * storage mechanism.
     */
    public List<BaseballCard> getBaseballCardsByYearAndNumber(int year, int number) throws IOException;

    /**
     * Search for {@link BaseballCard}s matching the specified player's name.
     *
     * @param playerName The name of the player of on {@link BaseballCard}s to
     * look for.
     * @return A list of {@link BaseballCard}s which match the specified
     * player's name.
     * @throws IOException If any I/O errors occur while reading the underlying
     * storage mechanism.
     */
    public List<BaseballCard> getBaseballCardsByPlayerName(String playerName) throws IOException;

    /**
     * Updates data in the underlying persistent storage for the given card.
     *
     * @param card The card to update.
     * @throws IOException If any I/O errors occur while writing to the
     * underlying storage mechanism.
     */
    public void updateCard(BaseballCard card) throws IOException;

    /**
     * Updates data in the underlying persistent storage for all the cards in
     * the given List.
     *
     * @param cards The list of cards to update.
     * @throws IOException If any I/O errors occur while writing to the
     * underlying storage mechanism.
     */
    public void updateCards(List<BaseballCard> cards) throws IOException;
}
