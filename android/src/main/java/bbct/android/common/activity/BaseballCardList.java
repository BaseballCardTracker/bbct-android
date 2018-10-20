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
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import bbct.android.common.R;
import bbct.android.common.database.BaseballCard;
import bbct.android.common.database.BaseballCardDao;
import bbct.android.common.database.BaseballCardDatabase;
import bbct.android.common.provider.BaseballCardAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BaseballCardList extends Fragment {
    private static final String FILTER_PARAMS = "filterParams";
    private static final String TAG = BaseballCardList.class.getName();

    @BindView(R.id.card_list)
    RecyclerView cardList;
    @BindView(R.id.add_button)
    FloatingActionButton addButton;

    private BaseballCardAdapter adapter = null;
    private Bundle filterParams = null;

    public static BaseballCardList getInstance(Bundle filterArgs) {
        BaseballCardList cardList = new BaseballCardList();

        Bundle args = new Bundle();
        args.putBundle(FILTER_PARAMS, filterArgs);
        cardList.setArguments(args);

        return cardList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = this.getArguments();

        if (savedInstanceState != null) {
            this.filterParams = savedInstanceState.getBundle(FILTER_PARAMS);
        } else if (args != null) {
            this.filterParams = args.getBundle(FILTER_PARAMS);
        }

        this.setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        Activity activity = Objects.requireNonNull(getActivity());
        activity.setTitle(R.string.app_name);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_list, container, false);
        ButterKnife.bind(this, view);
        setUpRecyclerView();

        final FragmentActivity activity = Objects.requireNonNull(getActivity());
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseballCardDetails details = new BaseballCardDetails();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_holder, details, FragmentTags.EDIT_CARD)
                        .addToBackStack(FragmentTags.EDIT_CARD)
                        .commit();
            }
        });

        /*
        View headerView = new HeaderView(activity);
        CheckBox selectAll = headerView.findViewById(R.id.select_all);
        selectAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !mCallbacks.isStarted()) {
                    activity.startActionMode(mCallbacks);
                } else if (mCallbacks.isStarted()) {
                    mCallbacks.finish();
                }

                BaseballCardList.this.setAllChecked(isChecked);
            }
        });
        */

        applyFilter(filterParams);

        return view;
    }

    private void setUpRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        cardList.setLayoutManager(layoutManager);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list, menu);
    }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        FragmentActivity activity = Objects.requireNonNull(getActivity());
        switch (itemId) {
            case R.id.filter_menu:
                FilterCards filterCards = new FilterCards();
                activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_holder, filterCards, FragmentTags.FILTER_CARDS)
                    .addToBackStack(FragmentTags.FILTER_CARDS)
                    .commit();
                return true;
            case R.id.clear_filter_menu:
                this.filterParams = null;
                this.applyFilter(null);
                activity.invalidateOptionsMenu();
                return true;
            default:
                Log.e(TAG, "onOptionsItemSelected(): Invalid menu code: " + itemId);
                // TODO Throw exception?
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBundle(FILTER_PARAMS, this.filterParams);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        Fragment details = BaseballCardDetails.getInstance(id);
        FragmentActivity activity = Objects.requireNonNull(getActivity());
        activity.getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_holder, details, FragmentTags.EDIT_CARD)
            .addToBackStack(FragmentTags.EDIT_CARD)
            .commit();
    }

    /*public void deleteSelectedCards() {
        final Activity activity = Objects.requireNonNull(getActivity());
        final List<BaseballCard> cards = new ArrayList<>();
        for (int i = 0; i < getListAdapter().getCount() + 1; ++i) {
            if (getListView().isItemChecked(i)) {
                BaseballCard card = this.adapter.getItem(i - 1);
                cards.add(card);
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                BaseballCardDatabase database =
                    BaseballCardDatabase.getInstance(activity);
                database.getBaseballCardDao().deleteBaseballCards(cards);
            }
        }).start();

        Toast.makeText(
            activity,
            R.string.card_deleted_message,
            Toast.LENGTH_LONG
        ).show();
    }*/

    /*private void setAllChecked(boolean checked) {
        ListView listView = this.getListView();
        // Add 1 for the header view
        for (int i = 0; i < this.adapter.getCount() + 1; ++i) {
            listView.setItemChecked(i, checked);
        }
    }*/

    private void applyFilter(Bundle filterParams) {
        LiveData<List<BaseballCard>> cards;
        final Activity activity = Objects.requireNonNull(getActivity());
        BaseballCardDatabase database =
            BaseballCardDatabase.getInstance(activity);
        BaseballCardDao dao = database.getBaseballCardDao();

        if (filterParams == null) {
            cards = dao.getBaseballCards();
        } else {
            String format = "%%%s%%";
            String brand = filterParams.getString(
                FilterCards.BRAND_EXTRA, "");
            String year = filterParams.getString(
                FilterCards.YEAR_EXTRA, "-1");
            String number = filterParams.getString(
                FilterCards.NUMBER_EXTRA, "");
            String playerName = filterParams.getString(
                FilterCards.PLAYER_NAME_EXTRA, "");
            String team = filterParams.getString(
                FilterCards.TEAM_EXTRA, "");

            cards = dao.getBaseballCards(
                String.format(format, brand),
                Integer.valueOf(year),
                String.format(format, number),
                String.format(format, playerName),
                String.format(format, team)
            );
        }

        cards.observe(this, new Observer<List<BaseballCard>>() {
            @Override
            public void onChanged(@Nullable List<BaseballCard> cards) {
                RecyclerView.Adapter adapter = new BaseballCardAdapter(getActivity(), cards);
                cardList.setAdapter(adapter);
            }
        });
    }
}
