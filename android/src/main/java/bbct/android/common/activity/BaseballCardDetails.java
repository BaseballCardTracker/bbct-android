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

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Locale;

import bbct.android.common.R;
import bbct.android.common.activity.util.DialogUtil;
import bbct.android.common.provider.BaseballCardContract;
import bbct.android.common.provider.SingleColumnCursorAdapter;
import bbct.data.BaseballCard;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BaseballCardDetails extends Fragment {

    private static final String ID = "id";
    private static final String CARD = "card";
    private static final String TAG = BaseballCardDetails.class.getName();

    @BindView(R.id.autograph)
    CheckBox autographCheckBox = null;
    @BindView(R.id.condition)
    Spinner conditionSpinner = null;
    @BindView(R.id.brand_text)
    AutoCompleteTextView brandText = null;
    @BindView(R.id.year_text)
    EditText yearText = null;
    @BindView(R.id.number_text)
    EditText numberText = null;
    @BindView(R.id.value_text)
    EditText valueText = null;
    @BindView(R.id.count_text)
    EditText countText = null;
    @BindView(R.id.player_name_text)
    AutoCompleteTextView playerNameText = null;
    @BindView(R.id.team_text)
    AutoCompleteTextView teamText = null;
    @BindView(R.id.player_position_text)
    Spinner playerPositionSpinner = null;

    private ArrayAdapter<CharSequence> conditionAdapter;
    private ArrayAdapter<CharSequence> positionsAdapter;
    private Uri uri = null;
    private boolean isUpdating = false;
    private long cardId = -1L;

    public static BaseballCardDetails getInstance(long id, BaseballCard card) {
        Bundle args = new Bundle();
        args.putLong(ID, id);
        args.putSerializable(CARD, card);

        BaseballCardDetails details = new BaseballCardDetails();
        details.setArguments(args);

        return details;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_details, container, false);
        ButterKnife.bind(this, view);

        brandText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.d(TAG, "onKey() in Brand TextView");
                Log.d(TAG, "keyCode = " + keyCode);

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    Log.d(TAG, "focus on Year");
                    yearText.requestFocus();
                    return true;
                }

                return false;
            }
        });

        playerNameText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.d(TAG, "onKey() in Player Name TextView");
                Log.d(TAG, "keyCode = " + keyCode);

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    Log.d(TAG, "focus on Team");
                    teamText.requestFocus();
                    return true;
                }

                return false;
            }
        });

        teamText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.d(TAG, "onKey() in Team TextView");
                Log.d(TAG, "keyCode = " + keyCode);

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    Log.d(TAG, "hide keyboard");
                    InputMethodManager imm = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(teamText.getWindowToken(), 0);
                    playerPositionSpinner.requestFocus();
                    return true;
                }

                return false;
            }
        });

        String cardDetailsTitle = this.getString(R.string.card_details_title);
        String title = this.getString(R.string.bbct_title, cardDetailsTitle);
        this.getActivity().setTitle(title);

        this.conditionAdapter = this.populateSpinnerAdapter(R.array.condition);
        this.conditionSpinner.setAdapter(this.conditionAdapter);

        CursorAdapter brandAdapter = getSingleColumnCursorAdapter(BaseballCardContract.BRAND_COL_NAME);
        this.brandText.setAdapter(brandAdapter);

        CursorAdapter playerNameAdapter = getSingleColumnCursorAdapter(BaseballCardContract.PLAYER_NAME_COL_NAME);
        this.playerNameText.setAdapter(playerNameAdapter);

        CursorAdapter teamAdapter = getSingleColumnCursorAdapter(BaseballCardContract.TEAM_COL_NAME);
        this.teamText.setAdapter(teamAdapter);

        this.positionsAdapter = this.populateSpinnerAdapter(R.array.positions);
        this.playerPositionSpinner.setAdapter(this.positionsAdapter);

        Bundle args = this.getArguments();
        if (args != null) {
            long id = args.getLong(ID);
            BaseballCard card = (BaseballCard) args.getSerializable(CARD);
            this.setCard(id, card);
        }

        this.uri = BaseballCardContract.getUri(this.getActivity().getPackageName());

        return view;
    }

    @NonNull
    private CursorAdapter getSingleColumnCursorAdapter(String colName) {
        return new SingleColumnCursorAdapter(getActivity(), colName);
    }

    private void setCard(long cardId, BaseballCard card) {
        this.isUpdating = true;
        this.cardId = cardId;
        this.autographCheckBox.setChecked(card.isAutographed());

        int selectedCondition = this.conditionAdapter.getPosition(card
                .getCondition());
        this.conditionSpinner.setSelection(selectedCondition);

        this.brandText.setText(card.getBrand());
        this.yearText.setText(String.format(Locale.getDefault(), "%d", card.getYear()));
        this.numberText.setText(String.format(Locale.getDefault(), "%d", card.getNumber()));
        this.valueText.setText(String.format(Locale.getDefault(), "%.2f", card.getValue() / 100.0));
        this.countText.setText(String.format(Locale.getDefault(), "%d", card.getCount()));
        this.playerNameText.setText(card.getPlayerName());
        this.teamText.setText(card.getTeam());

        int selectedPosition = this.positionsAdapter.getPosition(card
                .getPlayerPosition());
        this.playerPositionSpinner.setSelection(selectedPosition);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();

        switch (menuId) {
            case R.id.save_menu:
                this.onSave();
                return true;

            case android.R.id.home:
                this.onHome();
                return true;
        }

        return false;
    }

    private void onHome() {
        Fragment list = BaseballCardList.getInstance(null);
        this.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_holder, list, FragmentTags.CARD_LIST)
                .addToBackStack(FragmentTags.CARD_LIST)
                .commit();
    }

    private ArrayAdapter<CharSequence> populateSpinnerAdapter(int arrayId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this.getActivity(), arrayId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return adapter;
    }

    private BaseballCard getBaseballCard() {
        Log.d(TAG, "getBaseballCard()");

        EditText[] allEditTexts = {this.brandText, this.yearText,
                this.numberText, this.valueText, this.countText,
                this.playerNameText, this.teamText};
        int[] errorIds = {R.string.brand_input_error,
                R.string.year_input_error, R.string.number_input_error,
                R.string.value_input_error, R.string.count_input_error,
                R.string.player_name_input_error, R.string.team_input_error};
        boolean validInput = true;

        boolean autographed = this.autographCheckBox.isChecked();
        String condition = (String) this.conditionSpinner.getSelectedItem();
        String playerPosition = (String) this.playerPositionSpinner
                .getSelectedItem();

        for (int i = allEditTexts.length - 1; i >= 0; --i) {
            String input = allEditTexts[i].getText().toString();
            if (input.equals("")) {
                allEditTexts[i].requestFocus();
                allEditTexts[i].setError(this.getString(errorIds[i]));
                validInput = false;
            }
        }

        if (validInput) {
            String brand = this.brandText.getText().toString();
            String yearStr = this.yearText.getText().toString();
            int year = Integer.parseInt(yearStr);
            String numberStr = this.numberText.getText().toString();
            int number = Integer.parseInt(numberStr);
            String valueStr = this.valueText.getText().toString();
            double value = Double.parseDouble(valueStr);
            String countStr = this.countText.getText().toString();
            int count = Integer.parseInt(countStr);
            String team = this.teamText.getText().toString();
            String playerName = this.playerNameText.getText().toString();
            return new BaseballCard(autographed, condition, brand, year,
                    number, (int) (value * 100), count, playerName, team,
                    playerPosition);
        } else {
            return null;
        }
    }

    private void resetInput() {
        this.autographCheckBox.setChecked(false);
        this.brandText.setText("");
        this.yearText.setText("");
        this.numberText.setText("");
        this.valueText.setText("");
        this.countText.setText("");
        this.playerNameText.setText("");
        this.teamText.setText("");
        this.playerPositionSpinner.setSelection(-1);
    }

    private void onSave() {
        ContentResolver resolver = this.getActivity().getContentResolver();
        BaseballCard newCard = this.getBaseballCard();

        if (newCard != null) {
            if (this.isUpdating) {
                Uri uri = ContentUris.withAppendedId(this.uri, this.cardId);
                resolver.update(uri,
                        BaseballCardContract.getContentValues(newCard), null,
                        null);
            } else {
                try {
                    ContentValues values = BaseballCardContract
                            .getContentValues(newCard);
                    resolver.insert(this.uri, values);

                    this.resetInput();
                    this.brandText.requestFocus();
                    Toast.makeText(this.getActivity(), R.string.card_added_message,
                            Toast.LENGTH_LONG).show();
                } catch (SQLException e) {
                    // Is duplicate card the only reason this exception
                    // will be thrown?
                    DialogUtil.showErrorDialog(this.getActivity(),
                            R.string.duplicate_card_title,
                            R.string.duplicate_card_error);
                }
            }
        }
    }

}
