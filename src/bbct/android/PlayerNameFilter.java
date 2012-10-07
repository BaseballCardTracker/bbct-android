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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import bbct.common.exceptions.InputException;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class PlayerNameFilter extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.player_name_filter);

        String format = this.getString(R.string.bbct_title);
        String playerNameFilterTitle = this.getString(R.string.player_name_filter_title);
        String title = String.format(format, playerNameFilterTitle);
        this.setTitle(title);

        this.playerNameText = (EditText) this.findViewById(R.id.player_name_filter_player_name_text);

        Button okButton = (Button) this.findViewById(R.id.player_name_filter_ok_button);
        okButton.setOnClickListener(this.onOk);

        Button cancelButton = (Button) this.findViewById(R.id.player_name_filter_cancel_button);
        cancelButton.setOnClickListener(this.onCancel);
    }
    private View.OnClickListener onOk = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String playerName = PlayerNameFilter.this.playerNameText.getText().toString();
            if (playerName.equals("")) {
                PlayerNameFilter.this.playerNameText.requestFocus();
                Toast.makeText(PlayerNameFilter.this, R.string.player_name_input_error, Toast.LENGTH_LONG).show();
            } else {
                Intent data = new Intent();
                data.putExtra(AndroidConstants.FILTER_REQUEST_EXTRA, AndroidConstants.PLAYER_NAME_FILTER_REQUEST);
                data.putExtra(AndroidConstants.PLAYER_NAME_EXTRA, playerName);
                PlayerNameFilter.this.setResult(RESULT_OK, data);
                PlayerNameFilter.this.finish();
            }
        }
    };
    private View.OnClickListener onCancel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PlayerNameFilter.this.setResult(RESULT_CANCELED);
            PlayerNameFilter.this.finish();
        }
    };
    private EditText playerNameText = null;
}
