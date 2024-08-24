package bbct.android.ui

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.platform.app.InstrumentationRegistry
import bbct.android.data.BaseballCard
import bbct.android.data.BaseballCardDatabase
import bbct.android.test.TestBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import org.junit.Test

class BaseballCardCreateTest : TestBase() {

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
            BaseballCardCreateScreen(navController, db)
        }

        enterCardData(card)

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
            BaseballCardCreateScreen(navController, db)
        }

        enterCardData(card)

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
