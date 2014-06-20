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
package bbct.android.common.activity.util;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.ListView;
import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardList;
import bbct.android.common.provider.BaseballCardAdapter;

public class BaseballCardActionModeCallback implements ActionMode.Callback,
        AdapterView.OnItemLongClickListener, View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = BaseballCardActionModeCallback.class.getName();

    private BaseballCardAdapter adapter;
    private ActionBarActivity activity;
    private BaseballCardList listFragment;
    private AdapterView.OnItemClickListener oldOnItemClick;
    private ActionMode mode;
    private int count;

    public BaseballCardActionModeCallback(BaseballCardList listFragment) {
        this.activity = (ActionBarActivity) listFragment.getActivity();
        this.listFragment = listFragment;

        this.adapter = (BaseballCardAdapter) this.listFragment.getListAdapter();
    }

    /**
     * Called when action mode is first created. The menu supplied will be used to
     * generate action buttons for the action mode.
     *
     * @param mode ActionMode being created
     * @param menu Menu used to populate action buttons
     * @return true if the action mode should be created, false if entering this
     * mode should be aborted.
     */
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.context, menu);
        return true;
    }

    /**
     * Called to refresh an action mode's action menu whenever it is invalidated.
     *
     * @param mode ActionMode being prepared
     * @param menu Menu used to populate action buttons
     * @return true if the menu or action mode was updated, false otherwise.
     */
    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    /**
     * Called to report a user click on an action button.
     *
     * @param mode The current ActionMode
     * @param item The item that was clicked
     * @return true if this callback handled the event, false if the standard MenuItem
     * invocation should continue.
     */
    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_menu:
                this.listFragment.deleteSelectedCards();
                return true;

            default:
                return false;
        }
    }

    /**
     * Called when an action mode is about to be exited and destroyed.
     *
     * @param mode The current ActionMode being destroyed
     */
    @Override
    public void onDestroyActionMode(ActionMode mode) {
        this.mode = null;
        this.listFragment.getListView().setOnItemClickListener(this.oldOnItemClick);
    }

    /**
     * Callback method to be invoked when an item in this view has been
     * clicked and held.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need to access
     * the data associated with the selected item.
     *
     * @param parent   The AbsListView where the click happened
     * @param view     The view within the AbsListView that was clicked
     * @param position The position of the view in the list
     * @param id       The row id of the item that was clicked
     * @return true if the callback consumed the long click, false otherwise
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Checkable ctv = (Checkable) view.findViewById(R.id.checkmark);
        if (this.mode == null) {
            ctv.setChecked(true);
            start();
        } else {
            ctv.toggle();
        }

        if (ctv.isChecked()) {
            this.count++;
        } else {
            this.count--;
        }

        return true;
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemClick()");
        Log.d(TAG, "  view=" + view);
        Log.d(TAG, "  position=" + position);
        Log.d(TAG, "  id=" + id);

        Checkable ctv = (Checkable) view.findViewById(R.id.checkmark);
        ctv.toggle();

        if (ctv.isChecked()) {
            this.count++;
        } else {
            this.count--;
        }

        if (this.count == 0) {
            this.mode.finish();
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick()");
        Log.d(TAG, "  v=" + v);

        if (this.mode == null) {
            start();
        }

        if (((Checkable)v).isChecked()) {
            this.count++;
        } else {
            this.count--;
        }

        if (this.count == 0) {
            this.mode.finish();
        }
    }

    private void start() {
        this.oldOnItemClick = this.listFragment.getListView().getOnItemClickListener();
        this.listFragment.getListView().setOnItemClickListener(this);
        this.mode = this.activity.startSupportActionMode(this);
        this.count = 0;
    }

    public void setAllChecked(boolean checked) {
        ListView listView = this.listFragment.getListView();

        for(int i=1; i < listView.getChildCount(); i++){
            View itemLayout = listView.getChildAt(i);
            Checkable cb = (Checkable)itemLayout.findViewById(R.id.checkmark);
            cb.setChecked(checked);
        }
    }

    public boolean isStarted() {
        return this.mode != null;
    }

}
