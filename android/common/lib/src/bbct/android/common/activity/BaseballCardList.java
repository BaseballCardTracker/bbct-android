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
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import bbct.android.common.R;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.exception.SQLHelperCreationException;
import bbct.android.common.provider.BaseballCardContract;
import bbct.android.common.provider.BaseballCardSQLHelper;
import bbct.android.common.provider.CheckedCursorAdapter;
import bbct.android.common.provider.SQLHelperFactory;

/**
 * TODO: Make list fancier
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

            this.emptyList = (TextView) this.findViewById(android.R.id.empty);
            if (this.filterRequest == R.id.no_filter) {
                this.emptyList.setText(R.string.start);
            } else {
                this.emptyList.setText(R.string.empty_list);
            }

            ListView listView = (ListView) this.findViewById(android.R.id.list);
            View headerView = View.inflate(this, R.layout.list_header, null);
            ((CheckedTextView) headerView.findViewById(R.id.checkmark)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckedTextView ctv = (CheckedTextView) v.findViewById(R.id.checkmark);
                    ctv.toggle();
                    BaseballCardList.this.toggleAll(ctv.isChecked());
                }
            });
            listView.addHeaderView(headerView);

            this.adapter = new CheckedCursorAdapter(this, R.layout.row, null, ROW_PROJECTION, ROW_TEXT_VIEWS);
            this.setListAdapter(this.adapter);
            this.sqlHelper.applyFilter(this, this.filterRequest, this.filterParams);
            this.swapCursor();
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

        // update delete menu option
        MenuItem deleteItem = menu.findItem(R.id.delete_menu);
        deleteItem.setEnabled(false);

        ListView lst = this.getListView();
        for (int i = 1; i < lst.getChildCount(); i++) {
            View v = lst.getChildAt(i);

            CheckedTextView ctv = (CheckedTextView) v.findViewById(R.id.checkmark);
            if (ctv.isChecked()) {
                deleteItem.setEnabled(true);
                break;
            }
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
            this.filterRequest = R.id.no_filter;
            this.emptyList.setText(R.string.start);
            this.sqlHelper.clearFilter();
            this.swapCursor();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                invalidateOptionsMenu();
            }

            return true;
        } else if (itemId == R.id.delete_menu) {

            ListView lst = this.getListView();
            for (int i = 1; i < lst.getChildCount(); i++) {
                CheckedTextView ctv = (CheckedTextView) lst.getChildAt(i).findViewById(R.id.checkmark);

                if (ctv.isChecked()) {
                    ctv.setChecked(false);
                    BaseballCard card = this.getBaseballCard(lst.getChildAt(i));
                    this.sqlHelper.removeBaseballCard(card);
                }
            }

            Toast.makeText(this, R.string.card_deleted_message, Toast.LENGTH_LONG).show();
            this.sqlHelper.applyFilter(this, this.filterRequest, this.filterParams);
            this.swapCursor();
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
        if (position == 0)
            return;

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
                this.filterParams = data.getExtras();
                this.emptyList.setText(R.string.empty_list);

                this.sqlHelper.applyFilter(this, this.filterRequest, this.filterParams);
                this.swapCursor();
            }
        } else {
            Log.e(TAG, "onActivityResult(): Invalid result code: " + requestCode);
            // TODO Throw exception?
        }
    }

    /**
     * Obtains {@link BaseballCard} from {@link View}.
     * The returned {@link BaseballCard} only includes
     * partial data - data required to delete a card.
     * @param v - {@link View} object to obtain the card from
     * @return the {@link BaseballCard} containing year, brand,
     * number and playerName.
     */
    private BaseballCard getBaseballCard(View v) {
        TextView yearCol = (TextView) v.findViewById(R.id.year_text_view);
        int year = Integer.parseInt(yearCol.getText().toString());

        TextView numCol = (TextView) v.findViewById(R.id.number_text_view);
        int number = Integer.parseInt(numCol.getText().toString());

        TextView nameCol = (TextView) v.findViewById(R.id.player_name_text_view);
        String player = nameCol.getText().toString();

        return new BaseballCard("", year, number, 0, 0, player, "", "");
    }

    /**
     * Marks/unmarks all items in the {@link ListView}.
     * @param check - a boolean indicating whether all items will be checked
     */
    private void toggleAll(boolean check) {
        ListView lst = this.getListView();

        for (int i = 0; i < lst.getChildCount(); i++) {
            View v = lst.getChildAt(i);

            CheckedTextView ctv = (CheckedTextView) v.findViewById(R.id.checkmark);
            ctv.setChecked(check);
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
