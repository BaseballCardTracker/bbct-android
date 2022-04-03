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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import bbct.android.common.R;
import bbct.android.common.activity.util.DialogUtil;
import bbct.android.common.api.BbctApi;
import bbct.android.common.api.RetrofitClient;
import bbct.android.common.database.BaseballCard;
import bbct.android.common.database.BaseballCardDao;
import bbct.android.common.database.BaseballCardDatabase;
import bbct.android.common.database.InsertCardTask;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseballCardDetails extends Fragment {
    private static final String ID = "id";
    private static final String TAG = BaseballCardDetails.class.getName();

    @BindView(R.id.autograph)
    CheckBox autographCheckBox = null;
    @BindView(R.id.condition)
    Spinner conditionSpinner = null;
    @BindView(R.id.brand_text)
    AutoCompleteTextView brandText = null;
    @BindView(R.id.year_text)
    EditText yearText = null;
    @BindView(R.id.number_text)
    EditText numberText = null;
    @BindView(R.id.value_text)
    EditText valueText = null;
    @BindView(R.id.count_text)
    EditText countText = null;
    @BindView(R.id.player_name_text)
    AutoCompleteTextView playerNameText = null;
    @BindView(R.id.team_text)
    AutoCompleteTextView teamText = null;
    @BindView(R.id.player_position_text)
    Spinner playerPositionSpinner = null;
    @BindView(R.id.save_button)
    FloatingActionButton saveButton = null;

    private ArrayAdapter<CharSequence> conditionAdapter;
    private ArrayAdapter<CharSequence> positionsAdapter;
    private boolean isUpdating = false;
    private long id;

    public static BaseballCardDetails getInstance(long id) {
        Bundle args = new Bundle();
        args.putLong(ID, id);
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
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_details, container, false);
        ButterKnife.bind(this, view);
        final Activity activity = Objects.requireNonNull(getActivity());
        String cardDetailsTitle = this.getString(R.string.card_details_title);
        String title = this.getString(R.string.bbct_title, cardDetailsTitle);
        activity.setTitle(title);

        saveButton.setOnClickListener(v -> onSave());

        brandText.setOnKeyListener((v, keyCode, event) -> {
            Log.d(TAG, "onKey() in Brand TextView");
            Log.d(TAG, "keyCode = " + keyCode);

            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                Log.d(TAG, "focus on Year");
                yearText.requestFocus();
                return true;
            }

            return false;
        });

        playerNameText.setOnKeyListener((v, keyCode, event) -> {
            Log.d(TAG, "onKey() in Player Name TextView");
            Log.d(TAG, "keyCode = " + keyCode);

            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                Log.d(TAG, "focus on Team");
                teamText.requestFocus();
                return true;
            }

            return false;
        });

        teamText.setOnKeyListener((v, keyCode, event) -> {
            Log.d(TAG, "onKey() in Team TextView");
            Log.d(TAG, "keyCode = " + keyCode);

            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                Log.d(TAG, "hide keyboard");
                InputMethodManager imm = Objects.requireNonNull(
                    (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE));
                imm.hideSoftInputFromWindow(teamText.getWindowToken(), 0);
                playerPositionSpinner.requestFocus();
                return true;
            }

            return false;
        });

        createAdapters(activity);
        populateTextEdits();

        return view;
    }

    private void createAdapters(final Activity activity) {
        this.conditionAdapter = this.populateSpinnerAdapter(R.array.condition);
        this.conditionSpinner.setAdapter(this.conditionAdapter);

        final ArrayAdapter<String> brandAdapter = new ArrayAdapter<>(
                activity,
                android.R.layout.simple_list_item_1
        );
        this.brandText.setAdapter(brandAdapter);

        final ArrayAdapter<String> playerNameAdapter = new ArrayAdapter<>(
                activity,
                android.R.layout.simple_list_item_1
        );
        this.playerNameText.setAdapter(playerNameAdapter);

        final ArrayAdapter<String> teamAdapter = new ArrayAdapter<>(
            activity,
            android.R.layout.simple_list_item_1
        );
        this.teamText.setAdapter(teamAdapter);

        BaseballCardDatabase database =
            BaseballCardDatabase.getInstance(activity);
        BaseballCardDao dao = database.getBaseballCardDao();
        LiveData<List<String>> brands = dao.getBrands();
        brands.observe(
            BaseballCardDetails.this,
            new ListObserver(brandAdapter)
        );

        LiveData<List<String>> playerNames = dao.getPlayerNames();
        playerNames.observe(
            BaseballCardDetails.this,
            new ListObserver(playerNameAdapter)
        );

        LiveData<List<String>> teams = dao.getTeams();
        teams.observe(
            BaseballCardDetails.this,
            new ListObserver(teamAdapter)
        );

        this.positionsAdapter = this.populateSpinnerAdapter(R.array.positions);
        this.playerPositionSpinner.setAdapter(this.positionsAdapter);
    }

    @SuppressLint("StaticFieldLeak")
    private void populateTextEdits() {
        Bundle args = getArguments();
        if (args != null) {
            id = args.getLong(ID);
            new AsyncTask<Long, Void, BaseballCard>() {
                @Override
                protected BaseballCard doInBackground(Long... args) {
                    long id = args[0];
                    Activity activity = Objects.requireNonNull(getActivity());
                    BaseballCardDatabase database =
                        BaseballCardDatabase.getInstance(activity);
                    BaseballCardDao dao = database.getBaseballCardDao();
                    return dao.getBaseballCard(id);
                }

                @Override
                protected void onPostExecute(BaseballCard card) {
                    setCard(card);
                }
            }.execute(id);
        }
    }

    private void setCard(BaseballCard card) {
        this.isUpdating = true;
        this.autographCheckBox.setChecked(card.autographed);

        int selectedCondition = this.conditionAdapter.getPosition(card.condition);
        this.conditionSpinner.setSelection(selectedCondition);

        this.brandText.setText(card.brand);
        this.yearText.setText(String.format(Locale.getDefault(), "%d", card.year));
        this.numberText.setText(card.number);
        this.valueText.setText(String.format(Locale.getDefault(), "%.2f", card.value / 100.0));
        this.countText.setText(String.format(Locale.getDefault(), "%d", card.quantity));
        this.playerNameText.setText(card.playerName);
        this.teamText.setText(card.team);

        int selectedPosition = this.positionsAdapter.getPosition(card.position);
        this.playerPositionSpinner.setSelection(selectedPosition);
    }

    private ArrayAdapter<CharSequence> populateSpinnerAdapter(int arrayId) {
        Activity activity = Objects.requireNonNull(getActivity());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
            activity, arrayId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return adapter;
    }

    private BaseballCard getBaseballCard() {
        Log.d(TAG, "getBaseballCard()");

        EditText[] allEditTexts = {this.brandText, this.yearText,
                this.numberText, this.valueText, this.countText,
                this.playerNameText, this.teamText};
        int[] errorIds = {R.string.brand_input_error,
                R.string.year_input_error, R.string.number_input_error,
                R.string.value_input_error, R.string.count_input_error,
                R.string.player_name_input_error, R.string.team_input_error};
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
            String number = this.numberText.getText().toString();
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
        final BaseballCard newCard = this.getBaseballCard();
        Activity activity = Objects.requireNonNull(getActivity());
        BaseballCardDatabase database = BaseballCardDatabase.getInstance(activity);
        final BaseballCardDao dao = database.getBaseballCardDao();

        if (newCard != null) {
            if (this.isUpdating) {
                newCard._id = id;
                new Thread() {
                    @Override
                    public void run() {
                        dao.updateBaseballCard(newCard);
                        FragmentActivity activity = Objects.requireNonNull(getActivity());
                        activity.getSupportFragmentManager().popBackStack();
                    }
                }.start();
            } else {
                new InsertCardTask(this, dao).execute(newCard);

                postCard(newCard);
            }
        }
    }

    private void postCard(BaseballCard card) {
        BbctApi service = RetrofitClient.getInstance().create(BbctApi.class);
        Call<bbct.android.common.api.BaseballCard> call = service.createBaseballCard(new bbct.android.common.api.BaseballCard(card));
        call.enqueue(new Callback<bbct.android.common.api.BaseballCard>() {
            @Override
            public void onResponse(Call<bbct.android.common.api.BaseballCard> call, Response<bbct.android.common.api.BaseballCard> response) {
                Log.d(TAG, response.body().toString());
            }

            @Override
            public void onFailure(Call<bbct.android.common.api.BaseballCard> call, Throwable t) {
                Toast.makeText(requireActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void insertSuccessful() {
        Activity activity = Objects.requireNonNull(getActivity());
        this.resetInput();
        this.brandText.requestFocus();
        Toast.makeText(activity, R.string.card_added_message,
            Toast.LENGTH_LONG).show();
    }

    public void insertFailed() {
        Activity activity = Objects.requireNonNull(getActivity());
        // Is duplicate card the only reason this exception
        // will be thrown?
        DialogUtil.showErrorDialog(activity,
            R.string.duplicate_card_title,
            R.string.duplicate_card_error);
    }
}

class ListObserver implements Observer<List<String>> {
    private final ArrayAdapter<String> adapter;

    ListObserver(ArrayAdapter<String> adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onChanged(@Nullable List<String> strings) {
        if (strings != null) {
            adapter.clear();
            adapter.addAll(strings);
            adapter.notifyDataSetChanged();
        }
    }
}
