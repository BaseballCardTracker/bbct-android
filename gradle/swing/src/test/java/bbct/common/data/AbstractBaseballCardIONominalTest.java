/*
 * This file is part of bbct.
 *
 * Copyright &copy; 2012 codeguru <codeguru@users.sourceforge.net>
 *
 * bbct is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * bbct is distributed in the hope that it will be useful,
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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class AbstractBaseballCardIONominalTest {

    /**
     *
     */
    public AbstractBaseballCardIONominalTest() {
    }

    /**
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    /**
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     *
     */
    @Before
    public void setUp() {
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    /**
     * Test for {@link AbstractBaseballCardIO#updateBaseballCards}.
     *
     * @throws Exception
     */
    @Test
    public void testUpdateCards() throws Exception {
        System.out.println("updateCards");
        List<BaseballCard> cards = null;
        AbstractBaseballCardIO instance = new AbstractBaseballCardIOImpl();
        instance.updateBaseballCards(cards);
        // TODO review the generated test code and remove the default call to fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test for {@link AbstractBaseballCardIO#removeBaseballCards}.
     *
     * @throws Exception
     */
    @Test
    public void testDeleteCards() throws Exception {
        // TODO add the actual test once the rest of the methods are complete.
        Assert.fail("The test case is a prototype.");
    }

    /**
     *
     */
    public class AbstractBaseballCardIOImpl extends AbstractBaseballCardIO {

        @Override
        public void close() throws BBCTIOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void insertBaseballCard(BaseballCard card) throws BBCTIOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public List<BaseballCard> getBaseballCardsByYear(int year) throws BBCTIOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public List<BaseballCard> getBaseballCardsByNumber(int number) throws BBCTIOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public List<BaseballCard> getBaseballCardsByYearAndNumber(int year, int number) throws BBCTIOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public List<BaseballCard> getBaseballCardsByPlayerName(String playerName) throws BBCTIOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateBaseballCard(BaseballCard card) throws BBCTIOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        @Override
        public void removeBaseballCard(BaseballCard card) throws BBCTIOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
