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
package bbct.android.common.test.util;

import bbct.android.common.data.BaseballCard;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class reads baseball card data from an input stream which is formatted
 * as comma-separated values.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardCsvFileReader {

    /**
     * Create a {@link BaseballCardCsvFileReader} object which reads baseball
     * card data as comma-separated values from the given {@link InputStream}.
     * The input may contain column headers, which will be ignored.
     *
     * @param in The {@link InputStream} containing the comma-separated values.
     * @param hasColHeaders Whether or not the input contains column headers.
     * @throws IOException If an error occurs while reading the input.
     */
    public BaseballCardCsvFileReader(InputStream in, boolean hasColHeaders) throws IOException {
        this.in = new BufferedReader(new InputStreamReader(in));

        if (hasColHeaders) {
            this.in.readLine();
        }
    }

    /**
     * Reads baseball card data from the next line of comma-spearated values.
     *
     * @return A {@link BaseballCard} containing the data from the input stream.
     * @throws IOException If an error occurs while reading the input.
     */
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

    /**
     * Determine if the input stream contains more baseball card data. If the
     * input stream is ready for an input operation, then this function returns
     * <code>true</code>. The validity of the data is determined only after
     * calling {@link #getNextBaseballCard() } or {@link #getAllBaseballCards()
     * }.
     *
     * @return <code>true</code> if the input stream is ready for an input
     * operation; <code>false</code>, otherwise.
     * @throws IOException If an error occurs while reading the input.
     */
    public boolean hasNextBaseballCard() throws IOException {
        return in.ready();
    }

    /**
     * Reads all the baseball card data from the input stream.
     *
     * @return A list of {@link BaseballCard} objects containing all of the
     * baseball card data from the input stream.
     * @throws IOException If an error occurs while reading the input.
     */
    public List<BaseballCard> getAllBaseballCards() throws IOException {
        List<BaseballCard> cards = new ArrayList<BaseballCard>();
        while (this.hasNextBaseballCard()) {
            cards.add(this.getNextBaseballCard());
        }

        return cards;
    }

    /**
     * Close the input stream.
     *
     * @throws IOException If an error occurs while reading the input.
     */
    public void close() throws IOException {
        this.in.close();
    }
    private BufferedReader in = null;
}
