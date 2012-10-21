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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import bbct.common.data.BaseballCard;

/**
 * TODO: Make list fancier
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
        this.sqlHelper = new BaseballCardSQLHelper(this);
        Cursor cursor = this.sqlHelper.getCursor();
        this.startManagingCursor(cursor);
        this.adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, ROW_PROJECTION, ROW_TEXT_VIEWS);
        this.setListAdapter(this.adapter);

        this.setContentView(R.layout.card_list);
        if (savedInstanceState != null) {
            this.filterRequest = savedInstanceState.getInt(this.getString(R.string.filter_request_extra));
            this.filterParams = savedInstanceState.getBundle(this.getString(R.string.filter_params_extra));
        }

        switch (this.filterRequest) {
            case R.id.year_filter_request:
                int year = this.filterParams.getInt(this.getString(R.string.year_extra));
                this.sqlHelper.filterCursorByYear(year);
                break;

            case R.id.number_filter_request:
                int number = this.filterParams.getInt(this.getString(R.string.number_extra));
                this.sqlHelper.filterCursorByNumber(number);
                break;

            case R.id.year_and_number_filter_request:
                year = this.filterParams.getInt(this.getString(R.string.year_extra));
                number = this.filterParams.getInt(this.getString(R.string.number_extra));
                this.sqlHelper.filterCursorByYearAndNumber(year, number);
                break;

            case R.id.player_name_filter_request:
                String playerName = this.filterParams.getString(this.getString(R.string.player_name_extra));
                this.sqlHelper.filterCursorByPlayerName(playerName);
                break;

            default:
                Log.e(TAG, "onCreate(): Invalid filter request code.");
                // TODO: Throw an exception?
                break;
        }
        
        if (cursor.getCount() == 0) {
            Toast.makeText(this, R.string.start, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.sqlHelper.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.option, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (this.filterRequest != R.id.no_filter) {
            MenuItem filterMenuItem = menu.findItem(R.id.filter_menu);
            filterMenuItem.setTitle(R.string.clear_filter_menu);
            filterMenuItem.setIcon(R.drawable.ic_menu_block);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_menu:
                this.startActivity(new Intent(this, BaseballCardDetails.class));
                return true;

            case R.id.filter_menu:
                if (this.filterRequest != R.id.no_filter) {
                    this.filterRequest = R.id.no_filter;
                    this.sqlHelper.clearFilter();
                    this.adapter.swapCursor(this.sqlHelper.getCursor());
                    this.invalidateOptionsMenu();
                } else {
                    this.startActivityForResult(new Intent(this, FilterOptions.class), R.id.filter_options_request);
                }
                return true;

            case R.id.about_menu:
                this.startActivity(new Intent(this, About.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(this.getString(R.string.filter_request_extra), this.filterRequest);
        outState.putBundle(this.getString(R.string.filter_params_extra), this.filterParams);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent i = new Intent(BaseballCardList.this, BaseballCardDetails.class);
        BaseballCard card = BaseballCardList.this.sqlHelper.getBaseballCardFromCursor();

        i.putExtra(this.getString(R.string.baseball_card_extra), card);
        BaseballCardList.this.startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case R.id.filter_options_request:
                if (resultCode == RESULT_OK) {
                    this.filterRequest = data.getIntExtra(this.getString(R.string.filter_request_extra), DEFAULT_INT_EXTRA);
                    this.filterParams = new Bundle();

                    switch (this.filterRequest) {
                        case R.id.year_filter_request:
                            int year = data.getIntExtra(this.getString(R.string.year_extra), DEFAULT_INT_EXTRA);
                            this.filterParams.putInt(this.getString(R.string.year_extra), year);
                            this.sqlHelper.filterCursorByYear(year);
                            break;

                        case R.id.number_filter_request:
                            int number = data.getIntExtra(this.getString(R.string.number_extra), DEFAULT_INT_EXTRA);
                            this.filterParams.putInt(this.getString(R.string.number_extra), number);
                            this.sqlHelper.filterCursorByNumber(number);
                            break;

                        case R.id.year_and_number_filter_request:
                            year = data.getIntExtra(this.getString(R.string.year_extra), DEFAULT_INT_EXTRA);
                            number = data.getIntExtra(this.getString(R.string.number_extra), DEFAULT_INT_EXTRA);
                            this.filterParams.putInt(this.getString(R.string.year_extra), year);
                            this.filterParams.putInt(this.getString(R.string.number_extra), number);
                            this.sqlHelper.filterCursorByYearAndNumber(year, number);
                            break;

                        case R.id.player_name_filter_request:
                            String playerName = data.getStringExtra(this.getString(R.string.player_name_extra));
                            this.filterParams.putString(this.getString(R.string.player_name_extra), playerName);
                            this.sqlHelper.filterCursorByPlayerName(playerName);
                            break;

                        default:
                            Log.e(TAG, "onActivityResult(): Invalid filter request code.");
                            // TODO: Throw an exception?
                            break;
                    }

                    Cursor cursor = this.sqlHelper.getCursor();
                    if (cursor.getCount() == 0) {
                        String msg = this.getString(R.string.no_cards_message);
                        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                    } else {
                        BaseballCardList.this.adapter.swapCursor(cursor);
                    }
                }
                break;
        }
    }
    private static final String[] ROW_PROJECTION = {
        BaseballCardSQLHelper.BRAND_COL_NAME, BaseballCardSQLHelper.YEAR_COL_NAME,
        BaseballCardSQLHelper.NUMBER_COL_NAME, BaseballCardSQLHelper.PLAYER_NAME_COL_NAME
    };
    private static final int[] ROW_TEXT_VIEWS = {
        R.id.brand_text_view, R.id.year_text_view, R.id.number_text_view, R.id.player_name_text_view
    };
    private static final String TAG = BaseballCardList.class.getName();
    private static final int DEFAULT_INT_EXTRA = -1;
    private BaseballCardSQLHelper sqlHelper = null;
    private CursorAdapter adapter = null;
    private int filterRequest = R.id.no_filter;
    private Bundle filterParams = null;
}
