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

import java.util.Objects;

/**
 * {@link BaseballCard} is the model underlying the BBCT application. It
 * contains properties for the brand, year, number, value, count, player name,
 * and player position.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCard {

    /**
     * Create a {@link BaseballCard} with the given values.
     *
     * @param brand The brand name.
     * @param year The year this card was published.
     * @param number The number on this card.
     * @param value The monetary value.
     * @param count The count of copies owned.
     * @param playerName The player on this card.
     * @param playerPosition The position this player played.
     */
    public BaseballCard(String brand, int year, int number, int value, int count, String playerName, String playerPosition) {
        this.brand = brand;
        this.year = year;
        this.number = number;
        this.count = count;
        this.value = value;
        this.playerName = playerName;
        this.playerPosition = playerPosition;
    }

    /**
     * Get the brand name of this {@link BaseballCard}.
     *
     * @return The brand name of this {@link BaseballCard}.
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Get the year of publication.
     *
     * @return The year of publication.
     */
    public int getYear() {
        return year;
    }

    /**
     * Get the number on this {@link BaseballCard}.
     *
     * @return The number of this {@link BaseballCard}.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Get the monetary value of this {@link BaseballCard}.
     *
     * @return The monetary value of this {@link BaseballCard}.
     */
    public int getValue() {
        return value;
    }

    /**
     * Set the monetary value of this {@link BaseballCard}.
     *
     * @param value The monetary value of this {@link BaseballCard}
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Get the count of copies of this {@link BaseballCard}s owned.
     *
     * @return The count of copies of this {@link BaseballCard}s owned.
     */
    public int getCount() {
        return count;
    }

    /**
     * Set the count of copies of this {@link BaseballCard}s owned.
     *
     * @param count The count of copies of this {@link BaseballCard}s owned.
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Get the name of the player on this {@link BaseballCard}.
     *
     * @return The name of the player on this {@link BaseballCard}.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Get the position played by the player on this {@link BaseballCard}.
     *
     * @return The position played by the player on this {@link BaseballCard}.
     */
    public String getPlayerPosition() {
        return playerPosition;
    }

    /**
     * Compare this {@link BaseballCard} with any other {@link java.lang.Object}
     * for equality.
     *
     * @param o The {@link java.lang.Object} to compare with this
     * {@link BaseballCard}.
     * @return True if <code>o</code> is a {@link BaseballCard} with identical
     * values. False, otherwise.
     */
    @Override
    public boolean equals(Object o) {
        BaseballCard c = (BaseballCard) o;

        return this.brand.equals(c.getBrand())
                && this.year == c.getYear()
                && this.number == c.getNumber()
                && this.value == c.getValue()
                && this.count == c.getCount()
                && this.playerName.equals(c.getPlayerName())
                && this.playerPosition.equals(c.getPlayerPosition());
    }

    /**
     * Generate a hash code for this {@link BaseballCard}.
     *
     * @return The generated hash code.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.brand);
        hash = 67 * hash + this.year;
        hash = 67 * hash + this.number;
        hash = 67 * hash + this.value;
        hash = 67 * hash + this.count;
        hash = 67 * hash + Objects.hashCode(this.playerName);
        hash = 67 * hash + Objects.hashCode(this.playerPosition);
        return hash;
    }

    /**
     * Create a {@link java.lang.String} representation containing the values of
     * all properties of this {@link BaseballCard}. This should only be used for
     * debugging purposes.
     *
     * @return A {@link java.lang.String} representation containing the values
     * of this {@link BaseballCard}.
     */
    @Override
    public String toString() {
        return "BaseballCard{" + "cardBrand=" + brand + ", cardYear=" + year
                + ", cardNumber=" + number + ", cardValue=" + value
                + ", cardCount=" + count + ", playerName=" + playerName
                + ", playerPosition=" + playerPosition + '}';
    }
    private String brand = "";
    private int year = 0;
    private int number = 0;
    private int value = 0;
    private int count = 0;
    private String playerName = "";
    private String playerPosition = "";
}
