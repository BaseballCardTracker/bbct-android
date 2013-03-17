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
import android.util.Log;
import android.widget.EditText;
import junit.framework.Assert;

/**
 * Tests for {@link PlayerNameFilter}.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class PlayerNameFilterTest extends FilterActivityTest<PlayerNameFilter> {

    /**
     * Create instrumented test cases for {@link PlayerNameFilter}.
     */
    public PlayerNameFilterTest() {
        super(PlayerNameFilter.class);
    }

    /**
     * Set up test fixture. Most of the test fixture is set up by
     * {@link FilterActivityTest#setUp()}. This class adds a {@link EditText}
     * view which contain player name being edited and an {@link String} for the
     * player name.
     *
     * @throws Exception If an error occurs while chaining to the super class.
     *
     * @see FilterActivityTest#setUp()
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.playerNameText = (EditText) this.activity.findViewById(R.id.player_name_text);

        this.testPlayerName = "codeguru";
    }

    /**
     * Tear down the test fixture by calling {@link Activity#finish()}.
     *
     * @throws Exception If an error occurs while chaining to the super class.
     *
     * @see FilterActivityTest#tearDown()
     */
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Check preconditions which must hold to guarantee the validity of all
     * other tests. Most preconditions are checked by
     * {@link FilterActivityTest#testPreConditions()}. In addition, this class
     * checks that the {@link EditText} view for player name is not
     * <code>null</code>, that it is empty, and that it has focus.
     *
     * @see FilterActivityTest#testPreConditions()
     */
    @Override
    public void testPreConditions() {
        super.testPreConditions();

        Assert.assertNotNull(this.playerNameText);

        Assert.assertEquals("", this.playerNameText.getText().toString());
        Assert.assertTrue(this.playerNameText.hasFocus());
    }

    /**
     * Get the substring representing this {@link Activity} which should appear
     * in the title bar.
     *
     * @return The substring representing this {@link Activity} which should
     * appear in the title bar.
     *
     * @see FilterActivityTest#testTitle()
     */
    @Override
    protected String getTitleSubString() {
        return this.activity.getString(R.string.player_name_filter_title);
    }

    /**
     * Assert that the correct error message is set in the {@link EditText} view
     * when it is empty.
     *
     * @see FilterActivityTest#testOkButtonOnClickWithNoInput()}
     */
    @Override
    protected void checkErrorMessage() {
        String expectedError = this.activity.getString(R.string.player_name_input_error);
        Assert.assertEquals(expectedError, this.playerNameText.getError());
    }

    /**
     * Set the text of the player name {@link EditText} views.
     */
    @Override
    protected void setInputText() {
        this.playerNameText.setText(this.testPlayerName);
    }

    /**
     * Inject instrumented key events for the player name text.
     *
     * @see FilterActivityTest#testOkButtonOnClickWithSendInputKeys()
     */
    @Override
    protected void sendInputKeys() {
        Log.d(TAG, "sendInputKeys()");
        this.getInstrumentation().sendStringSync(this.testPlayerName);
    }
    private EditText playerNameText = null;
    private String testPlayerName = null;
    private static final String TAG = "PlayerNameFilterTest";
}
