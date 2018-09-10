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
package bbct.android.common.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

import bbct.android.common.R;

public class About extends Fragment {

    private static final String TAG = About.class.getName();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about, container, false);

        String aboutTitle = this.getString(R.string.about_title);
        String title = this.getString(R.string.bbct_title, aboutTitle);
        Activity activity = Objects.requireNonNull(getActivity());
        activity.setTitle(title);

        TextView versionLabel = view.findViewById(R.id.version_label);
        String versionNumber = this.getString(R.string.version_number);
        String versionString = this.getString(R.string.version_text, versionNumber);

        Log.d(TAG, "versionLabel=" + versionLabel);
        Log.d(TAG, "versionString=" + versionString);

        versionLabel.setText(versionString);

        return view;
    }
}
