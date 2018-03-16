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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;

import bbct.android.common.R;

public class FilterCards extends Fragment {

    private static final String FILTERED_LIST = "Filtered List";
    private static final String INPUT_EXTRA = "input";

    public static final String BRAND_EXTRA = "brand";
    public static final String YEAR_EXTRA = "year";
    public static final String NUMBER_EXTRA = "number";
    public static final String PLAYER_NAME_EXTRA = "playerName";
    public static final String TEAM_EXTRA = "team";

    private static final int[] CHECKBOXES = { R.id.brand_check,
            R.id.year_check, R.id.number_check, R.id.player_name_check,
            R.id.team_check };

    private static final int[] TEXT_FIELDS = { R.id.brand_input,
            R.id.year_input, R.id.number_input, R.id.player_name_input,
            R.id.team_input };

    private static final String[] EXTRAS = { BRAND_EXTRA, YEAR_EXTRA, NUMBER_EXTRA,
            PLAYER_NAME_EXTRA, TEAM_EXTRA };

    /**
     * Finds the corresponding {@link EditText} element given a {@link CheckBox}
     * that was clicked upon.
     */
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
    private final ArrayList<Integer> enabledFields = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_cards, container, false);

        // set title
        String format = this.getString(R.string.bbct_title);
        String filterCardsTitle = this.getString(R.string.filter_cards_title);
        String title = String.format(format, filterCardsTitle);
        this.getActivity().setTitle(title);

        for (int id : CHECKBOXES) {
            View checkBox = view.findViewById(id);
            checkBox.setOnClickListener(this.onCheckBoxClick);
        }

        // restore input fields state
        if (savedInstanceState != null) {
            ArrayList<Integer> enabledFields = savedInstanceState
                    .getIntegerArrayList(INPUT_EXTRA);
            if (enabledFields != null) {
                for (int i : enabledFields) {
                    CheckBox cb = (CheckBox) view.findViewById(CHECKBOXES[i]);
                    cb.setChecked(true);
                    EditText et = (EditText) view.findViewById(TEXT_FIELDS[i]);
                    et.setEnabled(true);
                }
            }
        }

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        enabledFields.clear();
        for (int i = 0; i < TEXT_FIELDS.length; i++) {
            EditText et = (EditText) this.getActivity().findViewById(TEXT_FIELDS[i]);
            if (et.isEnabled()) {
                enabledFields.add(i);
            }
        }
    }

    /**
     * Save the state of all {@link EditText} elements.
     */
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

    /**
     * Toggles the state of {@link EditText}.
     *
     * @param et
     *            - the {@link EditText} to toggle
     */
    private void toggleTextField(EditText et) {
        if (et.isEnabled()) {
            et.setEnabled(false);
        } else {
            et.setEnabled(true);
            et.requestFocus();
        }
    }

    /**
     * Counts the number of {@link CheckBox} elements that are checked.
     *
     * @return the number of checked elements
     */
    private int numberChecked() {
        int count = 0;
        for (int id : CHECKBOXES) {
            CheckBox cb = (CheckBox) this.getActivity().findViewById(id);
            if (cb != null && cb.isChecked()) {
                count++;
            }
        }

        return count;
    }

    /**
     * Sets the combination of filter parameters as a result of
     * {@link FilterCards} activity and exits.
     */
    private void onConfirm() {
        Bundle filterArgs = new Bundle();
        for (int i = 0; i < TEXT_FIELDS.length; i++) {
            EditText input = (EditText) this.getActivity().findViewById(TEXT_FIELDS[i]);
            if (input.isEnabled() && input.getText().toString().length() > 0) {
                String key = EXTRAS[i];
                filterArgs.putString(key, input.getText().toString());
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
