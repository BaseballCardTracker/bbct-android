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

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import bbct.android.common.R;
import bbct.android.common.activity.util.DialogUtil;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.provider.BaseballCardContract;
import bbct.android.common.provider.SingleColumnCursorAdapter;

/**
 * Allows user to add a new card or view and edit details of an existing card.
 */
public class BaseballCardDetails extends Fragment {

    private static final String ID = "id";
    private static final String CARD = "card";
    private static final String TAG = BaseballCardDetails.class.getName();

    private CheckBox autographCheckBox = null;
    private Spinner conditionSpinner = null;
    private AutoCompleteTextView brandText = null;
    private EditText yearText = null;
    private EditText numberText = null;
    private EditText valueText = null;
    private EditText countText = null;
    private AutoCompleteTextView playerNameText = null;
    private AutoCompleteTextView teamText = null;
    private Spinner playerPositionSpinner = null;
    private ArrayAdapter<CharSequence> conditionAdapter;
    private ArrayAdapter<CharSequence> positionsAdapter;
    private Uri uri = null;
    private boolean isUpdating = false;
    private long cardId = -1L;

    public static BaseballCardDetails getInstance(long id, BaseballCard card) {
        Bundle args = new Bundle();
        args.putLong(ID, id);
        args.putSerializable(CARD, card);

        BaseballCardDetails details = new BaseballCardDetails();
        details.setArguments(args);

        return details;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_details, container, false);

        String cardDetailsTitle = this.getString(R.string.card_details_title);
        String title = this.getString(R.string.bbct_title, cardDetailsTitle);
        this.getActivity().setTitle(title);

        this.autographCheckBox = (CheckBox) view.findViewById(R.id.autograph);

        this.conditionSpinner = this.populateSpinner(view, R.id.condition, R.array.condition);
        this.conditionAdapter = (ArrayAdapter<CharSequence>) this.conditionSpinner
                .getAdapter();

        this.brandText = (AutoCompleteTextView) view.findViewById(R.id.brand_text);
        CursorAdapter brandAdapter = new SingleColumnCursorAdapter(getActivity(),
                BaseballCardContract.BRAND_COL_NAME);
        this.brandText.setAdapter(brandAdapter);

        this.yearText = (EditText) view.findViewById(R.id.year_text);
        this.numberText = (EditText) view.findViewById(R.id.number_text);
        this.valueText = (EditText) view.findViewById(R.id.value_text);
        this.countText = (EditText) view.findViewById(R.id.count_text);

        this.playerNameText = (AutoCompleteTextView) view.findViewById(R.id.player_name_text);
        CursorAdapter playerNameAdapter = new SingleColumnCursorAdapter(this.getActivity(),
                BaseballCardContract.PLAYER_NAME_COL_NAME);
        this.playerNameText.setAdapter(playerNameAdapter);

        this.teamText = (AutoCompleteTextView) view.findViewById(R.id.team_text);
        CursorAdapter teamAdapter = new SingleColumnCursorAdapter(this.getActivity(),
                BaseballCardContract.TEAM_COL_NAME);
        this.teamText.setAdapter(teamAdapter);

        this.playerPositionSpinner = this.populateSpinner(view, R.id.player_position_text,
                R.array.positions);
        this.positionsAdapter = (ArrayAdapter<CharSequence>) this.playerPositionSpinner
                .getAdapter();

        Bundle args = this.getArguments();
        if (args != null) {
            long id = args.getLong(ID);
            BaseballCard card = (BaseballCard) args.getSerializable(CARD);
            this.setCard(id, card);
        }

        this.uri = BaseballCardContract.getUri(this.getActivity().getPackageName());

