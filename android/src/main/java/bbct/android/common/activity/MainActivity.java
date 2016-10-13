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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import bbct.android.common.BuildConfig;
import bbct.android.common.R;
import bbct.android.common.SharedPreferenceKeys;
import bbct.android.common.provider.BaseballCardContract;
import com.crashlytics.android.Crashlytics;
import com.google.analytics.tracking.android.EasyTracker;
import io.fabric.sdk.android.Fabric;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static final int SURVEY_DELAY = 7;

    private static final String TAG = MainActivity.class.getName();

    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }

        this.setContentView(R.layout.main);

        if (savedInstanceState == null) {
            Uri uri = BaseballCardContract.getUri(this.getPackageName());
            Cursor cursor = this.getContentResolver().query(uri,
                    BaseballCardContract.PROJECTION, null, null, null);

            FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
            if (cursor == null || cursor.getCount() == 0) {
                ft.add(R.id.fragment_holder, new BaseballCardDetails(), FragmentTags.EDIT_CARD);
            } else {
                ft.add(R.id.fragment_holder, new BaseballCardList(), FragmentTags.CARD_LIST);
                cursor.close();
            }
            ft.commit();

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        prefs = getSharedPreferences(SharedPreferenceKeys.PREFS, MODE_PRIVATE);
        showSurvey1Dialog();
        showSurvey2Dialog();
    }

    private void showSurvey1Dialog() {
        DateFormat dateFormat = DateFormat.getDateInstance();
        Date today = new Date();
        final String todayStr = dateFormat.format(today);

        if (!prefs.contains(SharedPreferenceKeys.INSTALL_DATE)) {
            prefs.edit().putString(SharedPreferenceKeys.INSTALL_DATE, todayStr).apply();
        }

        if (!prefs.contains(SharedPreferenceKeys.SURVEY1_DATE)) {
            String installDate = prefs.getString(SharedPreferenceKeys.INSTALL_DATE, today.toString());

            try {
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateFormat.parse(installDate));
                cal.add(Calendar.DATE, SURVEY_DELAY);
                if (today.after(cal.getTime())) {
                    showSurveyDialog(this, todayStr, R.string.survey1, SharedPreferenceKeys.SURVEY1_DATE,
                            R.string.survey1_uri);
                }
            } catch (ParseException e) {
                Log.d(TAG, "Error parsing install date");
                e.printStackTrace();
            }
        }
    }

    private void showSurvey2Dialog() {
        DateFormat dateFormat = DateFormat.getDateInstance();
        Date today = new Date();
        final String todayStr = dateFormat.format(today);

        if (prefs.contains(SharedPreferenceKeys.SURVEY1_DATE)
                && !prefs.contains(SharedPreferenceKeys.SURVEY2_DATE)) {
            String survey1Date = prefs.getString(SharedPreferenceKeys.SURVEY1_DATE, today.toString());

            try {
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateFormat.parse(survey1Date));
                cal.add(Calendar.DATE, SURVEY_DELAY);
                if (today.after(cal.getTime())) {
                    showSurveyDialog(this, todayStr, R.string.survey2, SharedPreferenceKeys.SURVEY2_DATE,
                            R.string.survey2_uri);
                }
            } catch (ParseException e) {
                Log.d(TAG, "Error parsing install date");
                e.printStackTrace();
            }
        } else if (prefs.contains(SharedPreferenceKeys.SURVEY_TAKEN_PREF)) {
            prefs.edit().putString(SharedPreferenceKeys.SURVEY1_DATE, todayStr).apply();
        }
    }

    private static void showSurveyDialog(final Context context, final String todayStr, int surveyMessage,
                                         final String surveyDateKey, final int surveyUri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(surveyMessage);
        builder.setPositiveButton(R.string.now, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences prefs = context.getSharedPreferences(SharedPreferenceKeys.PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(surveyDateKey, todayStr);
                editor.apply();

                Intent surveyIntent = null;
                try {
                    surveyIntent = Intent.parseUri(context.getString(surveyUri), 0);
                } catch (URISyntaxException e) {
                    Log.e(TAG, "Error parsing URI for survey1", e);
                }
                context.startActivity(surveyIntent);
            }
        });
        builder.setNegativeButton(R.string.later, null);
        builder.show();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!BuildConfig.DEBUG) {
            EasyTracker.getInstance(this).activityStart(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!BuildConfig.DEBUG) {
            EasyTracker.getInstance(this).activityStop(this);
        }
    }

    /**
     * Create the options menu. This is simply inflated from the
     * {@code main.xml} resource file.
     *
     * @param menu The options menu in which new menu items are placed.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Respond to the user selecting a menu item.
     *
     * @param item The menu item selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.about_menu) {
            this.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_holder, new About(), FragmentTags.ABOUT)
                    .addToBackStack(FragmentTags.ABOUT)
                    .commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
