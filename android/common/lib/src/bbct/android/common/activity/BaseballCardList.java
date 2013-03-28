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
import android.widget.TextView;
import android.widget.Toast;
import bbct.android.common.R;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.database.BaseballCardSQLHelper;
import bbct.android.common.database.SQLHelperFactory;
import bbct.android.common.exception.SQLHelperCreationException;
import bbct.android.common.provider.BaseballCardContract;

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
        Log.d(TAG, "onCreate()");
        Log.d(TAG, "savedInstanceState=" + savedInstanceState);

        try {
            super.onCreate(savedInstanceState);
            this.sqlHelper = SQLHelperFactory.getSQLHelper(this);

            this.setContentView(R.layout.card_list);
            if (savedInstanceState != null) {
                this.filterRequest = savedInstanceState.getInt(this.getString(R.string.filter_request_extra));
                this.filterParams = savedInstanceState.getBundle(this.getString(R.string.filter_params_extra));
            }

            int year = -1;
            int number = -1;
            if (this.filterRequest == R.id.year_filter_request) {
                year = this.filterParams.getInt(this.getString(R.string.year_extra));
                this.sqlHelper.filterCursorByYear(year);
            } else if (this.filterRequest == R.id.number_filter_request) {
                number = this.filterParams.getInt(this.getString(R.string.number_extra));
                this.sqlHelper.filterCursorByNumber(number);
            } else if (this.filterRequest == R.id.year_and_number_filter_request) {
                year = this.filterParams.getInt(this.getString(R.string.year_extra));
                number = this.filterParams.getInt(this.getString(R.string.number_extra));
                this.sqlHelper.filterCursorByYearAndNumber(year, number);
            } else if (this.filterRequest == R.id.player_name_filter_request) {
                String playerName = this.filterParams.getString(this.getString(R.string.player_name_extra));
                this.sqlHelper.filterCursorByPlayerName(playerName);
            } else if (this.filterRequest == R.id.no_filter) {
                this.sqlHelper.clearFilter();
            } else {
                Log.e(TAG, "onCreate(): Invalid filter request code: " + this.filterRequest);
                // TODO: Throw an exception?
            }

            Cursor cursor = this.sqlHelper.getCursor();
            this.startManagingCursor(cursor);

            this.emptyList = (TextView) this.findViewById(android.R.id.empty);
            if (this.filterRequest == R.id.no_filter) {
                this.emptyList.setText(R.string.start);
            } else {
                this.emptyList.setText(R.string.empty_list);
            }

            ListView listView = (ListView) this.findViewById(android.R.id.list);
            View headerView = View.inflate(this, R.layout.list_header, null);
            listView.addHeaderView(headerView);

            this.adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, ROW_PROJECTION, ROW_TEXT_VIEWS);
            this.setListAdapter(this.adapter);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.option, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (this.filterRequest == R.id.no_filter) {
            menu.findItem(R.id.filter_menu).setVisible(true);
            menu.findItem(R.id.clear_filter_menu).setVisible(false);
        } else {
            menu.findItem(R.id.filter_menu).setVisible(false);
            menu.findItem(R.id.clear_filter_menu).setVisible(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.add_menu) {
            Intent intent = new Intent(Intent.ACTION_EDIT, BaseballCardDetails.DETAILS_URI);
            intent.setType(BaseballCardContract.BASEBALL_CARD_ITEM_MIME_TYPE);
            this.startActivity(intent);
            return true;
        } else if (itemId == R.id.filter_menu) {
            this.startActivityForResult(new Intent(this, FilterOptions.class), R.id.filter_options_request);
            return true;
        } else if (itemId == R.id.clear_filter_menu) {
            this.emptyList.setText(R.string.start);
            this.sqlHelper.clearFilter();
            this.swapCursor();
            this.invalidateOptionsMenu();
            return true;
        } else if (itemId == R.id.about_menu) {
            this.startActivity(new Intent(this, About.class));
            return true;
        } else {
            Log.e(TAG, "onOptionsItemSelected(): Invalid menu code: " + itemId);
            // TODO Throw exceptoin?
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
        Intent intent = new Intent(Intent.ACTION_EDIT, BaseballCardDetails.DETAILS_URI);
        BaseballCard card = BaseballCardList.this.sqlHelper.getBaseballCardFromCursor();

        intent.putExtra(this.getString(R.string.baseball_card_extra), card);
        intent.setType(BaseballCardContract.BASEBALL_CARD_ITEM_MIME_TYPE);
        BaseballCardList.this.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == R.id.filter_options_request) {
            if (resultCode == RESULT_OK) {
                this.filterRequest = data.getIntExtra(this.getString(R.string.filter_request_extra), DEFAULT_INT_EXTRA);
                this.filterParams = new Bundle();
                this.emptyList.setText(R.string.empty_list);

                int year = -1;
                int number = -1;
                if (this.filterRequest == R.id.year_filter_request) {
                    year = data.getIntExtra(this.getString(R.string.year_extra), DEFAULT_INT_EXTRA);
                    this.filterParams.putInt(this.getString(R.string.year_extra), year);
                    this.sqlHelper.filterCursorByYear(year);
                } else if (this.filterRequest == R.id.number_filter_request) {
                    number = data.getIntExtra(this.getString(R.string.number_extra), DEFAULT_INT_EXTRA);
                    this.filterParams.putInt(this.getString(R.string.number_extra), number);
                    this.sqlHelper.filterCursorByNumber(number);
                } else if (this.filterRequest == R.id.year_and_number_filter_request) {
                    year = data.getIntExtra(this.getString(R.string.year_extra), DEFAULT_INT_EXTRA);
                    number = data.getIntExtra(this.getString(R.string.number_extra), DEFAULT_INT_EXTRA);
                    this.filterParams.putInt(this.getString(R.string.year_extra), year);
                    this.filterParams.putInt(this.getString(R.string.number_extra), number);
                    this.sqlHelper.filterCursorByYearAndNumber(year, number);
                } else if (this.filterRequest == R.id.player_name_filter_request) {
                    String playerName = data.getStringExtra(this.getString(R.string.player_name_extra));
                    this.filterParams.putString(this.getString(R.string.player_name_extra), playerName);
                    this.sqlHelper.filterCursorByPlayerName(playerName);
                } else {
                    Log.e(TAG, "onActivityResult(): Invalid filter request code: " + this.filterRequest);
                    // TODO: Throw an exception?
                }

                this.swapCursor();
            }
        } else {
            Log.e(TAG, "onActivityResult(): Invalid result code: " + requestCode);
            // TODO Throw exception?
        }
    }

    private void swapCursor() {
        Cursor cursor = this.sqlHelper.getCursor();
        this.stopManagingCursor(this.adapter.getCursor());
        this.startManagingCursor(cursor);
        this.adapter.changeCursor(cursor);
    }
    private static final String[] ROW_PROJECTION = {
        BaseballCardContract.BRAND_COL_NAME, BaseballCardContract.YEAR_COL_NAME,
        BaseballCardContract.NUMBER_COL_NAME, BaseballCardContract.PLAYER_NAME_COL_NAME
    };
    private static final int[] ROW_TEXT_VIEWS = {
        R.id.brand_text_view, R.id.year_text_view, R.id.number_text_view, R.id.player_name_text_view
    };
    private static final String TAG = BaseballCardList.class.getName();
    private static final int DEFAULT_INT_EXTRA = -1;
    TextView emptyList = null;
    private BaseballCardSQLHelper sqlHelper = null;
    private CursorAdapter adapter = null;
    private int filterRequest = R.id.no_filter;
    private Bundle filterParams = null;
}
