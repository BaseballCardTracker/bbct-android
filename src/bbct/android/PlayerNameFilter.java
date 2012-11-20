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

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class PlayerNameFilter extends FilterActivity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.player_name_filter, R.string.player_name_filter_title);

        this.playerNameText = (EditText) this.findViewById(R.id.player_name_text);
    }

    @Override
    public boolean isInputValid() {
        String playerName = this.playerNameText.getText().toString();
        return !playerName.equals("");
    }
    
    @Override
    public int getErrorResourceId() {
        return R.string.player_name_input_error;
    }

    @Override
    public Intent getResult() {
        String playerName = this.playerNameText.getText().toString();
        Intent result = new Intent();
        result.putExtra(this.getString(R.string.filter_request_extra), R.id.player_name_filter_request);
        result.putExtra(this.getString(R.string.player_name_extra), playerName);

        return result;
    }
    private EditText playerNameText = null;
}
