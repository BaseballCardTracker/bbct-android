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
package bbct.android.common.activity.filter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardList;

/**
 * Base class for any {@link Activity} which requests parameter values for a
 * filter on the list of baseball cards displayed in {@link BaseballCardList}.
 */
public abstract class FilterActivity extends ActionBarActivity {

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState
     *            the icicle for this activity
     * @param layout
     *            the id of the XML layout file for this filter
     * @param titleResId
     *            the id of the string resource for the title of this filter
     */
    public void onCreate(Bundle savedInstanceState, int layout, int titleResId) {
        super.onCreate(savedInstanceState);
        this.setContentView(layout);

        String filterTitle = this.getString(titleResId);
        String title = this.getString(R.string.bbct_title, filterTitle);
        this.setTitle(title);

        Button okButton = (Button) this.findViewById(R.id.ok_button);
        okButton.setOnClickListener(this.onOk);

        Button cancelButton = (Button) this.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this.onCancel);
    }

    /**
     * Validate input fields which are defined by a subclass of
     * {@link FilterActivity}.
     *
     * @return {@code true} if the input is valid, {@code false} otherwise.
     */
    public abstract boolean validateInput();

    /**
     * Return an {@link Intent} with the parameter values defined by this
     * filter.
     *
     * @return an {@link Intent} with the parameter values defined by this
     *         filter
     */
    public abstract Intent getResult();
    private final View.OnClickListener onOk = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (FilterActivity.this.validateInput()) {
                Intent data = FilterActivity.this.getResult();
                FilterActivity.this.setResult(RESULT_OK, data);
                FilterActivity.this.finish();
            }
        }
    };
    private final View.OnClickListener onCancel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FilterActivity.this.setResult(RESULT_CANCELED);
            FilterActivity.this.finish();
        }
    };
}
