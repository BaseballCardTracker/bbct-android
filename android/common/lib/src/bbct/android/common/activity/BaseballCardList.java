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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import bbct.android.common.R;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.exception.SQLHelperCreationException;
import bbct.android.common.provider.BaseballCardAdapter;
import bbct.android.common.provider.BaseballCardContract;
import bbct.android.common.provider.BaseballCardSQLHelper;
import bbct.android.common.provider.SQLHelperFactory;

/**
 * Displays a list of all baseball cards stored in the database.
 *
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
            savedSelection = null;

            this.setContentView(R.layout.card_list);
            if (savedInstanceState != null) {
                this.filterRequest = savedInstanceState.getInt(this
                        .getString(R.string.filter_request_extra));
                this.filterParams = savedInstanceState.getBundle(this
                        .getString(R.string.filter_params_extra));
                savedSelection = savedInstanceState.getBooleanArray(this
                        .getString(R.string.selection_extra));
            }

            this.emptyList = (TextView) this.findViewById(android.R.id.empty);
            if (this.filterRequest == R.id.no_filter) {
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
                            BaseballCardList.this.adapter.toggleAll(ctv
                                    .isChecked());
                        }
                    });
            listView.addHeaderView(this.headerView);

            this.adapter = new BaseballCardAdapter(this, R.layout.row, null,
                    ROW_PROJECTION, ROW_TEXT_VIEWS);
        } catch (SQLHelperCreationException ex) {
            // TODO Show a dialog and exit app
            Toast.makeText(this, R.string.database_error, Toast.LENGTH_LONG)
                    .show();
            Log.e(TAG, ex.getMessage(), ex);
        }
    }

    /**
     * Close the connection to the database when the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        this.sqlHelper.close();
    }

    /**
     * Save the state of checkboxes for all list items.
     */
    @Override
    public void onPause() {
        super.onPause();

        savedSelection = this.adapter.getSelection();
    }

    /**
     * Restore the state of {@link CheckedTextView} for all list items.
     */
    @Override
    public void onResume() {
        super.onResume();

        this.sqlHelper.applyFilter(this, this.filterRequest, this.filterParams);
        this.swapCursor();

        // restore default header state
        CheckedTextView headerCheck = (CheckedTextView) this.headerView
                .findViewById(R.id.checkmark);
        headerCheck.setChecked(false);

        // restore old state if it exists
        if (savedSelection != null && this.filterRequest == R.id.no_filter) {

            // array needs to be extended in case a card was added
            boolean[] newSelection = new boolean[this.adapter.getSelection().length];

            // copy old selection array into new selection array
            int numSelected = 0;
            for (int i = 0; i < savedSelection.length; i++) {
                newSelection[i] = savedSelection[i];

                if (newSelection[i]) {
                    numSelected++;
                }
            }

            // restore header state
            if (numSelected == this.adapter.getSelection().length
                    && newSelection.length == savedSelection.length) {
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
     * @param The
     *            The options menu in which new menu items are placed.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.option, menu);

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
            this.startActivityForResult(new Intent(this, FilterOptions.class),
                    R.id.filter_options_request);
            return true;
        } else if (itemId == R.id.clear_filter_menu) {
            this.filterRequest = R.id.no_filter;
            this.emptyList.setText(R.string.start);
            savedSelection = null;
            this.sqlHelper.clearFilter();
            this.swapCursor();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                this.invalidateOptionsMenu();
            }

            return true;
        } else if (itemId == R.id.delete_menu) {

            boolean[] selected = this.adapter.getSelection();
            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                    selected[i] = false;
                    BaseballCard card = this.getBaseballCard(this.adapter
                            .getView(i, null, null));
                    this.sqlHelper.removeBaseballCard(card);
                }
            }

            Toast.makeText(this, R.string.card_deleted_message,
                    Toast.LENGTH_LONG).show();

            this.adapter.setSelection(selected);
            this.sqlHelper.applyFilter(this, this.filterRequest,
                    this.filterParams);
            this.swapCursor();
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

        outState.putInt(this.getString(R.string.filter_request_extra),
                this.filterRequest);
        outState.putBundle(this.getString(R.string.filter_params_extra),
                this.filterParams);
        outState.putBooleanArray(this.getString(R.string.selection_extra),
                this.adapter.getSelection());
    }

    /**
     * Respond to the user clicking on an item in the list. If the user clicks
     * on a checkbox, then the corresponding row will be marked for the next
     * delete operation. Otherwise, the app will display the card data for
     * editing in a {@link BaseballCardDetails}.
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
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if (position == 0) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_EDIT,
                BaseballCardDetails.DETAILS_URI);
        BaseballCard card = BaseballCardList.this.adapter.getSelectedCard();

        intent.putExtra(this.getString(R.string.baseball_card_extra), card);
        intent.putExtra(this.getString(R.string.card_id_extra), id);
        intent.setType(BaseballCardContract.BASEBALL_CARD_ITEM_MIME_TYPE);
        BaseballCardList.this.startActivity(intent);
    }

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
        if (requestCode == R.id.filter_options_request) {
            if (resultCode == RESULT_OK) {
                this.filterRequest = data.getIntExtra(
                        this.getString(R.string.filter_request_extra),
                        DEFAULT_INT_EXTRA);
                this.filterParams = data.getExtras();
                this.emptyList.setText(R.string.empty_list);

                this.sqlHelper.applyFilter(this, this.filterRequest,
                        this.filterParams);
                this.swapCursor();
            }
        } else {
            Log.e(TAG, "onActivityResult(): Invalid result code: "
                    + requestCode);
            // TODO Throw exception?
        }
    }

    /**
     * Obtains {@link BaseballCard} from {@link View}. The returned
     * {@link BaseballCard} only includes partial data - data required to delete
     * a card.
     *
     * @param v
     *            - {@link View} object to obtain the card from
     * @return the {@link BaseballCard} containing year, brand, number and
     *         playerName.
     */
    private BaseballCard getBaseballCard(View v) {
        TextView yearCol = (TextView) v.findViewById(R.id.year_text_view);
        int year = Integer.parseInt(yearCol.getText().toString());

        TextView numCol = (TextView) v.findViewById(R.id.number_text_view);
        int number = Integer.parseInt(numCol.getText().toString());

        TextView nameCol = (TextView) v
                .findViewById(R.id.player_name_text_view);
        String player = nameCol.getText().toString();

        return new BaseballCard("", year, number, 0, 0, player, "", "");
    }

    @SuppressWarnings("deprecation")
    private void swapCursor() {
        Cursor newCursor = this.sqlHelper.getCursor();
        Cursor oldCursor = this.adapter.getCursor();

        this.adapter.setSelection(new boolean[newCursor.getCount()]);
        this.stopManagingCursor(oldCursor);
        this.startManagingCursor(newCursor);
        this.adapter.changeCursor(newCursor);

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

    private static final String TAG = BaseballCardList.class.getName();
    private static final int DEFAULT_INT_EXTRA = -1;
    private static boolean[] savedSelection;
    TextView emptyList = null;
    private BaseballCardSQLHelper sqlHelper = null;
    private BaseballCardAdapter adapter = null;
    private int filterRequest = R.id.no_filter;
    private Bundle filterParams = null;
    private View headerView;
}
