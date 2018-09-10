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
package bbct.android.common.activity.test;

import android.app.Activity;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import bbct.android.common.activity.About;
import bbct.android.common.test.rule.SupportFragmentTestRule;

// TODO: Add tests for the layout of {@link About}
@RunWith(AndroidJUnit4.class)
public class AboutTest {
    @Rule
    public SupportFragmentTestRule fragmentTestRule = new SupportFragmentTestRule(new About());

    private Activity activity;
    private Fragment aboutFragment;

    @Before
    public void setUp() {
        this.activity = fragmentTestRule.getActivity();
        this.aboutFragment = fragmentTestRule.getFragment();
    }

    @Test
    public void testPreConditions() {
        Assert.assertNotNull(this.activity);
        Assert.assertNotNull(this.aboutFragment);
    }
}
