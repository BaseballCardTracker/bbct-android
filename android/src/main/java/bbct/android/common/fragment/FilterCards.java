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
package bbct.android.common.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import bbct.android.common.R;
import butterknife.BindView;
import butterknife.ButterKnife;

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

    @BindView(R.id.confirm_button)
    FloatingActionButton confirmButton;

    private View.OnClickListener onCheckBoxClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText input = null;

            Activity activity = Objects.requireNonNull(getActivity());
            for (int i = 0; i < CHECKBOXES.length; i++) {
                if (v.getId() == CHECKBOXES[i]) {
                    input = activity.findViewById(TEXT_FIELDS[i]);
                }
            }

            if (input != null) {
                toggleTextField(input);
            }
            confirmButton.setEnabled(numberChecked() > 0);
        }
    };
    private final ArrayList<Integer> enabledFields = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_cards, container, false);
        ButterKnife.bind(this, view);

        confirmButton.setEnabled(false);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfirm();
            }
        });

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
                    CheckBox cb = view.findViewById(CHECKBOXES[i]);
                    cb.setChecked(true);
                    EditText et = view.findViewById(TEXT_FIELDS[i]);
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
        Activity activity = Objects.requireNonNull(getActivity());
        for (int i = 0; i < TEXT_FIELDS.length; i++) {
            EditText et = activity.findViewById(TEXT_FIELDS[i]);
            if (et.isEnabled()) {
                enabledFields.add(i);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putIntegerArrayList(INPUT_EXTRA, enabledFields);
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
        Activity activity = Objects.requireNonNull(getActivity());
        for (int id : CHECKBOXES) {
            CheckBox cb = activity.findViewById(id);
            if (cb != null && cb.isChecked()) {
                count++;
            }
        }

        return count;
    }

    private void onConfirm() {
        Bundle filterArgs = new Bundle();
        Activity activity = Objects.requireNonNull(getActivity());
        for (int i = 0; i < TEXT_FIELDS.length; i++) {
            EditText input = activity.findViewById(TEXT_FIELDS[i]);
            if (input.isEnabled() && input.getText().toString().length() > 0) {
                String key = EXTRAS[i];
                filterArgs.putString(key, input.getText().toString());
            }
        }

        NavDirections action = FilterCardsDirections.actionList(filterArgs);
        NavHostFragment.findNavController(this).navigate(action);
    }
}
