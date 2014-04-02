package bbct.android.common.activity.callback;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import bbct.android.common.activity.BaseballCardList;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.R;

/**
 * handles the creation of a contextual action menu for the
 * {@link BaseballCardList} activity
 * 
 * @author theoklitos
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public final class BaseballCardListActionCallback implements Callback {

	private BaseballCardList activity;

	public BaseballCardListActionCallback(final BaseballCardList activity) {
		this.activity = activity;
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		MenuInflater inflater = mode.getMenuInflater();
		inflater.inflate(R.menu.card_list_context_menu, menu);
		return true;
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		// nothing required here
		return true;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		final BaseballCard selectedCard = activity.getSqlHelper()
				.getBaseballCardFromCursor();
		if(selectedCard == null) {
			return false;
		} else {
			mode.setTitle("Card #" + selectedCard.getNumber());
		}
		if (item.getItemId() == R.id.delete_card) {
			activity.getSqlHelper().removeBaseballCard(selectedCard);
			Toast.makeText(activity, R.string.card_deleted_message,
					Toast.LENGTH_LONG).show();
			activity.updateFilter();
			activity.swapCursor();
		} else if (item.getItemId() == R.id.edit_card) {
			activity.createIntentAndStartEditCardActivity(selectedCard);
		}
		mode.finish();
		return true;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		// nothing here too
	}

}
