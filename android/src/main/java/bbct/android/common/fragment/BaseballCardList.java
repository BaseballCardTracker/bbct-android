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
package bbct.android.common.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import bbct.android.common.R;
import bbct.android.common.database.BaseballCard;
import bbct.android.common.database.BaseballCardDao;
import bbct.android.common.database.BaseballCardDatabase;
import bbct.android.common.view.BaseballCardActionModeCallback;
import bbct.android.common.view.BaseballCardAdapter;
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
    private final BaseballCardActionModeCallback callback =
        new BaseballCardActionModeCallback(this);

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
        Activity activity = requireActivity();
        activity.setTitle(R.string.app_name);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_list, container, false);
        ButterKnife.bind(this, view);
        setUpRecyclerView();

        final FragmentActivity activity = requireActivity();
        addButton.setOnClickListener(v -> {
            BaseballCardDetails details = new BaseballCardDetails();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_holder, details, FragmentTags.EDIT_CARD)
                    .addToBackStack(FragmentTags.EDIT_CARD)
                    .commit();
        });

        applyFilter(filterParams);

        return view;
    }

    private void setUpRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        cardList.setLayoutManager(layoutManager);
        FragmentActivity activity = requireActivity();
        adapter = new BaseballCardAdapter(activity, callback);
        cardList.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
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

        FragmentActivity activity = requireActivity();
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

    public void deleteSelectedCards() {
        final Activity activity = requireActivity();
        final List<BaseballCard> cards = adapter.getSelectedItems();

        new Thread(() -> {
            BaseballCardDatabase database =
                BaseballCardDatabase.getInstance(activity);
            database.getBaseballCardDao().deleteBaseballCards(cards);
        }).start();

        Toast.makeText(
            activity,
            R.string.card_deleted_message,
            Toast.LENGTH_LONG
        ).show();
    }

    private void applyFilter(Bundle filterParams) {
        LiveData<List<BaseballCard>> cards;
        final Activity activity = requireActivity();
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
                Integer.parseInt(year),
                String.format(format, number),
                String.format(format, playerName),
                String.format(format, team)
            );
        }

        cards.observe(getViewLifecycleOwner(), cards1 -> adapter.setCards(Objects.requireNonNull(cards1)));
    }

    public void setAllSelected(boolean isSelected) {
        adapter.setAllSelected(isSelected);
    }
}
