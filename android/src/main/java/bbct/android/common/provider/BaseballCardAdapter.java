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
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import bbct.android.common.database.BaseballCard;
import bbct.android.common.view.BaseballCardView;

public class BaseballCardAdapter extends RecyclerView.Adapter<BaseballCardAdapter.BaseballCardViewHolder> {
    private List<BaseballCard> cards;

    public class BaseballCardViewHolder extends RecyclerView.ViewHolder {
        public BaseballCardView view;

        BaseballCardViewHolder(BaseballCardView view) {
            super(view);
            this.view = view;
        }
    }

    public BaseballCardAdapter(List<BaseballCard> cards) {
        this.cards = cards;
    }

    @NonNull
    @Override
    public BaseballCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseballCardView view = new BaseballCardView(parent.getContext());
        return new BaseballCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseballCardViewHolder holder, int position) {
        holder.view.setBaseballCard(cards.get(position));
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }
}
