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
package bbct.android.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Checkable;
import bbct.android.common.R;

public class BaseballCardView extends CheckableLinearLayout {

    public BaseballCardView(Context context) {
        this(context, null);
    }

    public BaseballCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseballCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        ViewGroup root = (ViewGroup) ViewGroup.inflate(context, R.layout.baseball_card, this);
        mCheckable = (Checkable) root.findViewById(R.id.checkmark);
    }

}
