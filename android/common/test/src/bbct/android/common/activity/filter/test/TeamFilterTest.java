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
package bbct.android.common.activity.filter.test;

import android.widget.EditText;
import bbct.android.common.R;
import bbct.android.common.activity.filter.TeamFilter;
import junit.framework.Assert;

/**
 * Tests for {@link TeamFilter}.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class TeamFilterTest extends FilterActivityTest<TeamFilter> {

    /**
     * Create instrumented test cases for {@link TeamFilter}.
     */
    public TeamFilterTest() {
        super(TeamFilter.class);
    }

    /**
     * Set up test fixture. Most of the test fixture is set up by
     * {@link FilterActivityTest#setUp()}. This class adds a {@link EditText}
     * view which contains the team name being edited and a {@link String} for
     * the team name.
     *
     * @throws Exception If an error occurs while chaining to the super class.
     *
     * @see FilterActivityTest#setUp()
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.teamText = (EditText) this.activity.findViewById(R.id.team_text);

        this.testTeam = "codeguru devs";
    }

    /**
     * Check preconditions which must hold to guarantee the validity of all
     * other tests. Most preconditions are checked by
     * {@link FilterActivityTest#testPreConditions()}. In addition, this class
     * checks that the {@link EditText} view for the team name is not
     * <code>null</code>, that it is empty, and that it has focus.
     *
     * @see FilterActivityTest#testPreConditions()
     */
    @Override
    public void testPreConditions() {
        super.testPreConditions();

        Assert.assertNotNull(this.teamText);

        Assert.assertEquals("", this.teamText.getText().toString());
        Assert.assertTrue(this.teamText.hasFocus());
    }

    /**
     * Get the substring which should appear in the title bar of the
     * {@link Activity} being tested.
     *
     * @return The substring which should appear in the title bar of the
     * {@link Activity} being tested.
     *
     * @see FilterActivityTest#testTitle()
     */
    @Override
    protected String getTitleSubString() {
        return this.activity.getString(R.string.team_filter_title);
    }

    /**
     * Assert that the correct error message is set in the {@link EditText} view
     * when it is empty.
     *
     * @see FilterActivityTest#testOkButtonOnClickWithNoInput()
     */
    @Override
    protected void checkErrorMessage() {
        String expectedError = this.activity.getString(R.string.team_input_error);
        Assert.assertEquals(expectedError, this.teamText.getError());
    }

    /**
     * Inject instrumented key events to the team {@link EditText} view.
     *
     * @see FilterActivityTest#testOkButtonOnClickWithSendInputKeys()
     */
    @Override
    protected void sendInputKeys() {
        this.getInstrumentation().sendStringSync(this.testTeam);
    }
    private EditText teamText = null;
    private String testTeam = null;
}
