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

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import bbct.android.common.R;

/**
 *
 */
public class YearFilter extends FilterActivity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.year_filter, R.string.year_filter_title);

        this.yearText = (EditText) this.findViewById(R.id.year_text);
    }

    @Override
    public boolean validateInput() {
        String yearStr = this.yearText.getText().toString();
        boolean validYear = !yearStr.equals("");

        if (!validYear) {
            this.yearText.setError(this.getString(R.string.year_input_error));
        }

        return validYear;
    }

    @Override
    public Intent getResult() {
        String yearStr = this.yearText.getText().toString();
        int year = Integer.parseInt(yearStr);
        int requestCode = this.getResources().getInteger(R.integer.year_filter_request);
        Intent result = new Intent();
        result.putExtra(this.getString(R.string.filter_request_extra), requestCode);
        result.putExtra(this.getString(R.string.year_extra), year);

        return result;
    }
    private EditText yearText = null;
}
