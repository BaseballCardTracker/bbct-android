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
import android.test.ActivityInstrumentationTestCase2;
import bbct.android.common.R;
import bbct.android.common.activity.About;
import junit.framework.Assert;

/**
 * Tests for {@link About} activity class.
 *
 * TODO: Add tests for the layout of {@link About}
 */
public class AboutTest extends ActivityInstrumentationTestCase2<About> {

    /**
     * Create instrumented test cases for {@link About}.
     */
    public AboutTest() {
        super(About.class);
    }

    /**
     * Set up test fixture. This consists of an instance of the {@link About}
     * activity.
     *
     * @throws Exception If an error occurs while chaining to the super class.
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.activity = this.getActivity();
    }

    /**
     * Check preconditions which must hold to guarantee the validity of all
     * other tests. Assert that the {@link Activity} to test is not
     * <code>null</code>
     */
    public void testPreConditions() {
        Assert.assertNotNull(this.activity);
    }

    /**
     * Test that the title of the {@link Activity} is correct.
     */
    public void testTitle() {
        String title = this.activity.getTitle().toString();
        String aboutTitle = this.activity.getString(R.string.about_title);

        Assert.assertTrue(title.contains(aboutTitle));
    }
    private Activity activity = null;
}
