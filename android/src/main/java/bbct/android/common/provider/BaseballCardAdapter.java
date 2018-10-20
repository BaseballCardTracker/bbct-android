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
package bbct.android.common.provider;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardDetails;
import bbct.android.common.activity.FragmentTags;
import bbct.android.common.database.BaseballCard;
import bbct.android.common.view.BaseballCardView;
import bbct.android.common.view.HeaderView;

public class BaseballCardAdapter extends RecyclerView.Adapter<BaseballCardAdapter.BaseballCardViewHolder> {
    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;

    private FragmentActivity activity;
    private List<BaseballCard> cards;

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

    public BaseballCardAdapter(@NonNull FragmentActivity activity) {
        this.activity = activity;
        this.cards = new ArrayList<>();
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
    public void onBindViewHolder(@NonNull BaseballCardViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        switch (viewType) {
            case TYPE_HEADER:
                break;

            case TYPE_ITEM:
                holder.id = getItemId(position);
                BaseballCardView view = (BaseballCardView) holder.view;
                view.setBaseballCard(cards.get(position - 1));
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
        notifyDataSetChanged();
    }
}
