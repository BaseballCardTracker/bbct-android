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
 * TODO: JavaDoc
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public interface BaseballCardIO {
    
    /**
     * 
     * @throws IOException
     */
    public void close() throws IOException;
    
    /**
     * 
     * @param card
     * @throws IOException
     */
    public void insertBaseballCard(BaseballCard card) throws IOException;
    
    /**
     * 
     * @param year
     * @return
     * @throws IOException
     */
    public List<BaseballCard> getBaseballCardsByYear(int year) throws IOException;
    
    /**
     * 
     * @param number
     * @return
     * @throws IOException
     */
    public List<BaseballCard> getBaseballCardsByNumber(int number) throws IOException;
    
    /**
     * 
     * @param year
     * @param number
     * @return
     * @throws IOException
     */
    public List<BaseballCard> getBaseballCardsByYearAndNumber(int year, int number) throws IOException;

    /**
     * 
     * @param playerName
     * @return
     * @throws IOException
     */
    public List<BaseballCard> getBaseballCardsByPlayerName(String playerName) throws IOException;
}
