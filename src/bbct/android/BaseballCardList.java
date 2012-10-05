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

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import bbct.common.data.BaseballCard;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardList extends ListActivity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.card_list);

        this.sqlHelper = new BaseballCardSQLHelper(this);
        this.sqlHelper.unfilterCursor();

        Cursor cursor = this.sqlHelper.getCursor();
        this.startManagingCursor(cursor);
        CursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, ROW_PROJECTION, ROW_TEXT_VIEWS);
        this.setListAdapter(adapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.sqlHelper.close();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent i = new Intent(BaseballCardList.this, BaseballCardDetails.class);
        BaseballCard card = BaseballCardList.this.sqlHelper.getBaseballCardFromCursor();

        i.putExtra(AndroidConstants.BASEBALL_CARD_EXTRA, card);
        BaseballCardList.this.startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.option, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_menu:
                this.startActivity(new Intent(this, BaseballCardDetails.class));
                return true;

            case R.id.filter_menu:
                this.startActivity(new Intent(this, FilterOptions.class));
                return true;

            case R.id.about_menu:
                this.startActivity(new Intent(this, About.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private static final String[] ROW_PROJECTION = {
        BaseballCardSQLHelper.BRAND_COL_NAME, BaseballCardSQLHelper.YEAR_COL_NAME,
        BaseballCardSQLHelper.NUMBER_COL_NAME, BaseballCardSQLHelper.PLAYER_NAME_COL_NAME
    };
    private static final int[] ROW_TEXT_VIEWS = {
        R.id.brand_row, R.id.year_row, R.id.number_row, R.id.player_name_row
    };
    private BaseballCardSQLHelper sqlHelper = null;
}
