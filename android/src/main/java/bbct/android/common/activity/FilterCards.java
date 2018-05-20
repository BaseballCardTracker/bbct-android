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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import bbct.android.common.R;
import bbct.android.common.view.FilterOptionView;
import butterknife.BindViews;
import butterknife.ButterKnife;

import bbct.android.common.R;

public class FilterCards extends Fragment {
    private static final String TAG = FilterCards.class.getName();
    private static final String FILTERED_LIST = "Filtered List";
    private static final String INPUT_EXTRA = "input";

    public static final String BRAND_EXTRA = "brand";
    public static final String YEAR_EXTRA = "year";
    public static final String NUMBER_EXTRA = "number";
    public static final String PLAYER_NAME_EXTRA = "playerName";
    public static final String TEAM_EXTRA = "team";

    private static final String[] EXTRAS = {BRAND_EXTRA, YEAR_EXTRA, NUMBER_EXTRA,
            PLAYER_NAME_EXTRA, TEAM_EXTRA};

    @BindViews({R.id.brand, R.id.year, R.id.number, R.id.player_name, R.id.team})
    List<FilterOptionView> filterOptions;

    private ArrayList<Integer> enabledFields = new ArrayList<>();

    private View.OnClickListener onCheckBoxClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText input = null;

            for (int i = 0; i < CHECKBOXES.length; i++) {
                if (v.getId() == CHECKBOXES[i]) {
                    input = (EditText) FilterCards.this.getActivity().findViewById(TEXT_FIELDS[i]);
                }
            }

            FilterCards.this.toggleTextField(input);
            FilterCards.this.getActivity().supportInvalidateOptionsMenu();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_cards, container, false);
        ButterKnife.bind(this, view);

        // set title
        String format = this.getString(R.string.bbct_title);
        String filterCardsTitle = this.getString(R.string.filter_cards_title);
        String title = String.format(format, filterCardsTitle);
        this.getActivity().setTitle(title);

        // restore input fields state
        if (savedInstanceState != null) {
            ArrayList<Integer> enabledFields = savedInstanceState
                    .getIntegerArrayList(INPUT_EXTRA);

            Log.d(TAG, "enabledField=" + enabledFields);

            if (enabledFields != null) {
                for (int i : enabledFields) {
                    filterOptions.get(i).setChecked(true);
                }
            }
        }

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        enabledFields.clear();
        for (int i = 0; i < filterOptions.size(); i++) {
            if (filterOptions.get(i).isChecked()) {
                enabledFields.add(i);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putIntegerArrayList(INPUT_EXTRA, enabledFields);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem confirm = menu.findItem(R.id.save_menu);

        if (this.numberChecked() > 0) {
            confirm.setVisible(true);
            confirm.setEnabled(true);
        } else {
            confirm.setVisible(false);
            confirm.setEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();

        if (menuId == R.id.save_menu) {
            this.onConfirm();
            return true;
        }

        return false;
    }

    private void toggleTextField(EditText et) {
        if (et.isEnabled()) {
            et.setEnabled(false);
        } else {
            et.setEnabled(true);
            et.requestFocus();
        }
    }

    private int numberChecked() {
        int count = 0;
        for (FilterOptionView filterOption : filterOptions) {
            if (filterOption.isChecked()) {
                count++;
            }
        }

        return count;
    }

    private void onConfirm() {
        Bundle filterArgs = new Bundle();
        for (int i = 0; i < filterOptions.size(); i++) {
            FilterOptionView view = filterOptions.get(i);
            String filterOption = view.getText().toString();
            if (view.isEnabled() && filterOption.length() > 0) {
                filterArgs.putString(EXTRAS[i], filterOption);
            }
        }

        BaseballCardList cardList = BaseballCardList.getInstance(filterArgs);
        this.getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_holder, cardList)
                .addToBackStack(FILTERED_LIST)
                .commit();
    }
}
