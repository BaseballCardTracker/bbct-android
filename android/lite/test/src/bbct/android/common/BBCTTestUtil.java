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
import android.app.Instrumentation;
import android.test.InstrumentationTestCase;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import bbct.common.data.BaseballCard;
import java.util.List;
import junit.framework.Assert;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
abstract public class BBCTTestUtil {

    public static void assertListViewContainsItems(Instrumentation inst, List<BaseballCard> expectedItems, ListView listView) {
        inst.waitForIdleSync();
        Assert.assertEquals(expectedItems.size(), listView.getCount());

        for (int i = 0; i < expectedItems.size(); ++i) {
            View row = listView.getChildAt(i);
            TextView brandTextView = (TextView) row.findViewById(R.id.brand_text_view);
            TextView yearTextView = (TextView) row.findViewById(R.id.year_text_view);
            TextView numberTextView = (TextView) row.findViewById(R.id.number_text_view);
            TextView playerNameTextView = (TextView) row.findViewById(R.id.player_name_text_view);

            BaseballCard expectedCard = expectedItems.get(i);
            Assert.assertEquals(expectedCard.getBrand(), brandTextView.getText().toString());
            Assert.assertEquals(expectedCard.getYear(), Integer.parseInt(yearTextView.getText().toString()));
            Assert.assertEquals(expectedCard.getNumber(), Integer.parseInt(numberTextView.getText().toString()));
            Assert.assertEquals(expectedCard.getPlayerName(), playerNameTextView.getText().toString());
        }
    }

    public static Activity testMenuItem(Instrumentation inst, Activity activity, int menuId, Class childActivityClass) {
        Instrumentation.ActivityMonitor monitor = new Instrumentation.ActivityMonitor(childActivityClass.getName(), null, false);
        inst.addMonitor(monitor);

        Assert.assertTrue(inst.invokeMenuActionSync(activity, menuId, MENU_FLAGS));

        Activity childActivity = inst.waitForMonitorWithTimeout(monitor, TIME_OUT);

        Assert.assertNotNull(childActivity);
        Assert.assertEquals(childActivityClass, childActivity.getClass());

        return childActivity;
    }

    public static void addCard(InstrumentationTestCase test, Activity cardDetails, BaseballCard card) {
        BBCTTestUtil.sendKeysToCardDetails(test, cardDetails, card);

        final Button saveButton = (Button) cardDetails.findViewById(R.id.save_button);

        test.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                Assert.assertTrue(saveButton.performClick());

                // TODO Check that Toast appears with correct message.
            }
        });
    }

    public static void clickCardDetailsDone(Instrumentation inst, Activity cardDetails) {
        final Button doneButton = (Button) cardDetails.findViewById(R.id.done_button);

        inst.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                Assert.assertTrue(doneButton.performClick());
            }
        });

        Assert.assertTrue(cardDetails.isFinishing());
    }

    public static void sendKeysToCardDetails(InstrumentationTestCase test, Activity cardDetails, BaseballCard card) {
        BBCTTestUtil.sendKeysToCardDetails(test, cardDetails, card, ALL_FIELDS);
    }

    public static void sendKeysToCardDetails(InstrumentationTestCase test, Activity cardDetails, BaseballCard card, int skipFlags) {
        Instrumentation inst = test.getInstrumentation();

        if ((skipFlags & NO_BRAND) == 0) {
            inst.sendStringSync(card.getBrand());
            inst.sendKeyDownUpSync(KeyEvent.KEYCODE_ESCAPE);
        }
        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);

        if ((skipFlags & NO_YEAR) == 0) {
            inst.sendStringSync(Integer.toString(card.getYear()));
        }
        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);

        if ((skipFlags & NO_NUMBER) == 0) {
            inst.sendStringSync(Integer.toString(card.getNumber()));
        }
        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);

        if ((skipFlags & NO_VALUE) == 0) {
            String valueStr = String.format("%.2f", card.getValue() / 100.0);
            inst.sendStringSync(valueStr);
        }
        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);

        if ((skipFlags & NO_COUNT) == 0) {
            inst.sendStringSync(Integer.toString(card.getCount()));
        }
        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);

        if ((skipFlags & NO_PLAYER_NAME) == 0) {
            inst.sendStringSync(card.getPlayerName());
        }
        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);

        Spinner playerPositionSpinner = (Spinner) cardDetails.findViewById(R.id.player_position_text);
        ArrayAdapter<CharSequence> playerPositionAdapter = (ArrayAdapter<CharSequence>) playerPositionSpinner.getAdapter();
        int newPos = playerPositionAdapter.getPosition(card.getPlayerPosition());
        int oldPos = playerPositionSpinner.getSelectedItemPosition();
        int move = newPos - oldPos;

        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_CENTER);
        if (move > 0) {
            test.sendRepeatedKeys(move, KeyEvent.KEYCODE_DPAD_DOWN);
        } else {
            test.sendRepeatedKeys(-move, KeyEvent.KEYCODE_DPAD_UP);
        }
        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_CENTER);
        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);
    }

    private BBCTTestUtil() {
    }
    public static final int ALL_FIELDS = 0x00;
    public static final int NO_BRAND = 0x01;
    public static final int NO_YEAR = 0x02;
    public static final int NO_NUMBER = 0x04;
    public static final int NO_VALUE = 0x08;
    public static final int NO_COUNT = 0x10;
    public static final int NO_PLAYER_NAME = 0x20;
    private static final int MENU_FLAGS = 0;
    public static final int TIME_OUT = 5 * 1000; // 5 seconds
}
