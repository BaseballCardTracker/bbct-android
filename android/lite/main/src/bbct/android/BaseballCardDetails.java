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
package bbct.android;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import bbct.common.data.BaseballCard;
import bbct.common.exceptions.InputException;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardDetails extends Activity {

    private static final String DETAILS_AUTHORITY = "bbct.android";
    
    private static final String TABLE_NAME = BaseballCardContract.TABLE_NAME;
    
    public static final Uri DETAILS_URI = new Uri.Builder().scheme("content").authority(DETAILS_AUTHORITY).path(TABLE_NAME).build();
    
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.card_details);

        String format = this.getString(R.string.bbct_title);
        String cardDetailsTitle = this.getString(R.string.card_details_title);
        String title = String.format(format, cardDetailsTitle);
        this.setTitle(title);

        this.brandText = (EditText) this.findViewById(R.id.brand_text);
        this.yearText = (EditText) this.findViewById(R.id.year_text);
        this.numberText = (EditText) this.findViewById(R.id.number_text);
        this.valueText = (EditText) this.findViewById(R.id.value_text);
        this.countText = (EditText) this.findViewById(R.id.count_text);
        this.playerNameText = (EditText) this.findViewById(R.id.player_name_text);

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

            this.brandText.setEnabled(false);
            this.yearText.setEnabled(false);
            this.numberText.setEnabled(false);
            this.playerNameText.setEnabled(false);
            this.playerPositionSpinner.setEnabled(false);
        }

        this.sqlHelper = new BaseballCardSQLHelper(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.sqlHelper.close();
    }

    private BaseballCard getBaseballCard() throws InputException {
        String brand = this.brandText.getText().toString();
        if (brand.equals("")) {
            this.brandText.requestFocus();
            throw new InputException(this.getString(R.string.brand_input_error));
        }

        String yearStr = this.yearText.getText().toString();
        if (yearStr.equals("")) {
            this.yearText.requestFocus();
            throw new InputException(this.getString(R.string.year_input_error));
        }
        int year = Integer.parseInt(yearStr);

        String numberStr = this.numberText.getText().toString();
        if (numberStr.equals("")) {
            this.numberText.requestFocus();
            throw new InputException(this.getString(R.string.number_input_error));
        }
        int number = Integer.parseInt(numberStr);

        String valueStr = this.valueText.getText().toString();
        if (valueStr.equals("")) {
            this.valueText.requestFocus();
            throw new InputException(this.getString(R.string.value_input_error));
        }
        double value = Double.parseDouble(valueStr);

        String countStr = this.countText.getText().toString();
        if (countStr.equals("")) {
            this.countText.requestFocus();
            throw new InputException(this.getString(R.string.count_input_error));
        }
        int count = Integer.parseInt(countStr);

        String playerName = this.playerNameText.getText().toString();
        if (playerName.equals("")) {
            this.playerNameText.requestFocus();
            throw new InputException(this.getString(R.string.player_name_input_error));
        }

        String playerPosition = (String) this.playerPositionSpinner.getSelectedItem();
        if (playerPosition.equals("")) {
            this.playerPositionSpinner.requestFocus();
            throw new InputException(this.getString(R.string.player_position_input_error));
        }

        return new BaseballCard(brand, year, number, (int) (value * 100), count, playerName, playerPosition);
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
            try {
                BaseballCard card = BaseballCardDetails.this.getBaseballCard();

                if (BaseballCardDetails.this.isUpdating) {
                    BaseballCardDetails.this.sqlHelper.updateBaseballCard(card);
                    BaseballCardDetails.this.finish();
                } else {
                    BaseballCardDetails.this.sqlHelper.insertBaseballCard(card);
                    BaseballCardDetails.this.resetInput();
                    BaseballCardDetails.this.brandText.requestFocus();
                    Toast.makeText(BaseballCardDetails.this, R.string.card_added_message, Toast.LENGTH_LONG).show();
                }

                // TODO: Catch exceptions and show appropriate error messages.
            } catch (InputException ex) {
                Toast.makeText(BaseballCardDetails.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                Log.i(TAG, ex.getMessage(), ex);
            }
        }
    };
    private View.OnClickListener onDone = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BaseballCardDetails.this.finish();
        }
    };
    private EditText brandText = null;
    private EditText yearText = null;
    private EditText numberText = null;
    private EditText valueText = null;
    private EditText countText = null;
    private EditText playerNameText = null;
    private Spinner playerPositionSpinner = null;
    private BaseballCardSQLHelper sqlHelper = null;
    private boolean isUpdating = false;
    private static final String TAG = BaseballCardDetails.class.getName();
}
