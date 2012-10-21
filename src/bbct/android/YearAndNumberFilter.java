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
package bbct.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class YearAndNumberFilter extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.year_and_number_filter);

        String format = this.getString(R.string.bbct_title);
        String yearAndNumberFilterTitle = this.getString(R.string.year_and_number_filter_title);
        String title = String.format(format, yearAndNumberFilterTitle);
        this.setTitle(title);

        this.yearText = (EditText) this.findViewById(R.id.year_text);
        this.numberText = (EditText) this.findViewById(R.id.number_text);

        Button okButton = (Button) this.findViewById(R.id.ok_button);
        okButton.setOnClickListener(this.onOk);

        Button cancelButton = (Button) this.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this.onCancel);
    }
    private View.OnClickListener onOk = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String yearStr = YearAndNumberFilter.this.yearText.getText().toString();
            String numberStr = YearAndNumberFilter.this.numberText.getText().toString();
            if (yearStr.equals("")) {
                YearAndNumberFilter.this.yearText.requestFocus();
                Toast.makeText(YearAndNumberFilter.this, R.string.year_input_error, Toast.LENGTH_LONG).show();
            } else if (numberStr.equals("")) {
                YearAndNumberFilter.this.numberText.requestFocus();
                Toast.makeText(YearAndNumberFilter.this, R.string.number_input_error, Toast.LENGTH_LONG).show();
            } else {
                int year = Integer.parseInt(yearStr);
                int number = Integer.parseInt(numberStr);
                Intent data = new Intent();
                data.putExtra(YearAndNumberFilter.this.getString(R.string.filter_request_extra), R.id.year_and_number_filter_request);
                data.putExtra(YearAndNumberFilter.this.getString(R.string.year_extra), year);
                data.putExtra(YearAndNumberFilter.this.getString(R.string.number_extra), number);
                YearAndNumberFilter.this.setResult(RESULT_OK, data);
                YearAndNumberFilter.this.finish();
            }
        }
    };
    private View.OnClickListener onCancel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            YearAndNumberFilter.this.setResult(RESULT_CANCELED);
            YearAndNumberFilter.this.finish();
        }
    };
    private EditText yearText = null;
    private EditText numberText = null;
}
