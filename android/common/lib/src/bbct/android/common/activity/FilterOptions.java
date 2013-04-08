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
package bbct.android.common.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import bbct.android.common.R;
import bbct.android.common.activity.filter.FilterActivity;
import bbct.android.common.activity.filter.NumberFilter;
import bbct.android.common.activity.filter.PlayerNameFilter;
import bbct.android.common.activity.filter.TeamFilter;
import bbct.android.common.activity.filter.YearAndNumberFilter;
import bbct.android.common.activity.filter.YearFilter;
import bbct.android.common.activity.util.DialogUtil;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link FilterOptions} gives the user the choice to filter the baseball card
 * list by year, number, year and number, or player name. A new activity is
 * loaded when the user clicks the OK button. This activity contains the correct
 * widgets to get input from the user for the parameters of the chosen filter
 * criteria.
 *
 * TODO: Remove OK and Cancel buttons. Jump directly to the appropriate
 * XxxFilter activity when user clicks a radio button.
 *
 * @see YearFilter
 * @see NumberFilter
 * @see YearAndNumberFilter
 * @see PlayerNameFilter
 * @see TeamFilter
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class FilterOptions extends Activity {

    /**
     * Register a {@link FilterActivity} which should be launched when the
     * {@link RadioButton} with the given id is selected.
     *
     * @param id The radio button associated with the given
     * {@link FilterActivity} class.
     * @param filterActivityClass The {@link Class} for the
     * {@link FilterActivity} which should be launched when the
     * {@link RadioButton} with the given id is selected.
     */
    public static void registerFilterActivity(int id, Class<? extends FilterActivity> filterActivityClass) {
        Log.d(TAG, "registerFilterActivity()");
        Log.d(TAG, "id=" + id + ", filterActivityClass=" + filterActivityClass);

        filterActivities.put(id, filterActivityClass);
    }

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

        Button okButton = (Button) this.findViewById(R.id.ok_button);
        okButton.setOnClickListener(this.onOk);

        Button cancelButton = (Button) this.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this.onCancel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == R.id.year_filter_request
                || requestCode == R.id.number_filter_request
                || requestCode == R.id.year_and_number_filter_request
                || requestCode == R.id.player_name_filter_request
                || requestCode == R.id.team_filter_request) {
            if (resultCode == RESULT_OK) {
                this.setResult(RESULT_OK, data);
                this.finish();
            }
        } else {
            Log.e(TAG, "onActivityResult(): Invalid Activity request: " + requestCode);
            // TODO: Throw an exception?
        }
    }
    private View.OnClickListener onOk = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // TODO Remove Ok and Cancel buttons and launch FilterActivities immediately after user clicks the radio button

            Log.d(TAG, "OK button clicked.");
            RadioGroup filterByRadioGroup = (RadioGroup) FilterOptions.this.findViewById(R.id.filter_options_radio_group);

            int radioButtonId = filterByRadioGroup.getCheckedRadioButtonId();
            Log.d(TAG, "radioButtonId=" + radioButtonId);

            if (radioButtonId == NONE) {
                DialogUtil.showErrorDialog(FilterOptions.this, R.string.input_error_title, R.string.no_radio_button_error);
            } else if (radioButtonId == R.id.year_filter_radio_button) {
                Log.d(TAG, "Year radio button selected.");
                FilterOptions.this.startActivityForResult(new Intent(FilterOptions.this, YearFilter.class), R.id.year_filter_request);
            } else if (radioButtonId == R.id.number_filter_radio_button) {
                FilterOptions.this.startActivityForResult(new Intent(FilterOptions.this, NumberFilter.class), R.id.number_filter_request);
            } else if (radioButtonId == R.id.year_and_number_filter_radio_button) {
                FilterOptions.this.startActivityForResult(new Intent(FilterOptions.this, YearAndNumberFilter.class), R.id.year_and_number_filter_request);
            } else if (radioButtonId == R.id.player_name_filter_radio_button) {
                FilterOptions.this.startActivityForResult(new Intent(FilterOptions.this, PlayerNameFilter.class), R.id.player_name_filter_request);
            } else if (radioButtonId == R.id.team_filter_radio_button) {
                FilterOptions.this.startActivityForResult(new Intent(FilterOptions.this, TeamFilter.class), R.id.team_filter_request);
            } else {
                Log.e(TAG, "Invalid radio button ID.");
                // TODO: Throw an exception?
            }
        }
    };
    private View.OnClickListener onCancel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FilterOptions.this.setResult(RESULT_CANCELED);
            FilterOptions.this.finish();
        }
    };
    private static Map<Integer, Class<? extends FilterActivity>> filterActivities = new HashMap<Integer, Class<? extends FilterActivity>>();
    private static final String TAG = FilterOptions.class.getName();
    private static final int NONE = -1;
}
