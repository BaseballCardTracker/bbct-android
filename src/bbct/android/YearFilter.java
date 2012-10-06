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
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class YearFilter extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.year_filter);

        String format = this.getString(R.string.bbct_title);
        String yearFilterTitle = this.getString(R.string.year_filter_title);
        String title = String.format(format, yearFilterTitle);
        this.setTitle(title);

        this.yearText = (EditText) this.findViewById(R.id.year_filter_year_text);

        Button okButton = (Button) this.findViewById(R.id.year_filter_ok_button);
        okButton.setOnClickListener(this.onOk);

        Button cancelButton = (Button) this.findViewById(R.id.year_filter_cancel_button);
        cancelButton.setOnClickListener(this.onCancel);
    }
    private View.OnClickListener onOk = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BaseballCardSQLHelper sqlHelper = new BaseballCardSQLHelper(YearFilter.this);
            // TODO: Error checking
            int year = Integer.parseInt(YearFilter.this.yearText.getText().toString());
            
            sqlHelper.filterCursorByYear(year);
            Cursor cursor = sqlHelper.getCursor();
            // TODO: Return Cursor to FilterOptions
        }
    };
    private View.OnClickListener onCancel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // TODO: Return RESULT_CANCELED
            YearFilter.this.finish();
        }
    };
    private EditText yearText = null;
}
