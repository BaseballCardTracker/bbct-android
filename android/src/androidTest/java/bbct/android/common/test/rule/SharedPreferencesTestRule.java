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
package bbct.android.common.test.rule;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.rules.ExternalResource;

import bbct.android.common.SharedPreferenceKeys;

public class SharedPreferencesTestRule extends ExternalResource {
    SharedPreferences prefs;

    @Override
    protected void before() throws Throwable {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        prefs = context.getSharedPreferences(SharedPreferenceKeys.PREFS, Context.MODE_PRIVATE);
    }

    @Override
    protected void after() {
        prefs.edit().clear().apply();
    }

    public SharedPreferences getPrefs() {
        return prefs;
    }
}
