package bbct.android.ui.details

import androidx.navigation.compose.rememberNavController
import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.platform.app.InstrumentationRegistry
import bbct.android.data.BaseballCardDatabase
import bbct.android.data.cards
import bbct.android.test.TestBase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test

class BaseballCardEditTest : TestBase() {
    @Test
    fun testBaseballCardEdit() {
        runTest {
            val card = cards[0]
            val editCard = cards[2]

            val context = InstrumentationRegistry.getInstrumentation().targetContext
            val db = inMemoryDatabaseBuilder(
                context,
                BaseballCardDatabase::class.java
            ).build()

            launch {
                db.baseballCardDao.insertBaseballCard(card)
            }

            composeTestRule.setContent {
                val navController = rememberNavController()
                EditScreen(
                    navController = navController,
                    db = db,
                    cardId = card._id!!
                )
            }

            enterCardData(editCard)

            launch {
                val savedCard = db.baseballCardDao.baseballCards.single()
                assertThat(savedCard).isEqualTo(listOf(editCard))
            }
        }
    }
}
