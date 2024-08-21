package bbct.android.ui

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.platform.app.InstrumentationRegistry
import bbct.android.data.BaseballCard
import bbct.android.data.BaseballCardDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import org.junit.Rule
import org.junit.Test

class BaseballCardDetailsScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testBaseballCardDetailsSaveCard() {
        val card = BaseballCard(
            autographed = false,
            condition = "Mint",
            brand = "Topps",
            year = 1987,
            number = "123",
            value = 100,
            quantity = 1,
            playerName = "John Doe",
            team = "Yankees",
            position = "Pitcher"
        )

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val db = inMemoryDatabaseBuilder(context, BaseballCardDatabase::class.java).build()

        composeTestRule.setContent {
            val navController = rememberNavController()
            BaseballCardDetailsScreen(navController, db)
        }

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

        CoroutineScope(Dispatchers.IO).launch {
            val savedCard = db.baseballCardDao.baseballCards.single()
            assert(savedCard == listOf(card))
        }
    }

    @Test
    fun testBaseballCardDetailsSaveCardClearsFields() {
        val card = BaseballCard(
            autographed = false,
            condition = "Mint",
            brand = "Topps",
            year = 1987,
            number = "123",
            value = 100,
            quantity = 1,
            playerName = "John Doe",
            team = "Yankees",
            position = "Pitcher"
        )

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val db = inMemoryDatabaseBuilder(context, BaseballCardDatabase::class.java).build()

        composeTestRule.setContent {
            val navController = rememberNavController()
            BaseballCardDetailsScreen(navController, db)
        }

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

        composeTestRule
            .onNodeWithText("Brand")
            .assert(hasText(""))
        composeTestRule
            .onNodeWithText("Year")
            .assert(hasText(""))
        composeTestRule
            .onNodeWithText("Number")
            .assert(hasText(""))
        composeTestRule
            .onNodeWithText("Value")
            .assert(hasText(""))
        composeTestRule
            .onNodeWithText("Quantity")
            .assert(hasText(""))
        composeTestRule
            .onNodeWithText("Player Name")
            .assert(hasText(""))
        composeTestRule
            .onNodeWithText("Team")
            .assert(hasText(""))
    }
}
