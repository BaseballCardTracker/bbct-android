/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2012 codeguru <codeguru@users.sourceforge.net>
 *
 * BBCT for Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BBCT for Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bbct.android.common;

import bbct.common.data.BaseballCard;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardCsvFileReader {
    
    public BaseballCardCsvFileReader(InputStream in, boolean hasColHeaders) throws IOException {
        this.in = new BufferedReader(new InputStreamReader(in));
        
        if (hasColHeaders) {
            this.in.readLine();
        }
    }
    
    public BaseballCard getNextBaseballCard() throws IOException {
        String line = this.in.readLine();
        String[] data = line.split(",");
        String brand = data[0];
        int year = Integer.parseInt(data[1]);
        int number = Integer.parseInt(data[2]);
        int value = 10000;
        int count = 1;
        String playerName = data[3];
        String playerPosition = data[4];

        return new BaseballCard(brand, year, number, value, count, playerName, playerPosition);
    }
    
    public boolean hasNextBaseballCard() throws IOException {
        return in.ready();
    }
    
    public List<BaseballCard> getAllBaseballCards() throws IOException {
        List<BaseballCard> cards = new ArrayList<BaseballCard>();
        while (this.hasNextBaseballCard()) {
            cards.add(this.getNextBaseballCard());
        }
        
        return cards;
    }
    
    public void close() throws IOException {
        this.in.close();
    }
    
    private BufferedReader in = null;
    
}
