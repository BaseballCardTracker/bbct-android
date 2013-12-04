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
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import bbct.android.common.R;
import bbct.android.common.activity.util.DialogUtil;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.provider.BaseballCardSQLHelper;
import bbct.android.common.provider.SQLHelperFactory;
import bbct.android.common.provider.SingleColumnCursorAdapter;
import bbct.android.common.exception.SQLHelperCreationException;
import bbct.android.common.provider.BaseballCardContract;

/**
 * Allows user to add a new card or view and edit details of an existing card.
 */
public class BaseballCardDetails extends Activity {

    private static final String DETAILS_AUTHORITY = "bbct.android.details";
    private static final String TABLE_NAME = BaseballCardContract.TABLE_NAME;
    /**
     * URI for viewing card details.
     */
    public static final Uri DETAILS_URI = new Uri.Builder().scheme("content").authority(DETAILS_AUTHORITY).path(TABLE_NAME).build();

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

        this.brandText = (AutoCompleteTextView) this.findViewById(R.id.brand_text);
        CursorAdapter brandAdapter = new SingleColumnCursorAdapter(this, BaseballCardContract.BRAND_COL_NAME);
        this.brandText.setAdapter(brandAdapter);

        this.yearText = (EditText) this.findViewById(R.id.year_text);
        this.numberText = (EditText) this.findViewById(R.id.number_text);
        this.valueText = (EditText) this.findViewById(R.id.value_text);
        this.countText = (EditText) this.findViewById(R.id.count_text);

        this.playerNameText = (AutoCompleteTextView) this.findViewById(R.id.player_name_text);
        CursorAdapter playerNameAdapter = new SingleColumnCursorAdapter(this, BaseballCardContract.PLAYER_NAME_COL_NAME);
        this.playerNameText.setAdapter(playerNameAdapter);

        this.teamText = (AutoCompleteTextView) this.findViewById(R.id.team_text);
        CursorAdapter teamAdapter = new SingleColumnCursorAdapter(this, BaseballCardContract.TEAM_COL_NAME);
        this.teamText.setAdapter(teamAdapter);

        this.playerPositionSpinner = (Spinner) this.findViewById(R.id.player_position_text);
        ArrayAdapter<CharSequence> positionsAdapter = ArrayAdapter.createFromResource(this, R.array.positions, android.R.layout.simple_spinner_item);
        positionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.playerPositionSpinner.setAdapter(positionsAdapter);

        Button saveButton = (Button) this.findViewById(R.id.save_button);
        saveButton.setOnClickListener(this.onSave);

        Button doneButton = (Button) this.findViewById(R.id.done_button);
        doneButton.setOnClickListener(this.onDone);

        this.oldCard = (BaseballCard) this.getIntent().getSerializableExtra(this.getString(R.string.baseball_card_extra));

        if (this.oldCard != null) {
            this.isUpdating = true;
            this.brandText.setText(this.oldCard.getBrand());
            this.yearText.setText(Integer.toString(this.oldCard.getYear()));
            this.numberText.setText(Integer.toString(this.oldCard.getNumber()));
            this.valueText.setText(Double.toString(this.oldCard.getValue() / 100.0));
            this.countText.setText(Integer.toString(this.oldCard.getCount()));
            this.playerNameText.setText(this.oldCard.getPlayerName());
            this.teamText.setText(this.oldCard.getTeam());

            int selectedPosition = positionsAdapter.getPosition(this.oldCard.getPlayerPosition());
            this.playerPositionSpinner.setSelection(selectedPosition);
        }
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

        String playerPosition = (String) this.playerPositionSpinner.getSelectedItem();

        for (int i = allEditTexts.length - 1; i >= 0; --i) {
            Log.d(TAG, "i=" + i);

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
            return new BaseballCard(brand, year, number, (int) (value * 100), count, playerName, team, playerPosition);
        } else {
            return null;
        }
    }

    /**
     *
     * Called when a key was released and not handled by any of the views inside of the activity.
     * @param keyCode - The value in event.getKeyCode().
     * @param event   - Description of the key event.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        //If the key entered is 'Enter'('next' or 'done'), then move the focus
        // to the next view.
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            if (brandText.hasFocus()) {
                yearText.requestFocus();
                return true;
            } else if (playerNameText.hasFocus()) {
                teamText.requestFocus();
                return true;
            } else if (teamText.hasFocus()) {
                playerPositionSpinner.requestFocus();
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
    private View.OnClickListener onSave = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BaseballCardSQLHelper sqlHelper = null;
            try {
                BaseballCard newCard = BaseballCardDetails.this.getBaseballCard();
                sqlHelper = SQLHelperFactory.getSQLHelper(view.getContext());

                if (newCard != null) {
                    if (BaseballCardDetails.this.isUpdating) {
                        sqlHelper.updateBaseballCard(BaseballCardDetails.this.oldCard, newCard);
                        BaseballCardDetails.this.finish();
                    } else {
                        long result = sqlHelper.insertBaseballCard(newCard);

                        if (result == -1) {
                            DialogUtil.showErrorDialog(BaseballCardDetails.this, R.string.duplicate_card_title, R.string.duplicate_card_error);
                        } else {
                            BaseballCardDetails.this.resetInput();
                            BaseballCardDetails.this.brandText.requestFocus();
                            Toast.makeText(view.getContext(), R.string.card_added_message, Toast.LENGTH_LONG).show();
                        }
                    }
                    // TODO: Catch SQL exceptions and show appropriate error messages.
                }
            } catch (SQLHelperCreationException ex) {
                // TODO Show a dialog and exit app
                Toast.makeText(view.getContext(), R.string.database_error, Toast.LENGTH_LONG).show();
                Log.e(TAG, ex.getMessage(), ex);
            } finally {
                if (sqlHelper != null) {
                    sqlHelper.close();
                }
            }
        }
    };
    private View.OnClickListener onDone = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BaseballCardDetails.this.finish();
        }
    };
    private BaseballCard oldCard = null;
    private AutoCompleteTextView brandText = null;
    private EditText yearText = null;
    private EditText numberText = null;
    private EditText valueText = null;
    private EditText countText = null;
    private AutoCompleteTextView playerNameText = null;
    private AutoCompleteTextView teamText = null;
    private Spinner playerPositionSpinner = null;
    private boolean isUpdating = false;
    private static final String TAG = BaseballCardDetails.class.getName();
}
