package bbct.android.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import bbct.android.data.cards
import bbct.android.test.hasRole
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BaseballCardListTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private var cardListState = mutableListOf<BaseballCardSelectedState>()

    @Before
    fun setup(): Unit {
        cardListState = cards.map { BaseballCardSelectedState(it, false) }.toMutableList()
        composeTestRule.setContent {
            val navController = rememberNavController()
            BaseballCardList(
                navController = navController,
                cards = cardListState,
                onCardChanged = { index, card -> cardListState[index] = card },
                contentPadding = PaddingValues(),
            )
        }
    }

    @Test
    fun testDisplaysCards() {
        for ((i, card) in cards.withIndex()) {
            composeTestRule.onAllNodesWithText(card.brand)[i].assertIsDisplayed()
            composeTestRule.onNodeWithText("${card.year}").assertIsDisplayed()
            composeTestRule.onNodeWithText(card.number).assertIsDisplayed()
            composeTestRule.onNodeWithText(card.playerName).assertIsDisplayed()
        }
    }

    @Test
    fun testSelectCard() {
        assert(!cardListState[0].selected)
        composeTestRule.onAllNodes(hasRole(Role.Checkbox))[0].performClick()
        assert(cardListState[0].selected)
    }
}
