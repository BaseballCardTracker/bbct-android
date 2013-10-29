
/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2012 codeguru <codeguru@users.sourceforge.net>
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
import android.view.View;
import android.widget.Button;
import bbct.android.common.R;

/**
 *
 */
public abstract class FilterActivity extends Activity {

    /**
     * Called when the activity is first created.
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

    public abstract boolean validateInput();

    public abstract Intent getResult();
    private View.OnClickListener onOk = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (FilterActivity.this.validateInput()) {
                Intent data = FilterActivity.this.getResult();
                FilterActivity.this.setResult(RESULT_OK, data);
                FilterActivity.this.finish();
            }
        }
    };
    private View.OnClickListener onCancel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FilterActivity.this.setResult(RESULT_CANCELED);
            FilterActivity.this.finish();
        }
    };
}
