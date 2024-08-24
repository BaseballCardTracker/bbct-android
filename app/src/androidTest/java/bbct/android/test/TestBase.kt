package bbct.android.test

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
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
            .performTextInput(card.brand)
        composeTestRule
            .onNodeWithText("Year")
            .assertIsDisplayed()
            .performTextInput(card.year.toString())
        composeTestRule
            .onNodeWithText("Number")
            .assertIsDisplayed()
            .performTextInput(card.number)
        composeTestRule
            .onNodeWithText("Value")
            .assertIsDisplayed()
            .performTextInput(card.value.toString())
        composeTestRule
            .onNodeWithText("Quantity")
            .assertIsDisplayed()
            .performTextInput(card.quantity.toString())
        composeTestRule
            .onNodeWithText("Player Name")
            .assertIsDisplayed()
            .performTextInput(card.playerName)
        composeTestRule
            .onNodeWithText("Team")
            .assertIsDisplayed()
            .performTextInput(card.team)
        composeTestRule
            .onNodeWithText("Position")
            .assertIsDisplayed()
            .performClick()
        composeTestRule
            .onNodeWithText(card.position)
            .performClick()
        composeTestRule
            .onNodeWithContentDescription("Save")
            .performClick()
    }

}