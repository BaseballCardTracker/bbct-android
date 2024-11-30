package bbct.android.ui.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.platform.app.InstrumentationRegistry
import bbct.android.data.BaseballCardDatabase
import bbct.android.rules.CardDatabaseTestRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BaseballCardListScreenTest {
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val db = inMemoryDatabaseBuilder(
        context,
        BaseballCardDatabase::class.java
    ).build()

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val cardDatabaseTestRule = CardDatabaseTestRule("cards.csv", db)

    @Before
    fun setup() {
        composeTestRule.setContent {
            var navController = rememberNavController()
            BaseballCardListScreen(navController, db)
        }
    }

    @Test
    fun testFilterCardsByYear() {
        composeTestRule.onNodeWithContentDescription("Filter Cards").performClick()
        composeTestRule.onNodeWithText("Filter Cards").assertIsDisplayed()
        composeTestRule.onNodeWithText("Year").performTextInput("1993")
        composeTestRule.onNodeWithContentDescription("Apply Filter").performClick()

        val filteredCards = cardDatabaseTestRule.cards!!.filter { it.year == 1993 }
        for (card in filteredCards) {
            composeTestRule.onNodeWithText(card.brand).assertIsDisplayed()
            composeTestRule.onNodeWithText(card.year.toString()).assertIsDisplayed()
            composeTestRule.onNodeWithText(card.number).assertIsDisplayed()
            composeTestRule.onNodeWithText(card.playerName).assertIsDisplayed()
        }
    }
}
