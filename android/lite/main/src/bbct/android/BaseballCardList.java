/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2012 codeguru <codeguru@users.sourceforge.net>
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
package bbct.android;

import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import java.util.Arrays;
import java.util.HashSet;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardList extends bbct.android.common.BaseballCardList {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AdView adView = (AdView) this.findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest();

        if (Build.PRODUCT.contains("sdk")) {
            adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
        }
        
        String[] keywords = this.getResources().getStringArray(R.array.keywords);
        adRequest.addKeywords(new HashSet<String>(Arrays.asList(keywords)));

        adView.loadAd(adRequest);

        TextView premiumText = (TextView) this.findViewById(R.id.premium_text);
        premiumText.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
