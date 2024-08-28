package bbct.android.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.platform.app.InstrumentationRegistry
import bbct.android.data.BaseballCardDatabase
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BaseballCardFilterScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val db = inMemoryDatabaseBuilder(
            context,
            BaseballCardDatabase::class.java
        ).build()

        composeTestRule.setContent {
            val navController = rememberNavController()
            BaseballCardFilterScreen(
                navController = navController,
                db = db
            )
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
}
