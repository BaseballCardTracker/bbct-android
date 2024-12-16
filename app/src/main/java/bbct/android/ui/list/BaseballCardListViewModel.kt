package bbct.android.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import bbct.android.data.BaseballCard
import bbct.android.data.BaseballCardDao
import bbct.android.ui.filter.BaseballCardFilterState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BaseballCardListViewModel(val baseballCardDao: BaseballCardDao) : ViewModel() {
    val filterState = MutableStateFlow(BaseballCardFilterState())
    private val _baseballCards = MutableStateFlow<List<BaseballCard>>(emptyList())
    val baseballCards: Flow<List<BaseballCard>> = _baseballCards

    init {
        viewModelScope.launch {
            filterState.collect { filter ->
                _baseballCards.value = getBaseballCards(
                    filter.brand,
                    filter.year,
                    filter.number,
                    filter.playerName,
                    filter.team
                ).first()
            }
        }
    }

    private fun getBaseballCards(
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

    fun applyFilter(filter: BaseballCardFilterState) {
        filterState.value = filter
    }

    fun isFiltered() = filterState.value != BaseballCardFilterState()
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
