/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2014 codeguru <codeguru@users.sourceforge.net>
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
package bbct.android.lite.provider;

import bbct.android.common.provider.BaseballCardContract;
import bbct.android.common.provider.BaseballCardProvider;

public class LiteProvider extends BaseballCardProvider {

    static {
        uriMatcher.addURI(BaseballCardContract.LITE_AUTHORITY,
                BaseballCardContract.TABLE_NAME, ALL_CARDS);
        uriMatcher.addURI(BaseballCardContract.LITE_AUTHORITY,
                BaseballCardContract.TABLE_NAME + "/#", CARD_ID);
        uriMatcher.addURI(BaseballCardContract.LITE_AUTHORITY,
                BaseballCardContract.TABLE_NAME + "/distinct", DISTINCT);
    }

}
