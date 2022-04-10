/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2012-14 codeguru <codeguru@users.sourceforge.net>
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
package bbct.android.common.layout;

import org.junit.Rule;
import org.junit.Test;

import bbct.android.common.R;
import bbct.android.common.fragment.BaseballCardDetails;
import bbct.android.common.test.rule.SupportFragmentTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasFocus;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

public class BaseballCardDetailsLayoutTest {
    @Rule
    public SupportFragmentTestRule fragmentTestRule
            = new SupportFragmentTestRule(new BaseballCardDetails());

    @Test
    public void testAutographedCheckBox() {
        onView(withId(R.id.autograph)).check(matches(allOf(isDisplayed(), isNotChecked())));
    }

    @Test
    public void testBrandEditText() {
        onView(withId(R.id.brand_text)).check(matches(allOf(isDisplayed(), hasFocus())));
    }

}
