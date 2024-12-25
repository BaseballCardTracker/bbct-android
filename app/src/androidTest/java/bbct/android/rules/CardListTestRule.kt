package bbct.android.rules

import androidx.test.platform.app.InstrumentationRegistry
import bbct.android.data.BaseballCard
import bbct.android.test.BaseballCardCsvFileReader
import org.junit.rules.ExternalResource

open class CardListTestRule(private val filename: String) : ExternalResource() {
    var cards: List<BaseballCard>? = null

    override fun before() {
        val inst = InstrumentationRegistry.getInstrumentation()

        val cardInputStream = inst.context.assets.open(filename)
        val cardInput: BaseballCardCsvFileReader = BaseballCardCsvFileReader(
            cardInputStream,
            true
        )
        cards = cardInput.getAllBaseballCards()
        cardInput.close()
    }
}
