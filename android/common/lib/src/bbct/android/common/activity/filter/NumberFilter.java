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

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import bbct.android.common.R;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class NumberFilter extends FilterActivity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.number_filter, R.string.number_filter_title);

        this.numberText = (EditText) this.findViewById(R.id.number_text);
    }

    @Override
    public boolean validateInput() {
        String numberStr = this.numberText.getText().toString();
        boolean validNumber = !numberStr.equals("");

        if (!validNumber) {
            this.numberText.setError(this.getString(R.string.number_input_error));
        }

        return validNumber;
    }

    @Override
    public Intent getResult() {
        String numberStr = this.numberText.getText().toString();
        int number = Integer.parseInt(numberStr);
        Intent result = new Intent();
        result.putExtra(this.getString(R.string.filter_request_extra), R.id.number_filter_request);
        result.putExtra(this.getString(R.string.number_extra), number);

        return result;
    }
    private EditText numberText = null;
}
