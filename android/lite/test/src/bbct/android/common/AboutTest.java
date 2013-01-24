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
package bbct.android.common;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import junit.framework.Assert;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class AboutTest extends ActivityInstrumentationTestCase2<About> {

    public AboutTest() {
        super(About.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.activity = this.getActivity();
    }

    @Override
    public void tearDown() throws Exception {
        this.activity.finish();

        super.tearDown();
    }

    public void testPreConditions() {
        Assert.assertNotNull(this.activity);
    }

    public void testTitle() {
        String title = this.activity.getTitle().toString();
        String aboutTitle = this.activity.getString(R.string.about_title);

        Assert.assertTrue(title.contains(aboutTitle));
    }
    private Activity activity = null;
}
