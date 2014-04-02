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
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import bbct.android.common.R;
import bbct.android.common.activity.util.DialogUtil;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.provider.BaseballCardContract;
import bbct.android.common.provider.SingleColumnCursorAdapter;

/**
 * Allows user to add a new card or view and edit details of an existing card.
 */
public class BaseballCardDetails extends ActionBarActivity {

    private static final String DETAILS_AUTHORITY = "bbct.android.details";
    private static final String TABLE_NAME = BaseballCardContract.TABLE_NAME;
    /**
     * URI for viewing card details.
     */
    public static final Uri DETAILS_URI = new Uri.Builder().scheme("content")
            .authority(DETAILS_AUTHORITY).path(TABLE_NAME).build();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.card_details);

        String cardDetailsTitle = this.getString(R.string.card_details_title);
        String title = this.getString(R.string.bbct_title, cardDetailsTitle);
        this.setTitle(title);

        this.autographCheckBox = (CheckBox) this.findViewById(R.id.autograph);

        this.conditionSpinner = this.populateSpinner(R.id.condition,
                R.array.condition);

        this.brandText = (AutoCompleteTextView) this
                .findViewById(R.id.brand_text);
        CursorAdapter brandAdapter = new SingleColumnCursorAdapter(this,
                BaseballCardContract.BRAND_COL_NAME);
        this.brandText.setAdapter(brandAdapter);

        this.yearText = (EditText) this.findViewById(R.id.year_text);
        this.numberText = (EditText) this.findViewById(R.id.number_text);
        this.valueText = (EditText) this.findViewById(R.id.value_text);
        this.countText = (EditText) this.findViewById(R.id.count_text);

        this.playerNameText = (AutoCompleteTextView) this
                .findViewById(R.id.player_name_text);
        CursorAdapter playerNameAdapter = new SingleColumnCursorAdapter(this,
                BaseballCardContract.PLAYER_NAME_COL_NAME);
        this.playerNameText.setAdapter(playerNameAdapter);

        this.teamText = (AutoCompleteTextView) this
                .findViewById(R.id.team_text);
        CursorAdapter teamAdapter = new SingleColumnCursorAdapter(this,
                BaseballCardContract.TEAM_COL_NAME);
        this.teamText.setAdapter(teamAdapter);

        this.playerPositionSpinner = this.populateSpinner(
                R.id.player_position_text, R.array.positions);
        @SuppressWarnings("unchecked")
        ArrayAdapter<CharSequence> positionsAdapter = (ArrayAdapter<CharSequence>) this.playerPositionSpinner
                .getAdapter();

        Button saveButton = (Button) this.findViewById(R.id.save_button);
        saveButton.setOnClickListener(this.onSave);

        Button cancelButton = (Button) this.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this.onCancel);

        this.oldCard = (BaseballCard) this.getIntent().getSerializableExtra(
                this.getString(R.string.baseball_card_extra));

        if (this.oldCard != null) {
            this.isUpdating = true;
            this.cardId = this.getIntent().getLongExtra(
                    this.getString(R.string.card_id_extra), -1L);
            this.brandText.setText(this.oldCard.getBrand());
            this.yearText.setText(Integer.toString(this.oldCard.getYear()));
            this.numberText.setText(Integer.toString(this.oldCard.getNumber()));
            this.valueText
                    .setText(Double.toString(this.oldCard.getValue() / 100.0));
            this.countText.setText(Integer.toString(this.oldCard.getCount()));
            this.playerNameText.setText(this.oldCard.getPlayerName());
            this.teamText.setText(this.oldCard.getTeam());

            int selectedPosition = positionsAdapter.getPosition(this.oldCard
                    .getPlayerPosition());
            this.playerPositionSpinner.setSelection(selectedPosition);
        }

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        this.uri = BaseballCardContract.getUri(this.getPackageName());
        Log.d(TAG, "package name=" + this.getPackageName());
        Log.d(TAG, "uri=" + this.uri);
    }

    private Spinner populateSpinner(int spinnerId, int araryId) {
        Spinner spinner = (Spinner) this.findViewById(spinnerId);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, araryId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        return spinner;
    }

    private BaseballCard getBaseballCard() {
        Log.d(TAG, "getBaseballCard()");

        EditText[] allEditTexts = { this.brandText, this.yearText,
                this.numberText, this.valueText, this.countText,
                this.playerNameText, this.teamText };
        int[] errorIds = { R.string.brand_input_error,
                R.string.year_input_error, R.string.number_input_error,
                R.string.value_input_error, R.string.count_input_error,
                R.string.player_name_input_error, R.string.team_input_error };
        boolean validInput = true;

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
            return new BaseballCard(brand, year, number, (int) (value * 100),
                    count, playerName, team, playerPosition);
        } else {
            return null;
        }
    }

    /**
     *
     * Called when a key was released and not handled by any of the views inside
     * of the activity.
     *
     * @param keyCode
     *            The value in event.getKeyCode().
     * @param event
     *            Description of the key event.
     * @return {@code true} if the event was handled, {@code false} otherwise.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // If the key entered is 'Enter'('next' or 'done'), then
        // 1) move the focus to the next view if the current focus is in brand
        // or player name view and
        // 2) hide the keypad if the current focus is in team view.
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            if (this.brandText.hasFocus()) {
                this.yearText.requestFocus();
                return true;
            } else if (this.playerNameText.hasFocus()) {
                this.teamText.requestFocus();
                return true;
            } else if (this.teamText.hasFocus()) {
                // hide the soft keypad
                InputMethodManager imm = (InputMethodManager) this
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(this.teamText.getWindowToken(), 0);
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    private void resetInput() {
        this.brandText.setText("");
        this.yearText.setText("");
        this.numberText.setText("");
        this.valueText.setText("");
        this.countText.setText("");
        this.playerNameText.setText("");
        this.teamText.setText("");
        this.playerPositionSpinner.setSelection(-1);
    }

    private final View.OnClickListener onSave = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ContentResolver resolver = BaseballCardDetails.this
                    .getContentResolver();
            BaseballCard newCard = BaseballCardDetails.this.getBaseballCard();

            if (newCard != null) {
                if (BaseballCardDetails.this.isUpdating) {
                    Uri uri = ContentUris.withAppendedId(
                            BaseballCardDetails.this.uri,
                            BaseballCardDetails.this.cardId);
                    resolver.update(uri,
                            BaseballCardContract.getContentValues(newCard),
                            null, null);
                    BaseballCardDetails.this.finish();
                } else {
                    try {
                        ContentValues values = BaseballCardContract
                                .getContentValues(newCard);
                        resolver.insert(BaseballCardDetails.this.uri, values);

                        BaseballCardDetails.this.resetInput();
                        BaseballCardDetails.this.brandText.requestFocus();
                        Toast.makeText(view.getContext(),
                                R.string.card_added_message, Toast.LENGTH_LONG)
                                .show();
                    } catch (SQLException e) {
                        // Is duplicate card the only reason this exception
                        // will be thrown?
                        DialogUtil.showErrorDialog(BaseballCardDetails.this,
                                R.string.duplicate_card_title,
                                R.string.duplicate_card_error);
                    }
                }
            }
        }
    };

    private final View.OnClickListener onCancel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BaseballCardDetails.this.finish();
        }
    };

    private BaseballCard oldCard = null;
    private CheckBox autographCheckBox = null;
    private Spinner conditionSpinner = null;
    private AutoCompleteTextView brandText = null;
    private EditText yearText = null;
    private EditText numberText = null;
    private EditText valueText = null;
    private EditText countText = null;
    private AutoCompleteTextView playerNameText = null;
    private AutoCompleteTextView teamText = null;
    private Spinner playerPositionSpinner = null;
    private Uri uri = null;
    private boolean isUpdating = false;
    private long cardId = -1L;
    private static final String TAG = BaseballCardDetails.class.getName();

}
