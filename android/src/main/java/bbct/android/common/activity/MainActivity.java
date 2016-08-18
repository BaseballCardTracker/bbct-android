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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import bbct.android.common.BuildConfig;
import bbct.android.common.R;
import bbct.android.common.provider.BaseballCardContract;
import com.crashlytics.android.Crashlytics;
import com.google.analytics.tracking.android.EasyTracker;
import io.fabric.sdk.android.Fabric;
import java.net.URISyntaxException;

public class MainActivity extends ActionBarActivity {
    public static final String PREFS = "bbct.prefs";
    public static final String SURVEY_TAKEN_PREF = "survey";

    private static final String TAG = MainActivity.class.getName();

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

        showSurveyDialog();
    }

    private void showSurveyDialog() {
        final SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);

        if (!prefs.getBoolean(SURVEY_TAKEN_PREF, false)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.survey);
            builder.setPositiveButton(R.string.now, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(SURVEY_TAKEN_PREF, true);
                    editor.apply();

                    Intent surveyIntent = null;
                    try {
                        surveyIntent = Intent.parseUri(getString(R.string.survey_uri), 0);
                    } catch (URISyntaxException e) {
                        Log.e(TAG, "Error parsing URI for survey", e);
                    }
                    startActivity(surveyIntent);
                }
            });
            builder.setNegativeButton(R.string.later, null);
            builder.create().show();
        }
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
