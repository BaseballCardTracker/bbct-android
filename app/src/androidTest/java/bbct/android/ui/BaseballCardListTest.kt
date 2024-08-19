package bbct.android.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import bbct.android.data.cards
import org.junit.Rule
import org.junit.Test

class BaseballCardListTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testBaseballCardList() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            BaseballCardList(navController, cards)
        }

        for ((i, card) in cards.withIndex()) {
            composeTestRule.onAllNodesWithText(card.brand)[i].assertIsDisplayed()
            composeTestRule.onNodeWithText("${card.year}").assertIsDisplayed()
            composeTestRule.onNodeWithText(card.number).assertIsDisplayed()
            composeTestRule.onNodeWithText(card.playerName).assertIsDisplayed()
        }
    }
}