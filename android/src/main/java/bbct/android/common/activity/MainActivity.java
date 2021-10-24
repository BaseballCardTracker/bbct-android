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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import bbct.android.common.R;
import bbct.android.common.SharedPreferenceKeys;
import bbct.android.common.activity.util.DialogUtil;
import bbct.android.common.fragment.About;
import bbct.android.common.fragment.BaseballCardList;
import bbct.android.common.fragment.FragmentTags;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {
    public static final int SURVEY_DELAY = 7;
    public static final String SURVEY1_URI = "https://docs.google.com/forms/d/1wj3d3SiZ7U81_ZRp0zwgH0l2b2Az3A9XkYJbgQFdO9I/viewform";
    public static final String SURVEY2_URI = "https://docs.google.com/forms/d/e/1FAIpQLSfg0TPyKcWlGSOlhhDd_4qIjYG9htOjJ5pwjRYtc71zxPw-ag/viewform";

    private static final String TAG = MainActivity.class.getName();

    private SharedPreferences prefs;
    private FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                .add(R.id.fragment_holder, new BaseballCardList(), FragmentTags.CARD_LIST)
                .addToBackStack(FragmentTags.CARD_LIST)
                .commit();

            fragmentManager.addOnBackStackChangedListener(this);
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
                    DialogUtil.showSurveyDialog(this, todayStr, R.string.survey1,
                        SharedPreferenceKeys.SURVEY1_DATE, SURVEY1_URI);
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
                    DialogUtil.showSurveyDialog(this, todayStr, R.string.survey2,
                        SharedPreferenceKeys.SURVEY2_DATE, SURVEY2_URI);
                }
            } catch (ParseException e) {
                Log.d(TAG, "Error parsing install date");
                e.printStackTrace();
            }
        } else if (prefs.contains(SharedPreferenceKeys.SURVEY_TAKEN_PREF)) {
            prefs.edit().putString(SharedPreferenceKeys.SURVEY1_DATE, todayStr).apply();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        switch (menuId) {
            case R.id.about_menu:
                fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_holder, new About(), FragmentTags.ABOUT)
                    .addToBackStack(FragmentTags.ABOUT)
                    .commit();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.updateUpButtonVisibility();
    }

    @Override
    public void onBackStackChanged() {
        this.updateUpButtonVisibility();
    }

    private void updateUpButtonVisibility() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(fragmentManager.getBackStackEntryCount() > 1);
        }
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = fragmentManager
            .findFragmentById(R.id.fragment_holder);
        if (currentFragment instanceof About) {
            fragmentManager.popBackStack(FragmentTags.CARD_LIST, 0);
        } else {
            super.onBackPressed();
        }
    }
}
