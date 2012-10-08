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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

/**
 * {@link FilterOptions} gives the user the choice to filter the baseball card
 * list by year, number, year and number, or player name. A new activity is
 * loaded when the user clicks the OK button. This activity contains the correct
 * widgets to get input from the user for the parameters of the chosen filter
 * criteria.
 * 
 * TODO: Remove OK and Cancel buttons. Jump directly to the appropriate XxxFilter activity when user clicks a radio button.
 *
 * @see YearFilter
 * @see NumberFilter
 * @see YearAndNumberFilter
 * @see PlayerNameFilter
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class FilterOptions extends Activity {

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState Ignored
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.filter_options);

        String format = this.getString(R.string.bbct_title);
        String filterOptionsTitle = this.getString(R.string.filter_options_title);
        String title = String.format(format, filterOptionsTitle);
        this.setTitle(title);

        Button okButton = (Button) this.findViewById(R.id.filter_ok_button);
        okButton.setOnClickListener(this.onOkClick);

        Button cancelButton = (Button) this.findViewById(R.id.filter_cancel_button);
        cancelButton.setOnClickListener(this.onCancelClick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AndroidConstants.YEAR_FILTER_REQUEST:
            case AndroidConstants.NUMBER_FILTER_REQUEST:
            case AndroidConstants.YEAR_AND_NUMBER_FILTER_REQUEST:
            case AndroidConstants.PLAYER_NAME_FILTER_REQUEST:
                if (resultCode == RESULT_OK) {
                    this.setResult(RESULT_OK, data);
                    this.finish();
                }
                break;

            default:
                Log.e(TAG, "Invalid Activity request: " + requestCode);
                // TODO: Throw an exception?
                break;
        }
    }
    private View.OnClickListener onOkClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(TAG, "OK button clicked.");
            RadioGroup filterByRadioGroup = (RadioGroup) FilterOptions.this.findViewById(R.id.filter_by_radio_group);

            switch (filterByRadioGroup.getCheckedRadioButtonId()) {
                case NONE:
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FilterOptions.this);
                    dialogBuilder.setTitle(R.string.input_error_title);
                    dialogBuilder.setMessage(R.string.no_radio_button_error);
                    dialogBuilder.setPositiveButton(R.string.ok_button, FilterOptions.this.onDialogOkClick);
                    dialogBuilder.show();
                    break;

                case R.id.year_radio_button:
                    Log.d(TAG, "Year radio button selected.");
                    FilterOptions.this.startActivityForResult(new Intent(FilterOptions.this, YearFilter.class), AndroidConstants.YEAR_FILTER_REQUEST);
                    break;

                case R.id.number_radio_button:
                    FilterOptions.this.startActivityForResult(new Intent(FilterOptions.this, NumberFilter.class), AndroidConstants.NUMBER_FILTER_REQUEST);
                    break;

                case R.id.year_and_number_radio_button:
                    FilterOptions.this.startActivityForResult(new Intent(FilterOptions.this, YearAndNumberFilter.class), AndroidConstants.YEAR_AND_NUMBER_FILTER_REQUEST);
                    break;

                case R.id.player_name_radio_button:
                    FilterOptions.this.startActivityForResult(new Intent(FilterOptions.this, PlayerNameFilter.class), AndroidConstants.PLAYER_NAME_FILTER_REQUEST);
                    break;

                default:
                    Log.e(TAG, "Invalid radio button ID.");
                    // TODO: Throw an exception?
                    break;
            }
        }
    };
    private View.OnClickListener onCancelClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FilterOptions.this.setResult(RESULT_CANCELED);
            FilterOptions.this.finish();
        }
    };
    private DialogInterface.OnClickListener onDialogOkClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
        }
    };
    private static final String TAG = FilterOptions.class.getName();
    private static final int NONE = -1;
}
