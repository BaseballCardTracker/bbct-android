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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class FilterOptions extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.filter_options);

        String title = this.getString(R.string.app_name) + " - " + this.getString(R.string.filter_options_title);
        this.setTitle(title);

        Button okButton = (Button) this.findViewById(R.id.filter_ok_button);
        okButton.setOnClickListener(this.onOkClick);
        
        Button cancelButton = (Button) this.findViewById(R.id.filter_cancel_button);
        cancelButton.setOnClickListener(this.onCancelClick);
    }
    private View.OnClickListener onOkClick = new View.OnClickListener() {
        public void onClick(View view) {
            Log.d(TAG, "OK button clicked.");
            RadioGroup findBy = (RadioGroup) FilterOptions.this.findViewById(R.id.filter_by_radio_group);
            
            Log.d(TAG, "findBy:" + findBy);

            switch (findBy.getCheckedRadioButtonId()) {
                case NONE:
                    // TODO: Show an error message in a dialog.
                    break;
                
                case R.id.year_radio_button:
                    Log.d(TAG, "Year radio button selected.");
                    FilterOptions.this.startActivity(new Intent(FilterOptions.this, YearFilter.class));
                    break;

                case R.id.number_radio_button:
                    FilterOptions.this.startActivity(new Intent(FilterOptions.this, NumberFilter.class));
                    break;

                case R.id.year_and_number_radio_button:
                    FilterOptions.this.startActivity(new Intent(FilterOptions.this, YearAndNumberFilter.class));
                    break;

                case R.id.player_name_radio_button:
                    FilterOptions.this.startActivity(new Intent(FilterOptions.this, PlayerNameFilter.class));
                    break;

                default:
                    Log.e(TAG, "Invalid radio button ID.");
            }
        }
    };
    private View.OnClickListener onCancelClick = new View.OnClickListener() {
        public void onClick(View view) {
            FilterOptions.this.finish();
        }
    };
    private static final String TAG = FilterOptions.class.getName();
    private static final int NONE = -1;
}
