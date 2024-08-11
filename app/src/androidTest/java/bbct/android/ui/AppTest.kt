package bbct.android.ui

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import bbct.android.data.BaseballCardDatabase
import org.junit.Rule
import org.junit.Test

class AppTest {
    @get:Rule
    val androidComposeTestRule = createComposeRule()

    @Test
    fun testApp() {
        androidComposeTestRule.setContent {
            val db = BaseballCardDatabase.getInstance(
                LocalContext.current,
                BaseballCardDatabase.TEST_DATABASE_NAME
            )
            App(db)
        }

        androidComposeTestRule.onNodeWithText("BBCT").assertIsDisplayed()
    }
}