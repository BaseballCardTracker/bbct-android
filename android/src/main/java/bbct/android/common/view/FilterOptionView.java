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

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import bbct.android.common.R;

public class FilterOptionView extends CheckableLinearLayout {

    private TextView label;
    private EditText edit;

    public FilterOptionView(Context context) {
        this(context, null);
    }

    public FilterOptionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilterOptionView(final Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        label = new TextView(context);
        edit = new EditText(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FilterOptionView,
                0, 0);
        int[] set = {android.R.attr.enabled, android.R.attr.inputType, android.R.attr.singleLine};
        TypedArray b = context.getTheme().obtainStyledAttributes(attrs, set, 0, 0);

        try {
            boolean enabled = a.getBoolean(0, true);
            mCheckBox.setChecked(enabled);
            label.setEnabled(enabled);
            edit.setEnabled(enabled);

            int labelWeight = a.getInt(R.styleable.FilterOptionView_labelWeight, 0);
            ViewGroup.LayoutParams labelParams = new LinearLayout.LayoutParams(
                    0, LayoutParams.WRAP_CONTENT, labelWeight);
            label.setLayoutParams(labelParams);

            int editWeight = a.getInt(R.styleable.FilterOptionView_editWeight, 0);
            ViewGroup.LayoutParams editParams = new LinearLayout.LayoutParams(
                    0, LayoutParams.WRAP_CONTENT, editWeight);
            edit.setLayoutParams(editParams);

            label.setText(a.getString(R.styleable.FilterOptionView_label));
            label.setTextSize(a.getDimension(R.styleable.FilterOptionView_labelTextSize, 1.0f));
            edit.setHint(a.getString(R.styleable.FilterOptionView_hint));
            edit.setInputType(b.getInt(1, 0));
            edit.setSingleLine(b.getBoolean(2, true));
        } finally {
            a.recycle();
        }

        this.addView(label);
        this.addView(edit);

        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                label.setEnabled(isChecked);
                edit.setEnabled(isChecked);
                edit.requestFocus();

                // TODO: Should not be the responsibility of FilterOptionView
                if (context instanceof AppCompatActivity) {
                    ((AppCompatActivity) context).supportInvalidateOptionsMenu();
                }
            }
        });
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);

        label.setEnabled(checked);
        edit.setEnabled(checked);
        edit.requestFocus();
    }

    public Editable getText() {
        return edit.getText();
    }

    public EditText getEditText() {
        return edit;
    }

}
