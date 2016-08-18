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
package bbct.data;

import java.io.Serializable;

/**
 * {@link BaseballCard} is the model underlying the BBCT application. It
 * contains properties for the brand, year, number, value, count, player name,
 * and player position.
 */
public class BaseballCard implements Serializable {

    /**
     * Serial Version ID
     */
    private static final long serialVersionUID = -4027752761089257290L;

    /**
     * Create a {@link BaseballCard} with the given values.
     *
     * @param brand
     *            The brand name.
     * @param year
     *            The year this card was published.
     * @param number
     *            The number on this card.
     * @param value
     *            The monetary value.
     * @param count
     *            The count of copies owned.
     * @param playerName
     *            The player on this card.
     * @param team
     *            The team for the player on this card.
     * @param playerPosition
     *            The position this player played.
     */
    public BaseballCard(boolean autographed, String condition, String brand,
            int year, int number, int value, int count, String playerName,
            String team, String playerPosition) {
        this.autographed = autographed;
        this.condition = condition;
        this.brand = brand;
        this.year = year;
        this.number = number;
        this.count = count;
        this.value = value;
        this.playerName = playerName;
        this.team = team;
        this.playerPosition = playerPosition;
    }

    /**
     * Is this {@link BaseballCard} autographed?
     *
     * @return Whether or not the card is autographed
     */
    public boolean isAutographed() {
        return this.autographed;
    }

    /**
     * Get the condition of the {@link BaseballCard}.
     *
     * @return The condition of the {@link BaseballCard}.
     */
    public String getCondition() {
        return this.condition;
    }

    /**
     * Get the brand name of this {@link BaseballCard}.
     *
     * @return The brand name of this {@link BaseballCard}.
     */
    public String getBrand() {
        return this.brand;
    }

    /**
     * Get the year of publication.
     *
     * @return The year of publication.
     */
    public int getYear() {
        return this.year;
    }

    /**
     * Get the number on this {@link BaseballCard}.
     *
     * @return The number of this {@link BaseballCard}.
     */
    public int getNumber() {
        return this.number;
    }

    /**
     * Get the monetary value of this {@link BaseballCard}.
     *
     * @return The monetary value of this {@link BaseballCard}.
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Set the monetary value of this {@link BaseballCard}.
     *
     * @param value
     *            The monetary value of this {@link BaseballCard}
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
        return this.count;
    }

    /**
     * Set the count of copies of this {@link BaseballCard}s owned.
     *
     * @param count
     *            The count of copies of this {@link BaseballCard}s owned.
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
        return this.playerName;
    }

    /**
     * Get the name of the team for which the player played.
     *
     * @return The player's team.
     */
    public String getTeam() {
        return this.team;
    }

    /**
     * Get the position played by the player on this {@link BaseballCard}.
     *
     * @return The position played by the player on this {@link BaseballCard}.
     */
    public String getPlayerPosition() {
        return this.playerPosition;
    }

    /**
     * Compare this {@link BaseballCard} for equality with another given
     * {@link Object}.
     *
     * @param o
     *            The {@link Object} to compare with this {@link BaseballCard}.
     * @return True if {@code o} is a {@link BaseballCard} with identical
     *         values. False, otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof BaseballCard) {
            BaseballCard c = (BaseballCard) o;

            return this.autographed == c.isAutographed()
                    && this.condition.equals(c.condition)
                    && this.brand.equals(c.getBrand())
                    && this.year == c.getYear()
                    && this.number == c.getNumber()
                    && this.value == c.getValue()
                    && this.count == c.getCount()
                    && this.playerName.equals(c.getPlayerName())
                    && this.team.equals(c.getTeam())
                    && this.playerPosition.equals(c.getPlayerPosition());
        }

        return false;
    }

    /**
     * Generate a hash code for this {@link BaseballCard}.
     *
     * @return The generated hash code.
     */
    @Override
    public int hashCode() {
        // Might throw NPE if any of this.brand, this.playerName, or
        // this.playerPosition is null.
        int hash = 7;
        hash = 67 * hash + Boolean.valueOf(this.autographed).hashCode();
        hash = 67 * hash + this.condition.hashCode();
        hash = 67 * hash + this.brand.hashCode();
        hash = 67 * hash + this.year;
        hash = 67 * hash + this.number;
        hash = 67 * hash + this.value;
        hash = 67 * hash + this.count;
        hash = 67 * hash + this.playerName.hashCode();
        hash = 67 * hash + this.team.hashCode();
        hash = 67 * hash + this.playerPosition.hashCode();
        return hash;
    }

    /**
     * Create a {@link String} representation containing the values of all
     * properties of this {@link BaseballCard}. This should only be used for
     * debugging purposes.
     *
     * @return A {@link String} representation containing the values of this
     *         {@link BaseballCard}.
     */
    @Override
    public String toString() {
        return "BaseballCard{" + "autographed=" + this.autographed
                + ", condition=" + this.condition + ", cardBrand=" + this.brand
                + ", cardYear=" + this.year + ", cardNumber=" + this.number
                + ", cardValue=" + this.value + ", cardCount=" + this.count
                + ", playerName=" + this.playerName + ", team=" + this.team
                + ", playerPosition=" + this.playerPosition + '}';
    }

    private final boolean autographed;
    private final String condition;
    private final String brand;
    private final int year;
    private final int number;
    private int value;
    private int count;
    private final String playerName;
    private final String team;
    private final String playerPosition;
}
