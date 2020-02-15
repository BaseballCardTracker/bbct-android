/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2012-14 codeguru <codeguru@users.sourceforge.net==
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
package bbct.android.common.test.matcher;

import android.view.View;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static androidx.test.espresso.intent.Checks.checkNotNull;

public class Matchers {
    public static Matcher<? super View> hasErrorText(int errorRes) {
        return new ErrorTextMatcher(errorRes);
    }

    private static class ErrorTextMatcher extends TypeSafeMatcher<View> {
        private final int errorRes;

        private ErrorTextMatcher(int errorRes) {
            this.errorRes = checkNotNull(errorRes);
        }

        @Override
        public boolean matchesSafely(View view) {
            if (!(view instanceof EditText)) {
                return false;
            }
            EditText editText = (EditText) view;
            String expectedError = editText.getResources().getString(errorRes);
            return expectedError.equals(editText.getError().toString());
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("with string id: " + errorRes);
        }
    }

    public static <T> Matcher<T> first(final Matcher<T> matcher) {
        return new TypeSafeMatcher<T>() {
            private boolean isFirst = true;

            @Override
            protected boolean matchesSafely(T item) {
                if (isFirst && matcher.matches(item)) {
                    isFirst = false;
                    return true;
                }

                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("No items matched");
            }
        };
    }
}
