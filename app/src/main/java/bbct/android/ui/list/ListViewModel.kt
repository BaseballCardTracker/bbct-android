package bbct.android.ui.list

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import bbct.android.data.BaseballCard
import bbct.android.data.BaseballCardDao
import bbct.android.ui.filter.FilterState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ListViewModel(val baseballCardDao: BaseballCardDao) : ViewModel() {
    val filterState = mutableStateOf(FilterState())
    val isFiltered = mutableStateOf(false)
    var baseballCards = MutableStateFlow<List<BaseballCard>>(emptyList())

    init {
        viewModelScope.launch {
            snapshotFlow<FilterState> {
                filterState.value
            }.flatMapLatest { filter ->
                getBaseballCards(
                    filter.brand,
                    filter.year,
                    filter.number,
                    filter.playerName,
                    filter.team
                )
            }.collect { baseballCards.value = it }
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
