package bbct.android.ui.list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import bbct.android.data.BaseballCard
import bbct.android.data.BaseballCardDao
import bbct.android.ui.filter.FilterState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ListViewModel(val baseballCardDao: BaseballCardDao) : ViewModel() {
    val filterState = MutableStateFlow(FilterState())
    val isFiltered = mutableStateOf(false)
    val baseballCards = MutableStateFlow<List<BaseballCard>>(emptyList())

    init {
        viewModelScope.launch {
            filterState.collect { filter ->
                baseballCards.value = getBaseballCards(
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

    fun applyFilter(filter: FilterState) {
        filterState.value = filter
        isFiltered.value = filter != FilterState()
    }
}

@Suppress("UNCHECKED_CAST")
class ListViewModelFactory(private val baseballCardDao: BaseballCardDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            return ListViewModel(baseballCardDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
