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
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
        @SuppressWarnings("unchecked")
        ArrayAdapter<CharSequence> conditionAdapter = (ArrayAdapter<CharSequence>) this.conditionSpinner
                .getAdapter();

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

        BaseballCard oldCard = (BaseballCard) this.getIntent().getSerializableExtra(this.getString(R.string.baseball_card_extra));

        if (oldCard != null) {
            this.isUpdating = true;
            this.cardId = this.getIntent().getLongExtra(
                    this.getString(R.string.card_id_extra), -1L);
            this.autographCheckBox.setChecked(oldCard.isAutographed());

            int selectedCondition = conditionAdapter.getPosition(oldCard
                    .getCondition());
            this.conditionSpinner.setSelection(selectedCondition);

            this.brandText.setText(oldCard.getBrand());
            this.yearText.setText(Integer.toString(oldCard.getYear()));
            this.numberText.setText(Integer.toString(oldCard.getNumber()));
            this.valueText
                    .setText(Double.toString(oldCard.getValue() / 100.0));
            this.countText.setText(Integer.toString(oldCard.getCount()));
            this.playerNameText.setText(oldCard.getPlayerName());
            this.teamText.setText(oldCard.getTeam());

            int selectedPosition = positionsAdapter.getPosition(oldCard
                    .getPlayerPosition());
            this.playerPositionSpinner.setSelection(selectedPosition);
        }

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        this.uri = BaseballCardContract.getUri(this.getPackageName());
        Log.d(TAG, "package name=" + this.getPackageName());
        Log.d(TAG, "uri=" + this.uri);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.save, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();

        if (menuId == R.id.save_menu) {
            this.onSave();
            return true;
        }

        return false;
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
        ContentResolver resolver = this.getContentResolver();
        BaseballCard newCard = this.getBaseballCard();

        if (newCard != null) {
            if (this.isUpdating) {
                Uri uri = ContentUris.withAppendedId(this.uri, this.cardId);
                resolver.update(uri,
                        BaseballCardContract.getContentValues(newCard), null,
                        null);
                this.finish();
            } else {
                try {
                    ContentValues values = BaseballCardContract
                            .getContentValues(newCard);
                    resolver.insert(this.uri, values);

                    this.resetInput();
                    this.brandText.requestFocus();
                    Toast.makeText(this, R.string.card_added_message,
                            Toast.LENGTH_LONG).show();
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
