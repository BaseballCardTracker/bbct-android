package bbct.android.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import bbct.android.data.BaseballCard
import org.junit.Rule
import org.junit.Test

class BaseballCardListTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testBaseballCardList() {
        val cards = listOf(
            BaseballCard(
                _id = 1,
                autographed = false,
                condition = "Excellent",
                brand = "Topps",
                year = 1991,
                number = "278",
                value = 500,
                quantity = 1,
                playerName = "Alex Fernandez",
                team = "White Sox",
                position = "Pitcher"
            ),
            BaseballCard(
                _id = 2,
                autographed = true,
                condition = "Mint",
                brand = "Topps",
                year = 1974,
                number = "175",
                value = 1000,
                quantity = 1,
                playerName = "Bob Stanley",
                team = "Red Sox",
                position = "Pitcher"
            ),
            BaseballCard(
                _id = 3,
                autographed = false,
                condition = "Very Good",
                brand = "Topps",
                year = 1985,
                number = "201",
                value = 200,
                quantity = 1,
                playerName = "Vince Coleman",
                team = "Cardinals",
                position = "Left Field",
            ),
        )

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