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
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import bbct.common.data.BaseballCard;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardDetails extends Activity {

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

        this.brandText = (EditText) this.findViewById(R.id.details_brand_text);
        this.yearText = (EditText) this.findViewById(R.id.details_year_text);
        this.numberText = (EditText) this.findViewById(R.id.details_number_text);
        this.valueText = (EditText) this.findViewById(R.id.details_value_text);
        this.countText = (EditText) this.findViewById(R.id.details_count_text);
        this.playerNameText = (EditText) this.findViewById(R.id.details_player_name_text);

        this.playerPositionSpinner = (Spinner) this.findViewById(R.id.details_player_position_text);
        ArrayAdapter<CharSequence> positionsAdapter = ArrayAdapter.createFromResource(this, R.array.positions, android.R.layout.simple_spinner_item);
        positionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.playerPositionSpinner.setAdapter(positionsAdapter);

        Button saveButton = (Button) this.findViewById(R.id.details_save_button);
        saveButton.setOnClickListener(this.onSave);

        Button doneButton = (Button) this.findViewById(R.id.details_done_button);
        doneButton.setOnClickListener(this.onDone);

        BaseballCard card = (BaseballCard) getIntent().getSerializableExtra(AndroidConstants.BASEBALL_CARD_EXTRA);

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

    public BaseballCard getBaseballCard() {
        // TODO: Add error checking.
        String brand = this.brandText.getText().toString();
        int year = Integer.parseInt(this.yearText.getText().toString());
        int number = Integer.parseInt(this.numberText.getText().toString());
        double value = Double.parseDouble(this.valueText.getText().toString());
        int count = Integer.parseInt(this.countText.getText().toString());
        String playerName = this.playerNameText.getText().toString();
        String playerPosition = (String) this.playerPositionSpinner.getSelectedItem();

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
            
            // TOOD: Catch exceptions and show appropriate error messages.
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
