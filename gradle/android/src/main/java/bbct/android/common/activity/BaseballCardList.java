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
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import bbct.android.common.activity.util.BaseballCardMultiChoiceModeListener;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.provider.BaseballCardAdapter;
import bbct.android.common.provider.BaseballCardContract;

/**
 * Displays a list of all baseball cards stored in the database.
 *
 * TODO: Make list fancier
 */
public class BaseballCardList extends ListFragment {

    public static BaseballCardList getInstance(Bundle filterArgs) {
        BaseballCardList cardList = new BaseballCardList();

        Bundle args = new Bundle();
        args.putBundle(FILTER_PARAMS, filterArgs);
        cardList.setArguments(args);

        return cardList;
    }

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

        Bundle args = this.getArguments();

        if (savedInstanceState != null) {
            this.filterParams = savedInstanceState.getBundle(FILTER_PARAMS);
        } else if (args != null) {
            this.filterParams = args.getBundle(FILTER_PARAMS);
        }

        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");

        View view = inflater.inflate(R.layout.card_list, container, false);

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
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new BaseballCardMultiChoiceModeListener());
        this.applyFilter(this.filterParams);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        this.adapter.setSelection(new boolean[this.adapter.getCount()]);
    }

    /**
     * Create the options menu. This is simply inflated from the
     * {@code list.xml} resource file.
     *
     * @param menu The options menu in which new menu items are placed.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list, menu);
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
        if (this.filterParams == null) {
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
            BaseballCardDetails details = new BaseballCardDetails();
            this.getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_holder, details)
                    .addToBackStack(EDIT_CARD)
                    .commit();
            return true;
        } else if (itemId == R.id.filter_menu) {
            FilterCards filterCards = new FilterCards();
            this.getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_holder, filterCards)
                    .addToBackStack(EDIT_CARD)
                    .commit();
            return true;
        } else if (itemId == R.id.clear_filter_menu) {
            this.emptyList.setText(R.string.start);
            this.applyFilter(null);

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
            this.applyFilter(this.filterParams);
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
     * @param outState The saved state.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBundle(FILTER_PARAMS, this.filterParams);
        outState.putBooleanArray(SELECTION_EXTRA, this.adapter.getSelection());
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

        BaseballCard card = BaseballCardList.this.adapter.getItem(position);

        Fragment details = BaseballCardDetails.getInstance(id, card);
        this.getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_holder, details)
                .addToBackStack(EDIT_CARD)
                .commit();
    }

    protected void applyFilter(Bundle filterParams) {
        Log.d(TAG, "applyFilter()");

        this.filterParams = filterParams;

        if (this.filterParams == null) {
            this.emptyList.setText(R.string.start);
        } else {
            this.emptyList.setText(R.string.empty_list);
        }

        StringBuilder sb = null;
        String[] args = null;

        if (this.filterParams != null) {
            sb = new StringBuilder();
            args = new String[this.filterParams.size()];

            int numQueries = 0;
            for (String key : this.filterParams.keySet()) {
                String value = this.filterParams.getString(key);

                if (key.equals(FilterCards.YEAR_EXTRA)) {
                    sb.append(BaseballCardContract.YEAR_SELECTION);
                } else if (key.equals(FilterCards.BRAND_EXTRA)) {
                    sb.append(BaseballCardContract.BRAND_SELECTION);
                } else if (key.equals(FilterCards.NUMBER_EXTRA)) {
                    sb.append(BaseballCardContract.NUMBER_SELECTION);
                } else if (key.equals(FilterCards.PLAYER_NAME_EXTRA)) {
                    sb.append(BaseballCardContract.PLAYER_NAME_SELECTION);
                } else if (key.equals(FilterCards.TEAM_EXTRA)) {
                    sb.append(BaseballCardContract.TEAM_SELECTION);
                } else {
                    Log.e(TAG, "Invalid key: " + key);
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

    private static final String FILTER_PARAMS = "filterParams";
    private static final String EDIT_CARD = "Edit Card";
    private static final String SELECTION_EXTRA = "selection";

    private static final String TAG = BaseballCardList.class.getName();
    TextView emptyList = null;
    private BaseballCardAdapter adapter = null;
    private Uri uri = null;
    private Bundle filterParams = null;
    private View headerView;

}
