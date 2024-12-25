package bbct.android.ui.filter

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class FilterViewModel : ViewModel() {
    val filterState: MutableState<FilterState> =
        mutableStateOf(FilterState())
}
