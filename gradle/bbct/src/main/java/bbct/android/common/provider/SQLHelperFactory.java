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
package bbct.android.common.provider;

import android.content.Context;
import android.util.Log;
import bbct.android.R;
import bbct.android.common.exception.SQLHelperCreationException;
import java.lang.reflect.Constructor;

/**
 * Create a {@link BaseballCardSQLHelper} object for a given {@link Context}.
 */
final public class SQLHelperFactory {

    /**
     * Create a {@link BaseballCardSQLHelper} object for the given
     * {@link Context}. Uses the fully-qualified class name from the string
     * resource named
     * <code>sql_helper</code>.
     *
     * @param context The context containing the string resource with the name
     * of the {@link BaseballCardSQLHelper} subclass.
     * @return A {@link BaseballCardSQLHelper} object for the given
     * {@link Context}
     * @throws SQLHelperCreationException If an error occurs while creating the
     * {@link BaseballCardSQLHelper}.
     */
    public static BaseballCardSQLHelper getSQLHelper(Context context) throws SQLHelperCreationException {
        try {
            String sqlHelperClassName = context.getString(R.string.sql_helper);
            Class<?> sqlHelperClass = Class.forName(sqlHelperClassName);

            Log.d(TAG, "sqlHelperClass=" + sqlHelperClass.toString());

            Constructor<?> sqlHelperCtor = sqlHelperClass.getConstructor(Context.class);

            Log.d(TAG, "sqlHelperCtor=" + sqlHelperCtor.toString());

            return (BaseballCardSQLHelper) sqlHelperCtor.newInstance(context);
        } catch (Exception ex) {
            throw new SQLHelperCreationException(ex);
        }
    }
    private static final String TAG = SQLHelperFactory.class.getName();
}