        return view;
    }

    private void setCard(long cardId, BaseballCard card) {
        this.isUpdating = true;
        this.cardId = cardId;
        this.autographCheckBox.setChecked(card.isAutographed());

        int selectedCondition = this.conditionAdapter.getPosition(card
                .getCondition());
        this.conditionSpinner.setSelection(selectedCondition);

        this.brandText.setText(card.getBrand());
        this.yearText.setText(Integer.toString(card.getYear()));
        this.numberText.setText(Integer.toString(card.getNumber()));
        this.valueText
                .setText(Double.toString(card.getValue() / 100.0));
        this.countText.setText(Integer.toString(card.getCount()));
        this.playerNameText.setText(card.getPlayerName());
        this.teamText.setText(card.getTeam());

        int selectedPosition = this.positionsAdapter.getPosition(card
                .getPlayerPosition());
        this.playerPositionSpinner.setSelection(selectedPosition);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();

        switch (menuId) {
            case R.id.save_menu:
                this.onSave();
                return true;

            case android.R.id.home:
                this.onHome();
                return true;
        }

        return false;
    }

    private void onHome() {
        Fragment list = BaseballCardList.getInstance(null);
        this.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_holder, list, FragmentTags.CARD_LIST)
                .addToBackStack(FragmentTags.CARD_LIST)
                .commit();
    }

    private Spinner populateSpinner(View view, int spinnerId, int araryId) {
        Spinner spinner = (Spinner) view.findViewById(spinnerId);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this.getActivity(), araryId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        return spinner;
    }

    private BaseballCard getBaseballCard() {
        Log.d(TAG, "getBaseballCard()");

        EditText[] allEditTexts = { this.brandText, this.yearText,
                this.numberText, this.valueText, this.countText,
                this.playerNameText, this.teamText };
        int[] errorIds = { R.string.brand_input_error,
                R.string.year_input_error, R.string.number_input_error,
                R.string.value_input_error, R.string.count_input_error,
                R.string.player_name_input_error, R.string.team_input_error };
        boolean validInput = true;

        boolean autographed = this.autographCheckBox.isChecked();
        String condition = (String) this.conditionSpinner.getSelectedItem();
        String playerPosition = (String) this.playerPositionSpinner
                .getSelectedItem();

        for (int i = allEditTexts.length - 1; i >= 0; --i) {
            String input = allEditTexts[i].getText().toString();
            if (input.equals("")) {
                allEditTexts[i].requestFocus();
                allEditTexts[i].setError(this.getString(errorIds[i]));
                validInput = false;
            }
        }

        if (validInput) {
            String brand = this.brandText.getText().toString();
            String yearStr = this.yearText.getText().toString();
            int year = Integer.parseInt(yearStr);
            String numberStr = this.numberText.getText().toString();
            int number = Integer.parseInt(numberStr);
            String valueStr = this.valueText.getText().toString();
            double value = Double.parseDouble(valueStr);
            String countStr = this.countText.getText().toString();
            int count = Integer.parseInt(countStr);
            String team = this.teamText.getText().toString();
            String playerName = this.playerNameText.getText().toString();
            return new BaseballCard(autographed, condition, brand, year,
                    number, (int) (value * 100), count, playerName, team,
                    playerPosition);
        } else {
            return null;
        }
    }

    /**
     *
     * Called when a key was released and not handled by any of the views inside
     * of the activity.
     *
     * @param keyCode
     *            The value in event.getKeyCode().
     * @param event
     *            Description of the key event.
     * @return {@code true} if the event was handled, {@code false} otherwise.
     */
//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        // If the key entered is 'Enter'('next' or 'done'), then
//        // 1) move the focus to the next view if the current focus is in brand
//        // or player name view and
//        // 2) hide the keypad if the current focus is in team view.
//        if (keyCode == KeyEvent.KEYCODE_ENTER) {
//            if (this.brandText.hasFocus()) {
//                this.yearText.requestFocus();
//                return true;
//            } else if (this.playerNameText.hasFocus()) {
//                this.teamText.requestFocus();
//                return true;
//            } else if (this.teamText.hasFocus()) {
//                // hide the soft keypad
//                InputMethodManager imm = (InputMethodManager) this.getActivity()
//                        .getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(this.teamText.getWindowToken(), 0);
//                return true;
//            }
//        }
//        return super.onKeyUp(keyCode, event);
//    }

    private void resetInput() {
        this.autographCheckBox.setChecked(false);
        this.brandText.setText("");
        this.yearText.setText("");
        this.numberText.setText("");
        this.valueText.setText("");
        this.countText.setText("");
        this.playerNameText.setText("");
        this.teamText.setText("");
        this.playerPositionSpinner.setSelection(-1);
    }

    private void onSave() {
        ContentResolver resolver = this.getActivity().getContentResolver();
        BaseballCard newCard = this.getBaseballCard();

        if (newCard != null) {
            if (this.isUpdating) {
                Uri uri = ContentUris.withAppendedId(this.uri, this.cardId);
                resolver.update(uri,
                        BaseballCardContract.getContentValues(newCard), null,
                        null);
            } else {
                try {
                    ContentValues values = BaseballCardContract
                            .getContentValues(newCard);
                    resolver.insert(this.uri, values);

                    this.resetInput();
                    this.brandText.requestFocus();
                    Toast.makeText(this.getActivity(), R.string.card_added_message,
                            Toast.LENGTH_LONG).show();
                } catch (SQLException e) {
                    // Is duplicate card the only reason this exception
                    // will be thrown?
                    DialogUtil.showErrorDialog(this.getActivity(),
                            R.string.duplicate_card_title,
                            R.string.duplicate_card_error);
                }
            }
        }
    }

}
