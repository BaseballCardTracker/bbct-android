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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbct.android.common.R;
import bbct.android.common.activity.util.BaseballCardMultiChoiceModeListener;
import bbct.android.common.database.BaseballCard;
import bbct.android.common.database.BaseballCardDao;
import bbct.android.common.database.BaseballCardDatabase;
import bbct.android.common.provider.BaseballCardAdapter;
import bbct.android.common.view.HeaderView;
import butterknife.BindView;
import butterknife.ButterKnife;

//TODO: Make list fancier
public class BaseballCardList extends ListFragment {
    private static final String FILTER_PARAMS = "filterParams";
    private static final String TAG = BaseballCardList.class.getName();

    @BindView(android.R.id.empty)
    TextView emptyList = null;
    @BindView(android.R.id.list)
    ListView listView;

    private BaseballCardAdapter adapter = null;
    private Bundle filterParams = null;
    private BaseballCardMultiChoiceModeListener mCallbacks;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_list, container, false);
        ButterKnife.bind(this, view);

        final Activity activity = Objects.requireNonNull(getActivity());
        View headerView = new HeaderView(this.getActivity());
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
        listView.addHeaderView(headerView);
        applyFilter(filterParams);

        return view;
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
            case R.id.add_menu:
                BaseballCardDetails details = new BaseballCardDetails();
                activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_holder, details, FragmentTags.EDIT_CARD)
                    .addToBackStack(FragmentTags.EDIT_CARD)
                    .commit();
                return true;
            case R.id.filter_menu:
                FilterCards filterCards = new FilterCards();
                activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_holder, filterCards, FragmentTags.FILTER_CARDS)
                    .addToBackStack(FragmentTags.FILTER_CARDS)
                    .commit();
                return true;
            case R.id.clear_filter_menu:
                this.emptyList.setText(R.string.start);
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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (position == 0) {
            return;
        }

        Fragment details = BaseballCardDetails.getInstance(id);
        FragmentActivity activity = Objects.requireNonNull(this.getActivity());
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_holder, details, FragmentTags.EDIT_CARD)
                .addToBackStack(FragmentTags.EDIT_CARD)
                .commit();
    }

    public void deleteSelectedCards() {
        final Activity activity = getActivity();
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
    }

    private void setAllChecked(boolean checked) {
        ListView listView = this.getListView();
        // Add 1 for the header view
        for (int i = 0; i < this.adapter.getCount() + 1; ++i) {
            listView.setItemChecked(i, checked);
        }
    }

    private void applyFilter(Bundle filterParams) {
        LiveData<List<BaseballCard>> cards;
        BaseballCardDatabase database =
            BaseballCardDatabase.getInstance(getActivity());
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
                Activity activity = getActivity();
                adapter = new BaseballCardAdapter(
                    activity, R.layout.baseball_card, cards);
                setListAdapter(adapter);
                adapter.setListFragment(BaseballCardList.this);

                mCallbacks = new BaseballCardMultiChoiceModeListener(BaseballCardList.this);
                listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
                listView.setMultiChoiceModeListener(mCallbacks);
                adapter.setActionModeCallback(mCallbacks);
            }
        });
    }
}
