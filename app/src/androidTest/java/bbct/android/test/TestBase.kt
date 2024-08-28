package bbct.android.test

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.printToLog
import bbct.android.data.BaseballCard
import org.junit.Rule

abstract class TestBase {
    @get:Rule
    val composeTestRule = createComposeRule()

    fun enterCardData(card: BaseballCard) {
        composeTestRule
            .onNodeWithText("Condition")
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()
        composeTestRule
            .onNodeWithText(card.condition)
            .performClick()
        composeTestRule
            .onNodeWithText("Brand")
            .assertIsDisplayed()
            .performTextReplacement(card.brand)
        composeTestRule
            .onNodeWithText("Year")
            .assertIsDisplayed()
            .performTextReplacement(card.year.toString())
        composeTestRule
            .onNodeWithText("Number")
            .assertIsDisplayed()
            .performTextReplacement(card.number)
        composeTestRule
            .onNodeWithText("Value")
            .assertIsDisplayed()
            .performTextReplacement((card.value / 100.0f).toString())
        composeTestRule
            .onNodeWithText("Quantity")
            .assertIsDisplayed()
            .performTextReplacement(card.quantity.toString())
        composeTestRule
            .onNodeWithText("Player Name")
            .assertIsDisplayed()
            .performTextReplacement(card.playerName)
        composeTestRule
            .onNodeWithText("Team")
            .assertIsDisplayed()
            .performTextReplacement(card.team)
        composeTestRule
            .onNodeWithText("Position")
            .assertIsDisplayed()
            .performClick()

        composeTestRule.onRoot().printToLog("TestBase")

        composeTestRule
            .onNodeWithText(card.position)
            .assertIsDisplayed()
            .performClick()
        composeTestRule
            .onNodeWithContentDescription("Save")
            .performClick()
    }
}
