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
package bbct.data.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import bbct.data.BaseballCard;

public class BaseballCardNominalTest {
    private BaseballCard instance = null;
    private boolean autographed = true;
    private String condition = "Excellent";
    private String brand = "Topps";
    private int year = 1991;
    private int num = 278;
    private int value = 10100;
    private int count = 1;
    private String playerName = "Alex Fernandez";
    private String team = "Dodgers";
    private String playerPosition = "Pitcher";

    @Before
    public void setUp() {
        this.instance = new BaseballCard(autographed, condition, brand, year, num, value, count, playerName, team, playerPosition);
    }

    /**
     * Test for {@link BaseballCard#getBrand}.
     */
    @Test
    public void testGetBrand() {
        Assert.assertEquals(this.brand, this.instance.getBrand());
    }

    /**
     * Test for {@link BaseballCard#getYear}.
     */
    @Test
    public void testGetYear() {
        Assert.assertEquals(this.year, this.instance.getYear());
    }

    /**
     * Test for {@link BaseballCard#getNumber}.
     */
    @Test
    public void testGetNumber() {
        Assert.assertEquals(this.num, this.instance.getNumber());
    }

    /**
     * Test for {@link BaseballCard#getValue}.
     */
    @Test
    public void testGetValue() {
        Assert.assertEquals(this.value, this.instance.getValue());
    }

    /**
     * Test of setValue method, of class BaseballCard.
     */
    @Test
    public void testSetValue() {
        int newValue = 20000;
        this.instance.setValue(newValue);
        Assert.assertEquals(newValue, this.instance.getValue());
    }

    /**
     * Test for {@link BaseballCard#getCount}.
     */
    @Test
    public void testGetCount() {
        Assert.assertEquals(this.count, this.instance.getCount());
    }

    /**
     * Test of setCount method, of class BaseballCard.
     */
    @Test
    public void testSetCount() {
        int newCount = 2;
        instance.setCount(newCount);
        Assert.assertEquals(newCount, this.instance.getCount());
    }

    /**
     * Test for {@link BaseballCard#getPlayerName}.
     */
    @Test
    public void testGetPlayerName() {
        Assert.assertEquals(this.playerName, this.instance.getPlayerName());
    }

    /**
     * Test for {@link BaseballCard#getPlayerPosition}.
     */
    @Test
    public void testGetPlayerPosition() {
        Assert.assertEquals(this.playerPosition, this.instance.getPlayerPosition());
    }

    /**
     * Test for {@link BaseballCard#equals}.
     */
    @Test
    public void testEquals() {
        BaseballCard card1 = new BaseballCard(autographed, condition, brand, year, num, value, count, playerName, team, playerPosition);
        BaseballCard card2 = new BaseballCard(autographed, condition, brand, year, num, value, count, playerName, team, playerPosition);
        Assert.assertTrue(card1.equals(card2));
    }

    /**
     * Test for {@link BaseballCard#hashCode}.
     */
    @Test
    public void testHashCode() {
        BaseballCard card1 = new BaseballCard(autographed, condition, brand, year, num, value, count, playerName, team, playerPosition);
        BaseballCard card2 = new BaseballCard(autographed, condition, brand, year, num, value, count, playerName, team, playerPosition);
        Assert.assertTrue(card1.hashCode() == card2.hashCode());
    }

    /**
     * Test for {@link BaseballCard#toString}.
     */
    @Test
    public void testToString() {
        String expResult = "BaseballCard{"
                + "autographed=" + this.autographed
                + ", condition=" + this.condition
                + ", cardBrand=" + this.brand
                + ", cardYear=" + this.year + ", cardNumber=" + this.num
                + ", cardValue=" + this.value + ", cardCount=" + this.count
                + ", playerName=" + this.playerName
                + ", team=" + this.team
                + ", playerPosition=" + this.playerPosition + '}';
        String result = this.instance.toString();
        Assert.assertEquals(expResult, result);
    }
}
