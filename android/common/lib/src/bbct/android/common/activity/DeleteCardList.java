package bbct.android.common.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import bbct.android.common.R;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.exception.SQLHelperCreationException;
import bbct.android.common.provider.BaseballCardSQLHelper;
import bbct.android.common.provider.SQLHelperFactory;

/**
 * {@link DeleteCardList} enables users to mark and remove
 * the cards they desire to delete. Activity receives filter
 * information through an intent and then makes the appropriate
 * query to fetch information. User is then able to mark any
 * number of items from {@link ListView} and remove them.
 */
public class DeleteCardList extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.delete_card_list);

        String deleteTitle = this.getString(R.string.delete_cards_title);
        String title = this.getString(R.string.bbct_title, deleteTitle);
        this.setTitle(title);

        Intent intent = getIntent();
        int request = intent.getIntExtra(this.getString(R.string.filter_request_extra), BaseballCardList.DEFAULT_INT_EXTRA);
        Bundle params = intent.getBundleExtra(this.getString(R.string.filter_params_extra));

        try {
            this.sqlHelper = SQLHelperFactory.getSQLHelper(this);

            btnDelete = (Button) findViewById(R.id.delete_button);
            View headerView = View.inflate(this, R.layout.list_header_mark, null);
            this.getListView().addHeaderView(headerView);

            this.adapter = new SimpleCursorAdapter(this, R.layout.row_mark, null, this.sqlHelper.ROW_PROJECTION, this.sqlHelper.ROW_TEXT_VIEWS);
            this.setListAdapter(this.adapter);
            this.sqlHelper.applyFilter(this, request, params);
            this.swapCursor();

        } catch (SQLHelperCreationException ex) {
            // TODO Show a dialog and exit activity
            Toast.makeText(this, R.string.database_error, Toast.LENGTH_LONG).show();
            Log.e(TAG, ex.getMessage(), ex);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.sqlHelper.close();
    }

    /**
     * Count the number of checked items in list and enables/disables
     * the delete button. Button should be disabled if nothing is marked.
     */
    private void updateDeleteButton() {
        ListView lst = this.getListView();

        for (int i = 1; i < lst.getChildCount(); i++) {
            View v = lst.getChildAt(i);

            CheckedTextView ctv = (CheckedTextView) v.findViewById(R.id.checkmark);
            if (ctv.isChecked()) {
                this.btnDelete.setEnabled(true);
                return;
            }
        }

        this.btnDelete.setEnabled(false);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        CheckedTextView ctv = (CheckedTextView) v.findViewById(R.id.checkmark);

        // header item - mark all
        if (position == 0) {
            if (ctv.isChecked())
                setAll(false);
            else
                setAll(true);
        }

        else
            ctv.toggle();

        updateDeleteButton();
    }

    /**
     * Marks/unmarks all items in the delete list according to @param check
     */
    private void setAll(boolean check) {
        ListView lst = this.getListView();

        for (int i = 0; i < lst.getChildCount(); i++) {
            View v = lst.getChildAt(i);

            CheckedTextView ctv = (CheckedTextView) v.findViewById(R.id.checkmark);
            ctv.setChecked(check);
        }
    }

    public void cancelDelete(View v) {
        this.finish();
    }

    public void commitDelete(View v) {
        ListView lst = this.getListView();

        for (int i = 1; i < lst.getChildCount(); i++) {
            CheckedTextView ctv = (CheckedTextView) lst.getChildAt(i).findViewById(R.id.checkmark);

            if (ctv.isChecked()) {
                BaseballCard card = getBaseballCard(lst.getChildAt(i));
                this.sqlHelper.removeBaseballCard(card);
            }
        }

        Toast.makeText(v.getContext(), R.string.card_deleted_message, Toast.LENGTH_LONG).show();
        this.finish();
    }

    /**
     * Obtains {@link BaseballCard} from @param v
     * The returned {@link BaseballCard} only includes
     * partial data - data required to delete a card.
     */
    private BaseballCard getBaseballCard(View v) {
        TextView yearCol = (TextView) v.findViewById(R.id.year_text_view);
        int year = Integer.parseInt(yearCol.getText().toString());

        TextView numCol = (TextView) v.findViewById(R.id.number_text_view);
        int number = Integer.parseInt(numCol.getText().toString());

        TextView nameCol = (TextView) v.findViewById(R.id.player_name_text_view);
        String player = nameCol.getText().toString();

        return new BaseballCard("", year, number, 0, 0, player, "", "");
    }

    private void swapCursor() {
        Cursor cursor = this.sqlHelper.getCursor();
        this.stopManagingCursor(this.adapter.getCursor());
        this.startManagingCursor(cursor);
        this.adapter.changeCursor(cursor);
    }

    private static final String TAG = DeleteCardList.class.getName();
    private CursorAdapter adapter = null;
    private BaseballCardSQLHelper sqlHelper = null;
    private Button btnDelete = null;
}
