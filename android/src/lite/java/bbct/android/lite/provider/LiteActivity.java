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
package bbct.android.lite.provider;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amazon.device.ads.AdError;
import com.amazon.device.ads.AdLayout;
import com.amazon.device.ads.AdListener;
import com.amazon.device.ads.AdProperties;
import com.amazon.device.ads.AdRegistration;
import com.amazon.device.ads.AdTargetingOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import bbct.android.common.BuildConfig;
import bbct.android.common.R;
import bbct.android.common.activity.MainActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class LiteActivity extends MainActivity implements AdListener {

    private static final String AD_UNIT_ID = "a151047824e2718";

    private static final String APP_KEY = "42d5980d355a49afb22ea8c6618591d8";

    @BindView(R.id.ad_view) ViewGroup adViewContainer;
    @BindView(R.id.premium_text) TextView premiumText;

    private AdLayout amazonAdView;
    private AdView admobAdView;
    private boolean amazonAdEnabled;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        AdRegistration.setAppKey(APP_KEY);

        // Initialize ad views
        amazonAdView = new AdLayout(this, com.amazon.device.ads.AdSize.SIZE_320x50);
        amazonAdView.setListener(this);
        admobAdView = new AdView(this);
        admobAdView.setAdUnitId(AD_UNIT_ID);
        admobAdView.setAdSize(AdSize.BANNER);

        // Initialize view container
        amazonAdEnabled = true;
        adViewContainer.addView(amazonAdView);

        amazonAdView.loadAd(new AdTargetingOptions());

        premiumText.setMovementMethod(LinkMovementMethod.getInstance());

        if (BuildConfig.DEBUG) {
            AdRegistration.enableTesting(true);
            AdRegistration.enableLogging(true);
        }
    }

    /**
     * Destroy all fragments and loaders.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.amazonAdView.destroy();
    }

    @Override
    public void onAdLoaded(AdLayout adLayout, AdProperties adProperties) {
        if (!amazonAdEnabled) {
            amazonAdEnabled = true;
            adViewContainer.removeView(admobAdView);
            adViewContainer.addView(amazonAdView);
        }
    }

    @Override
    public void onAdExpanded(AdLayout adLayout) {

    }

    @Override
    public void onAdCollapsed(AdLayout adLayout) {

    }

    @Override
    public void onAdFailedToLoad(AdLayout adLayout, AdError adError) {
        // Call AdMob SDK for backfill
        if (amazonAdEnabled) {
            amazonAdEnabled = false;
            adViewContainer.removeView(amazonAdView);
            adViewContainer.addView(admobAdView);
        }

        AdRequest request = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        admobAdView.loadAd(request);
    }
}
