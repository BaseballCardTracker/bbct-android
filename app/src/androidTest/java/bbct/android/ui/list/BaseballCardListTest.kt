package bbct.android.ui.list

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import bbct.android.rules.CardListTestRule
import bbct.android.test.hasRole
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BaseballCardListTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val cardListRule = CardListTestRule("three_cards.csv")

    private var cardListState = mutableListOf<SelectedState>()

    @Before
    fun setup() {
        cardListState = cardListRule.cards!!
            .map {
                SelectedState(
                    it,
                    false
                )
            }
            .toMutableList()
        composeTestRule.setContent {
            val navController = rememberNavController()
            BaseballCardList(
                navController = navController,
                cards = cardListState,
                onCardChanged = { index, card -> cardListState[index] = card },
            )
        }
    }

    @Test
    fun testDisplaysCards() {
        for ((i, card) in cardListRule.cards!!.withIndex()) {
            composeTestRule.onAllNodesWithText(card.brand)[i].assertIsDisplayed()
            composeTestRule
                .onNodeWithText("${card.year}")
                .assertIsDisplayed()
            composeTestRule
                .onNodeWithText(card.number)
                .assertIsDisplayed()
            composeTestRule
                .onNodeWithText(card.playerName)
                .assertIsDisplayed()
        }
    }

    @Test
    fun testSelectCard() {
        assert(!cardListState[0].selected)
        composeTestRule.onAllNodes(hasRole(Role.Checkbox))[0].performClick()
        assert(cardListState[0].selected)
    }
}
