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
package bbct.android.common.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "baseball_cards",
        indices = {@Index(value = {"brand", "year", "number"}, unique = true)})
public class BaseballCard {
    @PrimaryKey
    public int id;
    public boolean autographed;
    public String condition;
    public String brand;
    public int year;
    public int number;
    public int value;
    public int quantity;
    @ColumnInfo(name = "player_name")
    public String playerName;
    public String team;
    public String position;

    public BaseballCard(boolean autographed, String condition, String brand,
                        int year, int number, int value, int quantity,
                        String playerName, String team, String position) {
        this.autographed = autographed;
        this.condition = condition;
        this.brand = brand;
        this.year = year;
        this.number = number;
        this.quantity = quantity;
        this.value = value;
        this.playerName = playerName;
        this.team = team;
        this.position= position;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BaseballCard) {
            BaseballCard c = (BaseballCard) o;

            return this.autographed == c.autographed
                    && this.condition.equals(c.condition)
                    && this.brand.equals(c.brand)
                    && this.year == c.year
                    && this.number == c.number
                    && this.value == c.value
                    && this.quantity == c.quantity
                    && this.playerName.equals(c.playerName)
                    && this.team.equals(c.team)
                    && this.position.equals(c.playerName);
        }

        return false;
    }

    @Override
    public int hashCode() {
        // Might throw NPE if any of this.condition, this.brand,
        // this.playerName, this.team, or this.position is null.
        int hash = 7;
        hash = 67 * hash + Boolean.valueOf(this.autographed).hashCode();
        hash = 67 * hash + this.condition.hashCode();
        hash = 67 * hash + this.brand.hashCode();
        hash = 67 * hash + this.year;
        hash = 67 * hash + this.number;
        hash = 67 * hash + this.value;
        hash = 67 * hash + this.quantity;
        hash = 67 * hash + this.playerName.hashCode();
        hash = 67 * hash + this.team.hashCode();
        hash = 67 * hash + this.position.hashCode();
        return hash;
    }
}
