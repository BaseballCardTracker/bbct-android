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

import android.annotation.SuppressLint;
import bbct.android.common.SharedPreferenceKeys;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import static bbct.android.common.activity.MainActivity.SURVEY_DELAY;

public class Survey2SharedPreferencesTestRule extends SharedPreferencesTestRule {
    @SuppressLint("CommitPrefEdits")
    @Override
    protected void before() throws Throwable {
        super.before();

        DateFormat dateFormat = DateFormat.getDateInstance();
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DATE, -SURVEY_DELAY);
        prefs.edit().putString(SharedPreferenceKeys.SURVEY1_DATE, dateFormat.format(cal.getTime())).commit();
    }
}
