package bbct.android.data

import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.platform.app.InstrumentationRegistry
import bbct.android.test.BaseballCardCsvFileReader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Test

class LoadDataTest {
    @Test
    fun loadData() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val db = inMemoryDatabaseBuilder(
            instrumentation.targetContext,
            BaseballCardDatabase::class.java
        ).build()
        val csvReader = BaseballCardCsvFileReader(
            instrumentation.context.assets.open("cards.csv"),
            hasColHeaders = true
        )

        CoroutineScope(Dispatchers.IO).launch {
            while (csvReader.hasNextBaseballCard()) {
                val card = csvReader.getNextBaseballCard()
                db.baseballCardDao.insertBaseballCard(card)
            }
        }

        db.close()
    }
}
