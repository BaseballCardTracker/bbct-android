package bbct.android.ui

import androidx.lifecycle.ViewModel
import bbct.android.data.BaseballCard
import bbct.android.data.BaseballCardDao
import kotlinx.coroutines.flow.Flow

class BaseballCardListViewModel(val baseballCardDao: BaseballCardDao) : ViewModel() {
    val baseballCards: Flow<List<BaseballCard>> = baseballCardDao.baseballCards
}
