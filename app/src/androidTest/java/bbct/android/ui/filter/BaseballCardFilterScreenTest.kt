package bbct.android.ui.filter

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import bbct.android.data.cards
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BaseballCardFilterScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            BaseballCardFilterScreen(
                onApplyFilter = { },
                onClose = { })
        }
    }

    @Test
    fun testBaseballCardFilterFieldsAreVisible() {
        composeTestRule
            .onNodeWithText("Brand")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Year")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Number")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Player Name")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Team")
            .assertIsDisplayed()
    }

    @Test
    fun testFilterByBrand() {
        composeTestRule
            .onNodeWithText("Brand")
            .assertIsDisplayed()
            .performTextInput(cards[0].brand)
        composeTestRule
            .onNodeWithContentDescription("Filter Cards")
            .assertIsDisplayed()
            .performClick()
    }

    @Test
    fun testFilterByYear() {
        composeTestRule
            .onNodeWithText("Year")
            .assertIsDisplayed()
            .performTextInput(cards[0].year.toString())
        composeTestRule
            .onNodeWithContentDescription("Filter Cards")
            .assertIsDisplayed()
            .performClick()
    }

    @Test
    fun testFilterByNumber() {
        composeTestRule
            .onNodeWithText("Number")
            .assertIsDisplayed()
            .performTextInput(cards[0].number)
        composeTestRule
            .onNodeWithContentDescription("Filter Cards")
            .assertIsDisplayed()
            .performClick()
    }

    @Test
    fun testFilterByPlayerName() {
        composeTestRule
            .onNodeWithText("Player Name")
            .assertIsDisplayed()
            .performTextInput(cards[0].playerName)
        composeTestRule
            .onNodeWithContentDescription("Filter Cards")
            .assertIsDisplayed()
            .performClick()
    }

    @Test
    fun testFilterByTeam() {
        composeTestRule
            .onNodeWithText("Team")
            .assertIsDisplayed()
            .performTextInput(cards[0].team)
        composeTestRule
            .onNodeWithContentDescription("Filter Cards")
            .assertIsDisplayed()
            .performClick()
    }
}
