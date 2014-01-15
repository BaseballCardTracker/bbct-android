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
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import bbct.android.common.R;
import bbct.android.common.activity.filter.FilterActivity;
import bbct.android.common.activity.filter.NumberFilter;
import bbct.android.common.activity.filter.PlayerNameFilter;
import bbct.android.common.activity.filter.TeamFilter;
import bbct.android.common.activity.filter.YearAndNumberFilter;
import bbct.android.common.activity.filter.YearFilter;
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
 */
public class FilterOptions extends Activity {

    /**
     * Register a {@link FilterActivity} which should be launched when the
     * {@link RadioButton} with the given id is selected.
     *
     * @param id
     *            The radio button associated with the given
     *            {@link FilterActivity} class.
     * @param filterActivityClass
     *            The {@link Class} for the {@link FilterActivity} which should
     *            be launched when the {@link RadioButton} with the given id is
     *            selected.
     */
    public static void registerFilterActivity(int id,
            Class<? extends FilterActivity> filterActivityClass) {
        Log.d(TAG, "registerFilterActivity()");
        Log.d(TAG, "id=" + id + ", filterActivityClass=" + filterActivityClass);

        filterActivities.put(id, filterActivityClass);
    }

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState
     *            Ignored
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.filter_options);

        String format = this.getString(R.string.bbct_title);
        String filterOptionsTitle = this
                .getString(R.string.filter_options_title);
        String title = String.format(format, filterOptionsTitle);
        this.setTitle(title);

        RadioGroup filterByRadioGroup = (RadioGroup) this
                .findViewById(R.id.filter_options_radio_group);
        filterByRadioGroup
                .setOnCheckedChangeListener(this.onRadioButtonSelected);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Resources res = this.getResources();
        if (requestCode == res.getInteger(R.integer.year_filter_request)
                || requestCode == res
                        .getInteger(R.integer.number_filter_request)
                || requestCode == res
                        .getInteger(R.integer.year_and_number_filter_request)
                || requestCode == res
                        .getInteger(R.integer.player_name_filter_request)
                || requestCode == res.getInteger(R.integer.team_filter_request)) {
            if (resultCode == RESULT_OK) {
                this.setResult(RESULT_OK, data);
                this.finish();
            }
        } else {
            Log.e(TAG, "onActivityResult(): Invalid Activity request: "
                    + requestCode);
            // TODO: Throw an exception?
        }
    }

    private final OnCheckedChangeListener onRadioButtonSelected = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            // we do not want this call when selection on radio button gets
            // cleared
            if (checkedId == -1) {
                return;
            }

            Log.d(TAG, "Radio button selected.");
            Log.d(TAG, "radioButtonId=" + checkedId);

            Resources res = FilterOptions.this.getResources();
            if (checkedId == R.id.year_filter_radio_button) {
                Log.d(TAG, "Year radio button selected.");
                FilterOptions.this.startActivityForResult(new Intent(
                        FilterOptions.this, YearFilter.class), res
                        .getInteger(R.integer.year_filter_request));
            } else if (checkedId == R.id.number_filter_radio_button) {
                FilterOptions.this.startActivityForResult(new Intent(
                        FilterOptions.this, NumberFilter.class), res
                        .getInteger(R.integer.number_filter_request));
            } else if (checkedId == R.id.year_and_number_filter_radio_button) {
                FilterOptions.this.startActivityForResult(new Intent(
                        FilterOptions.this, YearAndNumberFilter.class), res
                        .getInteger(R.integer.year_and_number_filter_request));
            } else if (checkedId == R.id.player_name_filter_radio_button) {
                FilterOptions.this.startActivityForResult(new Intent(
                        FilterOptions.this, PlayerNameFilter.class), res
                        .getInteger(R.integer.player_name_filter_request));
            } else if (checkedId == R.id.team_filter_radio_button) {
                FilterOptions.this.startActivityForResult(new Intent(
                        FilterOptions.this, TeamFilter.class), res
                        .getInteger(R.integer.team_filter_request));
            } else {
                Log.e(TAG, "Invalid radio button ID.");
                // TODO: Throw an exception?
            }

            // clear radio button in case user cancels filter activity
            group.clearCheck();
        }
    };
    private static Map<Integer, Class<? extends FilterActivity>> filterActivities = new HashMap<Integer, Class<? extends FilterActivity>>();
    private static final String TAG = FilterOptions.class.getName();
}
