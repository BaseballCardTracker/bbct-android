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
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import bbct.android.common.R;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.database.BaseballCardSQLHelper;
import bbct.android.common.database.SQLHelperFactory;
import bbct.android.common.database.SingleColumnCursorAdapter;
import bbct.android.common.exception.SQLHelperCreationException;
import bbct.android.common.provider.BaseballCardContract;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardDetails extends Activity {

    private static final String DETAILS_AUTHORITY = "bbct.android.details";
    private static final String TABLE_NAME = BaseballCardContract.TABLE_NAME;
    public static final Uri DETAILS_URI = new Uri.Builder().scheme("content").authority(DETAILS_AUTHORITY).path(TABLE_NAME).build();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            this.setContentView(R.layout.card_details);

            String cardDetailsTitle = this.getString(R.string.card_details_title);
            String title = this.getString(R.string.bbct_title, cardDetailsTitle);
            this.setTitle(title);

            this.sqlHelper = SQLHelperFactory.getSQLHelper(this);
            Cursor cursor = this.sqlHelper.getCursor();
            this.startManagingCursor(cursor);

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

            this.playerPositionSpinner = (Spinner) this.findViewById(R.id.player_position_text);
            ArrayAdapter<CharSequence> positionsAdapter = ArrayAdapter.createFromResource(this, R.array.positions, android.R.layout.simple_spinner_item);
            positionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.playerPositionSpinner.setAdapter(positionsAdapter);

            Button saveButton = (Button) this.findViewById(R.id.save_button);
            saveButton.setOnClickListener(this.onSave);

            Button doneButton = (Button) this.findViewById(R.id.done_button);
            doneButton.setOnClickListener(this.onDone);

            BaseballCard card = (BaseballCard) this.getIntent().getSerializableExtra(this.getString(R.string.baseball_card_extra));

            if (card != null) {
                this.isUpdating = true;
                this.brandText.setText(card.getBrand());
                this.yearText.setText(Integer.toString(card.getYear()));
                this.numberText.setText(Integer.toString(card.getNumber()));
                this.valueText.setText(Double.toString(card.getValue() / 100.0));
                this.countText.setText(Integer.toString(card.getCount()));
                this.playerNameText.setText(card.getPlayerName());

                int selectedPosition = positionsAdapter.getPosition(card.getPlayerPosition());
                this.playerPositionSpinner.setSelection(selectedPosition);
            }
        } catch (SQLHelperCreationException ex) {
            // TODO Show a dialog and exit app
            Toast.makeText(this, R.string.database_error, Toast.LENGTH_LONG).show();
            Log.e(TAG, ex.getMessage(), ex);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.sqlHelper.close();
    }

    private BaseballCard getBaseballCard() {
        Log.d(TAG, "getBaseballCard()");

        EditText[] allEditTexts = {this.brandText, this.yearText, this.numberText,
            this.countText, this.valueText, this.playerNameText};
        int[] errorIds = {R.string.brand_input_error, R.string.year_input_error, R.string.number_input_error,
            R.string.count_input_error, R.string.value_input_error, R.string.player_name_input_error};
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
            String playerName = this.playerNameText.getText().toString();
            return new BaseballCard(brand, year, number, (int) (value * 100), count, playerName, playerPosition);
        } else {
            return null;
        }
    }

    private void resetInput() {
        this.brandText.setText("");
        this.yearText.setText("");
        this.numberText.setText("");
        this.valueText.setText("");
        this.countText.setText("");
        this.playerNameText.setText("");
        this.playerPositionSpinner.setSelection(-1);
    }
    private View.OnClickListener onSave = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BaseballCard card = BaseballCardDetails.this.getBaseballCard();

            if (card != null) {
                if (BaseballCardDetails.this.isUpdating) {
                    BaseballCardDetails.this.sqlHelper.updateBaseballCard(card);
                    BaseballCardDetails.this.finish();
                } else {
                    BaseballCardDetails.this.sqlHelper.insertBaseballCard(card);
                    BaseballCardDetails.this.resetInput();
                    BaseballCardDetails.this.brandText.requestFocus();
                    Toast.makeText(BaseballCardDetails.this, R.string.card_added_message, Toast.LENGTH_LONG).show();
                }
                // TODO: Catch SQL exceptions and show appropriate error messages.
            }
        }
    };
    private View.OnClickListener onDone = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BaseballCardDetails.this.finish();
        }
    };
    private AutoCompleteTextView brandText = null;
    private EditText yearText = null;
    private EditText numberText = null;
    private EditText valueText = null;
    private EditText countText = null;
    private AutoCompleteTextView playerNameText = null;
    private Spinner playerPositionSpinner = null;
    private BaseballCardSQLHelper sqlHelper = null;
    private boolean isUpdating = false;
    private static final String TAG = BaseballCardDetails.class.getName();
}
