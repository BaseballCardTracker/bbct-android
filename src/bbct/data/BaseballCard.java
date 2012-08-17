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
     * @return the value of value
     */
    public int getValue() {
        return value;
    }

    /**
     * Get the count of copies of this {@link BaseballCard}s owned
     *
     * @return the value of count
     */
    public int getCount() {
        return count;
    }

    /**
     * Get the name of the player on this {@link BaseballCard}.
     *
     * @return the value of playerName
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Get the position played by the player on this {@link BaseballCard}.
     *
     * @return the value of playerPosition
     */
    public String getPlayerPosition() {
        return playerPosition;
    }

    /**
     * Compare this {@link BaseballCard} with any other {@link java.lang.Object}
     * for equality.
     *
     * @param o The {@link java.lang.Object} to compare with this {@link BaseballCard}.
     * @return True if
     * <code>o</code> is a {@link BaseballCard} with identical values. False,
     * otherwise.
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
        return "BaseballCard{" + "cardBrand=" + brand + ", cardYear=" + year + ", cardNumber=" + number + ", cardValue=" + value + ", cardCount=" + count + ", playerName=" + playerName + ", playerPosition=" + playerPosition + '}';
    }
    private String brand = "";
    private int year = 0;
    private int number = 0;
    private int value = 0;
    private int count = 0;
    private String playerName = "";
    private String playerPosition = "";
}
