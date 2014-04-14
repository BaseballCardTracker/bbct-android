/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2012-14 codeguru <codeguru@users.sourceforge.net>
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

import android.content.ContentUris;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import bbct.android.common.R;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.provider.BaseballCardAdapter;
import bbct.android.common.provider.BaseballCardContract;

/**
 * Displays a list of all baseball cards stored in the database.
 *
 * TODO: Make list fancier
 */
public class BaseballCardList extends ActionBarActivity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        Log.d(TAG, "savedInstanceState=" + savedInstanceState);

        super.onCreate(savedInstanceState);
        this.savedSelection = null;

        this.setContentView(R.layout.card_list);
        this.filterActive = false;
        if (savedInstanceState != null) {
            this.filterActive = savedInstanceState.getBoolean(this
                    .getString(R.string.filter_status_extra));
            this.filterParams = savedInstanceState.getBundle(this
                    .getString(R.string.filter_params_extra));
            this.savedSelection = savedInstanceState.getBooleanArray(this
                    .getString(R.string.selection_extra));
        }

        this.emptyList = (TextView) this.findViewById(android.R.id.empty);
        if (!this.filterActive) {
            this.emptyList.setText(R.string.start);
        } else {
            this.emptyList.setText(R.string.empty_list);
        }

        ListView listView = (ListView) this.findViewById(android.R.id.list);
        this.headerView = View.inflate(this, R.layout.list_header, null);
        ((CheckedTextView) this.headerView.findViewById(R.id.checkmark))
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckedTextView ctv = (CheckedTextView) v
                                .findViewById(R.id.checkmark);
                        ctv.toggle();
                        BaseballCardList.this.adapter.toggleAll(ctv.isChecked());
                    }
                });
        listView.addHeaderView(this.headerView);
        listView.setEmptyView(this.emptyList);
        listView.setOnItemClickListener(this.onCardClick);

        this.adapter = new BaseballCardAdapter(this, R.layout.row, null,
                ROW_PROJECTION, ROW_TEXT_VIEWS);

        this.uri = BaseballCardContract.getUri(this.getPackageName());
    }

    /**
     * Save the state of checkboxes for all list items.
     */
    @Override
    public void onPause() {
        super.onPause();

        this.savedSelection = this.adapter.getSelection();
    }

    /**
     * Restore the state of {@link CheckedTextView} for all list items.
     */
    @Override
    public void onResume() {
        super.onResume();

        this.applyFilter();

        // restore default header state
        CheckedTextView headerCheck = (CheckedTextView) this.headerView
                .findViewById(R.id.checkmark);
        headerCheck.setChecked(false);

        // restore old state if it exists
        if (this.savedSelection != null && !this.filterActive) {

            // array needs to be extended in case a card was added
            boolean[] newSelection = new boolean[this.adapter.getSelection().length];

            // copy old selection array into new selection array
            int numSelected = 0;
            for (int i = 0; i < this.savedSelection.length; i++) {
                newSelection[i] = this.savedSelection[i];

                if (newSelection[i]) {
                    numSelected++;
                }
            }

            // restore header state
            if (numSelected == this.adapter.getSelection().length
                    && newSelection.length == this.savedSelection.length) {
                headerCheck.setChecked(true);
            }

            // restore state
            this.adapter.setSelection(newSelection);
        }
    }

    /**
     * Create the options menu. This is simply inflated from the
     * {@code option.xml} resource file.
     *
     * @param menu
     *            The options menu in which new menu items are placed.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.option, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Prepare the options menu to be displayed. If a filter is active and
     * displays the "Clear Filter" menu item, otherwise displays the
     * "Filter Cards" menu item.
     *
     * @param menu
     *            The options menu which will be displayed.
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem filter = menu.findItem(R.id.filter_menu);
        MenuItem clearFilter = menu.findItem(R.id.clear_filter_menu);
        if (!this.filterActive) {
            filter.setVisible(true);
            filter.setEnabled(true);

            clearFilter.setVisible(false);
            clearFilter.setEnabled(false);
        } else {
            filter.setVisible(false);
            filter.setEnabled(false);

            clearFilter.setVisible(true);
            clearFilter.setEnabled(true);
        }

        // update delete menu option
        MenuItem deleteItem = menu.findItem(R.id.delete_menu);
        deleteItem.setEnabled(false);

        boolean[] curSelection = this.adapter.getSelection();
        if (curSelection != null) {
            for (boolean b : curSelection) {
                if (b) {
                    deleteItem.setEnabled(true);
                    break;
                }
            }
        }

        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Respond to the user selecting a menu item.
     *
     * @param item
     *            The menu item selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.add_menu) {
            Intent intent = new Intent(Intent.ACTION_EDIT,
                    BaseballCardDetails.DETAILS_URI);
            intent.setType(BaseballCardContract.BASEBALL_CARD_ITEM_MIME_TYPE);
            this.startActivity(intent);
            return true;
        } else if (itemId == R.id.filter_menu) {
            this.startActivityForResult(new Intent(this, FilterCards.class),
                    FILTER_CARDS_REQUEST);
            return true;
        } else if (itemId == R.id.clear_filter_menu) {
            this.filterActive = false;
            this.filterParams = null;
            this.emptyList.setText(R.string.start);
            this.savedSelection = null;
            this.applyFilter();

            this.supportInvalidateOptionsMenu();

            return true;
        } else if (itemId == R.id.delete_menu) {

            boolean[] selected = this.adapter.getSelection();
            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                    selected[i] = false;
                    long id = this.adapter.getItemId(i);
                    Uri deleteUri = ContentUris.withAppendedId(this.uri, id);
                    this.getContentResolver().delete(deleteUri, null, null);
                }
            }

            Toast.makeText(this, R.string.card_deleted_message,
                    Toast.LENGTH_LONG).show();

            this.adapter.setSelection(selected);
            this.applyFilter();
            return true;

        } else if (itemId == R.id.about_menu) {
            this.startActivity(new Intent(this, About.class));
            return true;
        } else {
            Log.e(TAG, "onOptionsItemSelected(): Invalid menu code: " + itemId);
            // TODO Throw exception?
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Save the currently active filter when the system asks for it. Also save
     * the current state of marked list items.
     *
     * @param outState
     *            The saved state.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(this.getString(R.string.filter_status_extra),
                this.filterActive);
        outState.putBundle(this.getString(R.string.filter_params_extra),
                this.filterParams);
        outState.putBooleanArray(this.getString(R.string.selection_extra),
                this.adapter.getSelection());
    }

    private final OnItemClickListener onCardClick = new OnItemClickListener() {
        /**
         * Respond to the user clicking on an item in the list. If the user
         * clicks on a checkbox, then the corresponding row will be marked for
         * the next delete operation. Otherwise, the app will display the card
         * data for editing in a {@link BaseballCardDetails}.
         *
         * @param l
         *            The ListView where the click happened.
         * @param v
         *            The view that was clicked within the ListView.
         * @param position
         *            The position of the view in the list.
         * @param id
         *            The row id of the item that was clicked.
         */
        @Override
        public void onItemClick(AdapterView<?> row, View v, int position,
                long id) {
            if (position == 0) {
                return;
            }

            Intent intent = new Intent(Intent.ACTION_EDIT,
                    BaseballCardDetails.DETAILS_URI);
            BaseballCard card = BaseballCardList.this.adapter.getSelectedCard();

            intent.putExtra(BaseballCardList.this
                    .getString(R.string.baseball_card_extra), card);
            intent.setType(BaseballCardContract.BASEBALL_CARD_ITEM_MIME_TYPE);
            BaseballCardList.this.startActivity(intent);
        }
    };

    /**
     * Respond to the result of a child activity by applying a filter after
     * {@link FilterOptions} returns the appropriate parameters.
     *
     * @param requestCode
     *            The integer request code originally supplied to
     *            {@link #startActivityForResult()}, allowing you to identify
     *            who this result came from.
     * @param resultCode
     *            The integer result code returned by the child activity through
     *            its {@link #setResult()}.
     * @param data
     *            An Intent with the data returned by the child activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILTER_CARDS_REQUEST) {
            if (resultCode == RESULT_OK) {
                this.filterActive = true;
                this.filterParams = data.getExtras();
                this.emptyList.setText(R.string.empty_list);

                this.applyFilter();

                this.supportInvalidateOptionsMenu();
            }
        } else {
            Log.e(TAG, "onActivityResult(): Invalid result code: "
                    + requestCode);
            // TODO Throw exception?
        }
    }

    private void applyFilter() {
        Log.d(TAG, "applyFilter()");

        Resources res = this.getResources();
        StringBuilder sb = null;
        String[] args = null;

        if (this.filterParams != null) {
            sb = new StringBuilder();
            args = new String[this.filterParams.size()];

            int numQueries = 0;
            for (String key : this.filterParams.keySet()) {
                String value = this.filterParams.getString(key);

                if (key.equals(res.getString(R.string.year_extra))) {
                    sb.append(BaseballCardContract.YEAR_SELECTION);
                } else if (key.equals(res.getString(R.string.brand_extra))) {
                    sb.append(BaseballCardContract.BRAND_SELECTION);
                } else if (key.equals(res.getString(R.string.number_extra))) {
                    sb.append(BaseballCardContract.NUMBER_SELECTION);
                } else if (key
                        .equals(res.getString(R.string.player_name_extra))) {
                    sb.append(BaseballCardContract.PLAYER_NAME_SELECTION);
                } else {
                    sb.append(BaseballCardContract.TEAM_SELECTION);
                }

                args[numQueries] = value;
                numQueries++;

                if (numQueries < args.length) {
                    sb.append(" AND ");
                }
            }
        }

        Cursor cursor = this.getContentResolver().query(this.uri,
                BaseballCardContract.PROJECTION,
                sb == null ? null : sb.toString(), args, null);
        this.swapCursor(cursor);
    }

    @SuppressWarnings("deprecation")
    private void swapCursor(Cursor newCursor) {
        Cursor oldCursor = this.adapter.getCursor();
        this.stopManagingCursor(oldCursor);

        if (newCursor != null) {
            this.adapter.setSelection(new boolean[newCursor.getCount()]);
            this.startManagingCursor(newCursor);
            this.adapter.changeCursor(newCursor);
        }

        if (oldCursor != null) {
            oldCursor.close();
        }
    }

    private static final String[] ROW_PROJECTION = {
            BaseballCardContract.BRAND_COL_NAME,
            BaseballCardContract.YEAR_COL_NAME,
            BaseballCardContract.NUMBER_COL_NAME,
            BaseballCardContract.PLAYER_NAME_COL_NAME };

    private static final int[] ROW_TEXT_VIEWS = { R.id.brand_text_view,
            R.id.year_text_view, R.id.number_text_view,
            R.id.player_name_text_view };

    private static final int FILTER_CARDS_REQUEST = 0x0001;
    private static final String TAG = BaseballCardList.class.getName();
    private boolean[] savedSelection;
    TextView emptyList = null;
    private BaseballCardAdapter adapter = null;
    private Uri uri = null;
    private boolean filterActive = false;
    private Bundle filterParams = null;
    private View headerView;
}
