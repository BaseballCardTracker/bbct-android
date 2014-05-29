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

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
 * <p/>
 * TODO: Make list fancier
 */
public class BaseballCardList extends ListFragment {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        Log.d(TAG, "  savedInstanceState=" + savedInstanceState);

        super.onCreate(savedInstanceState);

        this.adapter = new BaseballCardAdapter(this.getActivity(),
                R.layout.row, null, ROW_PROJECTION, ROW_TEXT_VIEWS);
        this.setListAdapter(this.adapter);

        Log.d(TAG, "  adapter=" + this.adapter);

        this.uri = BaseballCardContract.getUri(this.getActivity()
                .getPackageName());
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");

        View view = inflater.inflate(R.layout.card_list, null);
        this.filterActive = false;
        if (savedInstanceState != null) {
            this.filterActive = savedInstanceState.getBoolean(this
                    .getString(R.string.filter_status_extra));
            this.filterParams = savedInstanceState.getBundle(this
                    .getString(R.string.filter_params_extra));
        }

        this.emptyList = (TextView) view.findViewById(android.R.id.empty);

        ListView listView = (ListView) view.findViewById(android.R.id.list);
        this.headerView = View.inflate(this.getActivity(),
                R.layout.list_header, null);
        this.headerView.findViewById(R.id.checkmark)
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
        listView.setAdapter(this.adapter);
        this.applyFilter(false, null);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        this.adapter.setSelection(new boolean[this.adapter.getCount()]);
    }

    /**
     * Create the options menu. This is simply inflated from the
     * {@code option.xml} resource file.
     *
     * @param menu The options menu in which new menu items are placed.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.option, menu);
    }

    /**
     * Prepare the options menu to be displayed. If a filter is active and
     * displays the "Clear Filter" menu item, otherwise displays the
     * "Filter Cards" menu item.
     *
     * @param menu The options menu which will be displayed.
     */
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG, "onPrepareOptionsMenu()");

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

        Log.d(TAG, "  adapter=" + this.adapter);

        boolean[] curSelection = this.adapter.getSelection();
        if (curSelection != null) {
            for (boolean b : curSelection) {
                if (b) {
                    deleteItem.setEnabled(true);
                    break;
                }
            }
        }
    }

    /**
     * Respond to the user selecting a menu item.
     *
     * @param item The menu item selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.add_menu) {
            Intent intent = new Intent(this.getActivity(), BaseballCardDetails.class);
            this.startActivity(intent);
            return true;
        } else if (itemId == R.id.filter_menu) {
            Intent intent = new Intent(this.getActivity(), FilterCards.class);
            this.startActivityForResult(intent, FILTER_CARDS_REQUEST);
            return true;
        } else if (itemId == R.id.clear_filter_menu) {
            this.emptyList.setText(R.string.start);
            this.applyFilter(false, null);

            this.getActivity().supportInvalidateOptionsMenu();

            return true;
        } else if (itemId == R.id.delete_menu) {

            boolean[] selected = this.adapter.getSelection();
            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                    selected[i] = false;
                    long id = this.adapter.getItemId(i);
                    Uri deleteUri = ContentUris.withAppendedId(this.uri, id);
                    this.getActivity().getContentResolver()
                            .delete(deleteUri, null, null);
                }
            }

            Toast.makeText(this.getActivity(), R.string.card_deleted_message,
                    Toast.LENGTH_LONG).show();

            this.adapter.setSelection(selected);
            this.applyFilter(this.filterActive, this.filterParams);
            return true;

        } else if (itemId == R.id.about_menu) {
            this.startActivity(new Intent(this.getActivity(), About.class));
            return true;
        } else {
            Log.e(TAG, "onOptionsItemSelected(): Invalid menu code: " + itemId);
            // TODO Throw exception?
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Respond to the result of a child activity by applying a filter after
     * {@link FilterCards} returns the appropriate parameters.
     *
     * @param requestCode
     *            The integer request code originally supplied to
     *            {@link #startActivityForResult(Intent, int)}, allowing you to identify
     *            who this result came from.
     * @param resultCode
     *            The integer result code returned by the child activity through
     *            its {@link Activity#setResult(int)}.
     * @param data
     *            An Intent with the data returned by the child activity.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILTER_CARDS_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle filterParams = data.getExtras();

                this.applyFilter(true, filterParams);

                this.getActivity().supportInvalidateOptionsMenu();
            }
        } else {
            Log.e(TAG, "onActivityResult(): Invalid result code: "
                    + requestCode);
            // TODO Throw exception?
        }
    }

    /**
     * Save the currently active filter when the system asks for it. Also save
     * the current state of marked list items.
     *
     * @param outState The saved state.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(this.getString(R.string.filter_status_extra),
                this.filterActive);
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
     * @param l        The ListView where the click happened.
     * @param v        The view that was clicked within the ListView.
     * @param position The position of the view in the list.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (position == 0) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_EDIT,
                BaseballCardDetails.DETAILS_URI);
        BaseballCard card = BaseballCardList.this.adapter.getSelectedCard();

        intent.putExtra(
                BaseballCardList.this.getString(R.string.baseball_card_extra),
                card);
        intent.setType(BaseballCardContract.BASEBALL_CARD_ITEM_MIME_TYPE);
        BaseballCardList.this.startActivity(intent);
    }

    protected void applyFilter(boolean filterActive, Bundle filterParams) {
        Log.d(TAG, "applyFilter()");

        this.filterActive = filterActive;
        this.filterParams = filterParams;

        if (this.filterActive) {
            this.emptyList.setText(R.string.empty_list);
        } else {
            this.emptyList.setText(R.string.start);
        }

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

        Cursor cursor = this
                .getActivity()
                .getContentResolver()
                .query(this.uri, BaseballCardContract.PROJECTION,
                        sb == null ? null : sb.toString(), args, null);
        this.swapCursor(cursor);
    }

    @SuppressWarnings("deprecation")
    private void swapCursor(Cursor newCursor) {
        Log.d(TAG, "swapCursor()");
        Cursor oldCursor = this.adapter.getCursor();
        this.getActivity().stopManagingCursor(oldCursor);

        if (newCursor != null) {
            this.adapter.setSelection(new boolean[newCursor.getCount()]);
            this.getActivity().startManagingCursor(newCursor);
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
            BaseballCardContract.PLAYER_NAME_COL_NAME};

    private static final int[] ROW_TEXT_VIEWS = {R.id.brand_text_view,
            R.id.year_text_view, R.id.number_text_view,
            R.id.player_name_text_view};

    private static final int FILTER_CARDS_REQUEST = 0x0001;
    private static final String TAG = BaseballCardList.class.getName();
    TextView emptyList = null;
    private BaseballCardAdapter adapter = null;
    private Uri uri = null;
    private boolean filterActive = false;
    private Bundle filterParams = null;
    private View headerView;

}
