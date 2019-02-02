/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2017 codeguru <codeguru@users.sourceforge.net>
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
package bbct.android.common.test.rule;

import androidx.fragment.app.Fragment;
import androidx.test.rule.ActivityTestRule;

import bbct.android.common.activity.FragmentTestActivity;

public class SupportFragmentTestRule extends ActivityTestRule<FragmentTestActivity> {
    private final Fragment fragment;

    public SupportFragmentTestRule(Fragment fragment) {
        super(FragmentTestActivity.class);
        this.fragment = fragment;
    }

    public Fragment getFragment() {
        return fragment;
    }

    @Override
    protected void afterActivityLaunched() {
        super.afterActivityLaunched();

        getActivity().replaceFragment(fragment);
    }
}
