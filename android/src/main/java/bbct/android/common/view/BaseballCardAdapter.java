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
package bbct.android.common.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardDetails;
import bbct.android.common.activity.FragmentTags;
import bbct.android.common.database.BaseballCard;

public class BaseballCardAdapter extends RecyclerView.Adapter<BaseballCardAdapter.BaseballCardViewHolder> {
    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;

    private FragmentActivity activity;
    private List<BaseballCard> cards = new ArrayList<>();
    private List<Boolean> selected = new ArrayList<>();
    private BaseballCardActionModeCallback callback;

    public class BaseballCardViewHolder<T extends View>
        extends RecyclerView.ViewHolder
        implements View.OnClickListener {
        public long id = -1;
        public T view;

        BaseballCardViewHolder(T view) {
            super(view);
            this.view = view;
            this.view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Fragment details = BaseballCardDetails.getInstance(id);
            activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_holder, details, FragmentTags.EDIT_CARD)
                .addToBackStack(FragmentTags.EDIT_CARD)
                .commit();
        }
    }

    public BaseballCardAdapter(@NonNull FragmentActivity activity,
                               @NonNull BaseballCardActionModeCallback callback) {
        this.activity = activity;
        this.callback = callback;
    }

    @NonNull
    @Override
    public BaseballCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                HeaderView headerView = new HeaderView(parent.getContext());
                return new BaseballCardViewHolder<>(headerView);

            case TYPE_ITEM:
                BaseballCardView view = new BaseballCardView(parent.getContext());
                return new BaseballCardViewHolder<>(view);
        }

        return new BaseballCardViewHolder<View>(null);
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseballCardViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        switch (viewType) {
            case TYPE_HEADER:
                HeaderView headerView = (HeaderView) holder.view;
                CheckBox selectAll = headerView.findViewById(R.id.select_all);
                selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked && !callback.isStarted()) {
                            activity.startActionMode(callback);
                        } else if (callback.isStarted()) {
                            callback.finish();
                        }
                        setAllSelected(isChecked);
                        notifyDataSetChanged();
                    }
                });
                break;

            case TYPE_ITEM:
                holder.id = getItemId(position);
                BaseballCardView cardView = (BaseballCardView) holder.view;
                // Subtract 1 to account for header row
                cardView.setBaseballCard(cards.get(position - 1));
                cardView.setChecked(selected.get(position - 1));

                CheckBox ctv = cardView.findViewById(R.id.checkmark);
                ctv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int position = holder.getAdapterPosition();
                        // Subtract 1 to account for header row
                        setItemSelected(position - 1, isChecked);

                        if (isChecked && !callback.isStarted()) {
                            activity.startActionMode(callback);
                        } else if (callback.isStarted() && getSelectedCount() == 0) {
                            callback.finish();
                        }
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return cards.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }

        return TYPE_ITEM;
    }

    @Override
    public long getItemId(int position) {
        return cards.get(position - 1)._id;
    }

    public void setCards(@NonNull List<BaseballCard> cards) {
        this.cards = cards;
        this.selected =
                new ArrayList<>(Collections.nCopies(cards.size(), false));
        notifyDataSetChanged();
    }

    private void setAllSelected(boolean isSelected) {
        // Add 1 for the header view
        for (int i = 0; i < getItemCount() - 1; ++i) {
            setItemSelected(i, isSelected);
        }
    }

    private void setItemSelected(int position, boolean isSelected) {
        selected.set(position, isSelected);
    }

    public List<BaseballCard> getSelectedItems() {
        List<BaseballCard> selectedCards = new ArrayList<>();
        for (int i = 0; i < selected.size(); ++i) {
            if (selected.get(i)) {
                selectedCards.add(cards.get(i));
            }
        }

        return selectedCards;
    }

    public int getSelectedCount() {
        int count = 0;
        for (boolean selectedItem : selected) {
            if (selectedItem) {
                count++;
            }
        }
        return count;
    }
}
