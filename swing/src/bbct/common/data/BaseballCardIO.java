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
package bbct.common.data;

import bbct.common.exceptions.BBCTIOException;
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
     * @throws BBCTIOException If an error occurs while closing the underlying
     * storage mechanism.
     */
    public void close() throws BBCTIOException;

    /**
     * Insert a {@link BaseballCard} to the underlying persistent storage.
     *
     * @param card The {@link BaseballCard} to insert.
     * @throws BBCTIOException If any I/O errors occur while inserting.
     */
    public void insertBaseballCard(BaseballCard card) throws BBCTIOException;

    /**
     * Insert a batch of {@link BaseballCard}s to the underlying persistent
     * storage.
     *
     * @param cards The {@link BaseballCard}s to insert.
     * @throws BBCTIOException If any I/O errors occur while inserting.
     */
    public void insertBaseballCards(List<BaseballCard> cards) throws BBCTIOException;

    /**
     * Search for {@link BaseballCard}s matching the specified year.
     *
     * @param year The year of {@link BaseballCard}s to look for.
     * @return A list of {@link BaseballCard}s which match the specified year.
     * @throws BBCTIOException If any I/O errors occur while reading the
     * underlying storage mechanism.
     */
    public List<BaseballCard> getBaseballCardsByYear(int year) throws BBCTIOException;

    /**
     * Search for {@link BaseballCard}s matching the specified number.
     *
     * @param number The number of {@link BaseballCard}s to look for.
     * @return A list of {@link BaseballCard}s which match the specified number.
     * @throws BBCTIOException If any I/O errors occur while reading the
     * underlying storage mechanism.
     */
    public List<BaseballCard> getBaseballCardsByNumber(int number) throws BBCTIOException;

    /**
     * Search for {@link BaseballCard}s matching the specified number.
     *
     * @param year The year of {@link BaseballCard}s to look for.
     * @param number The number of {@link BaseballCard}s to look for.
     * @return A list of {@link BaseballCard}s which match the specified year
     * and number.
     * @throws BBCTIOException If any I/O errors occur while reading the
     * underlying storage mechanism.
     */
    public List<BaseballCard> getBaseballCardsByYearAndNumber(int year, int number) throws BBCTIOException;

    /**
     * Search for {@link BaseballCard}s matching the specified player's name.
     *
     * @param playerName The name of the player of on {@link BaseballCard}s to
     * look for.
     * @return A list of {@link BaseballCard}s which match the specified
     * player's name.
     * @throws BBCTIOException If any I/O errors occur while reading the
     * underlying storage mechanism.
     */
    public List<BaseballCard> getBaseballCardsByPlayerName(String playerName) throws BBCTIOException;

    /**
     * Updates data in the underlying persistent storage for the given card.
     *
     * @param card The card to update.
     * @throws BBCTIOException If any I/O errors occur while writing to the
     * underlying storage mechanism.
     */
    public void updateBaseballCard(BaseballCard card) throws BBCTIOException;

    /**
     * Updates data in the underlying persistent storage for all the cards in
     * the given List.
     *
     * @param cards The list of cards to update.
     * @throws BBCTIOException If any I/O errors occur while writing to the
     * underlying storage mechanism.
     */
    public void updateBaseballCards(List<BaseballCard> cards) throws BBCTIOException;
    
    /**
     * Removes data in the underlying persistent storage for the given card.
     * 
     * @param card The card to remove.
     * @throws BBCTIOException If any I/O errors occur while writing to the
     * underlying storage mechanism.
     */
    public void removeBaseballCard(BaseballCard card) throws BBCTIOException;
    
    /**
     * Removes data in the underlying persistent storage for all the cards in
     * the given List.
     *
     * @param cards The list of cards to remove.
     * @throws BBCTIOException If any I/O errors occur while writing to the
     * underlying storage mechanism.
     */
    public void removeBaseballCards(List<BaseballCard> cards) throws BBCTIOException;
}
