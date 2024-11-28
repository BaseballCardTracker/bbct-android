package bbct.android.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import bbct.android.data.BaseballCard
import bbct.android.data.BaseballCardDao
import bbct.android.ui.filter.BaseballCardFilterState
import kotlinx.coroutines.flow.Flow

class BaseballCardListViewModel(val baseballCardDao: BaseballCardDao) : ViewModel() {
    val filterState = BaseballCardFilterState()
    val baseballCards: Flow<List<BaseballCard>> = getBaseballCards(
        filterState.brand,
        filterState.year,
        filterState.number,
        filterState.playerName,
        filterState.team
    )

    fun getBaseballCards(
        brand: String,
        year: Int,
        number: String,
        playerName: String,
        team: String,
    ): Flow<List<BaseballCard>> {
        return baseballCardDao.getBaseballCards(
            "%$brand%",
            year,
            "%$number%",
            "%$playerName%",
            "%$team%",
        )
    }
}

@Suppress("UNCHECKED_CAST")
class BaseballCardListViewModelFactory(private val baseballCardDao: BaseballCardDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        if (modelClass.isAssignableFrom(BaseballCardListViewModel::class.java)) {
            return BaseballCardListViewModel(baseballCardDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
