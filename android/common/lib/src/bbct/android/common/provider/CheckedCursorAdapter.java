package bbct.android.common.provider;

import bbct.android.common.R;

/**
 * This class adds click listeners to {@link CheckedTextView} in
 * {@link SimpleCursorAdapter}. It enables {@link CheckedTextView}
 * to toggle its' state.
 * 
 * @author tmatek
 *
 */
public class CheckedCursorAdapter extends SimpleCursorAdapter {

    public CheckedCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
    }

    /**
     * Binds data from {@link Cursor} to the appropriate {@link View}.
     * Also adds {@link OnClickListener} to {@link CheckedTextView}.
     * 
     * @see {@link SimpleCursorAdapter#bindView}
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

        // set listeners for CheckedTextView
        final Cursor c = cursor;
        final CheckedTextView ctv = (CheckedTextView) view.findViewById(R.id.checkmark);
        final Activity curActivity = (Activity) context;
        ctv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                CheckedTextView cview = (CheckedTextView) v.findViewById(R.id.checkmark);
                cview.toggle();

                // update menu in the correct Activity
                curActivity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        curActivity.invalidateOptionsMenu();
                    }

                });

            }

        });

    }

}
