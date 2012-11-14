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

import android.util.Log;
import android.widget.EditText;
import junit.framework.Assert;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class PlayerNameFilterTest extends FilterActivityTest<PlayerNameFilter> {

    public PlayerNameFilterTest() {
        super(PlayerNameFilter.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.playerNameText = (EditText) this.activity.findViewById(R.id.player_name_text);

        this.testPlayerName = "codeguru";
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    public void testPreConditions() {
        super.testPreConditions();

        Assert.assertNotNull(this.playerNameText);

        Assert.assertEquals("", this.playerNameText.getText().toString());
        Assert.assertTrue(this.playerNameText.hasFocus());
    }

    @Override
    protected String getTitleSubString() {
        return this.activity.getString(R.string.player_name_filter_title);
    }

    @Override
    protected void checkErrorMessage() {
        Assert.fail("Need to test that error message is displayed");
    }

    @Override
    protected void setInputText() {
        this.playerNameText.setText(this.testPlayerName);
    }

    @Override
    protected void sendInputKeys() {
        Log.d(TAG, "sendInputKeys()");
        AndroidTestUtil.sendKeysFromString(this, this.testPlayerName);
    }
    private EditText playerNameText = null;
    private String testPlayerName = null;
    private static final String TAG = "PlayerNameFilterTest";
}
