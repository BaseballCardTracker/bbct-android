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

        // TODO: Use resource formatting
        String title = this.getString(R.string.app_name) + " - " + this.getString(R.string.card_details_title);
        this.setTitle(title);

        this.brandText = (EditText) this.findViewById(R.id.brand_text);
        this.yearText = (EditText) this.findViewById(R.id.year_text);
        this.numberText = (EditText) this.findViewById(R.id.number_text);
        this.valueText = (EditText) this.findViewById(R.id.number_text);
        this.countText = (EditText) this.findViewById(R.id.count_text);
        this.playerNameText = (EditText) this.findViewById(R.id.player_name_text);

        this.playerPositionSpinner = (Spinner) this.findViewById(R.id.player_position_text);
        ArrayAdapter<CharSequence> positionsAdapter = ArrayAdapter.createFromResource(this, R.array.positions, android.R.layout.simple_spinner_item);
        positionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.playerPositionSpinner.setAdapter(positionsAdapter);

        Button saveButton = (Button) this.findViewById(R.id.details_save_button);
        saveButton.setOnClickListener(this.onSave);

        Button doneButton = (Button) this.findViewById(R.id.details_done_button);
        doneButton.setOnClickListener(this.onDone);

        BaseballCard card = (BaseballCard) getIntent().getSerializableExtra(AndroidConstants.BASEBALL_CARD_EXTRA);

        if (card != null) {
            this.brandText.setText(card.getBrand());
            this.yearText.setText(Integer.toString(card.getYear()));
            this.numberText.setText(Integer.toString(card.getNumber()));
            this.valueText.setText(Integer.toString(card.getNumber()));
            this.countText.setText(Integer.toString(card.getNumber()));
            this.playerNameText.setText(card.getPlayerName());

            int selectedPosition = positionsAdapter.getPosition(card.getPlayerPosition());
            this.playerPositionSpinner.setSelection(selectedPosition);
        }
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
    private View.OnClickListener onSave = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BaseballCard card = BaseballCardDetails.this.getBaseballCard();
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
    private static final String TAG = BaseballCardDetails.class.getName();
}
