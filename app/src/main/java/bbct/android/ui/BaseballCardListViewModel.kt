package bbct.android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import bbct.android.data.BaseballCard
import bbct.android.data.BaseballCardDao
import kotlinx.coroutines.flow.Flow

class BaseballCardListViewModel(baseballCardDao: BaseballCardDao) : ViewModel() {
    val baseballCards: Flow<List<BaseballCard>> = baseballCardDao.baseballCards
}

@Suppress("UNCHECKED_CAST")
class BaseballCardListViewModelFactory(private val baseballCardDao: BaseballCardDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(BaseballCardListViewModel::class.java)) {
            return BaseballCardListViewModel(baseballCardDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
