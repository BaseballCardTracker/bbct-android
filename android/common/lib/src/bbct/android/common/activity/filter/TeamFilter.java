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
package bbct.android.common.activity.filter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import bbct.android.common.R;

/**
 *
 */
public class TeamFilter extends FilterActivity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.team_filter, R.string.team_filter_title);

        this.teamText = (EditText) this.findViewById(R.id.team_text);
    }

    @Override
    public boolean validateInput() {
        String team = this.teamText.getText().toString();
        boolean validTeam = !team.equals("");

        if (!validTeam) {
            this.teamText.setError(this.getString(R.string.team_input_error));
        }

        return validTeam;
    }

    @Override
    public Intent getResult() {
        String team = this.teamText.getText().toString();
        Intent result = new Intent();
        result.putExtra(this.getString(R.string.filter_request_extra), R.id.team_filter_request);
        result.putExtra(this.getString(R.string.team_extra), team);

        return result;
    }
    private EditText teamText = null;
}
