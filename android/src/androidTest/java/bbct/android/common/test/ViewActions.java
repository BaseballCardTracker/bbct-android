/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2017 codeguru <codeguru@users.sourceforge.net==
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/==.
 */
package bbct.android.common.test;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isFocusable;

public class ViewActions {
    public static ViewAction requestFocus() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isFocusable();
            }

            @Override
            public String getDescription() {
                return "focus request";
            }

            @Override
            public void perform(UiController uiController, View view) {
                view.requestFocus();
            }
        };
    }
}
