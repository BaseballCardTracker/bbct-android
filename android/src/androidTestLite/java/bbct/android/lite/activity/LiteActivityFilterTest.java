/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2016 codeguru <codeguru@users.sourceforge.net>
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
package bbct.android.lite.activity;

import bbct.android.common.fragment.FilterCardsCombinationTest;
import bbct.android.common.test.BBCTTestUtil;

import java.util.Set;
import junit.framework.Test;

public class LiteActivityFilterTest extends FilterCardsCombinationTest<LiteActivity> {
    public LiteActivityFilterTest(Set<BBCTTestUtil.FilterOption> inputFieldsFlags) {
        super(LiteActivity.class, inputFieldsFlags);
    }

    public static Test suite() throws ReflectiveOperationException {
        return FilterCardsCombinationTest.suite(LiteActivityFilterTest.class);
    }
}
