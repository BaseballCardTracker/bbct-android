package bbct.android.rules

import bbct.android.data.BaseballCardDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CardDatabaseTestRule(filename: String, val db: BaseballCardDatabase) :
    CardListTestRule(filename) {
    override fun before() {
        super.before()

        CoroutineScope(Dispatchers.IO).launch {
            for (card in cards!!) {
                db.baseballCardDao.insertBaseballCard(card)
            }
        }
    }
}
