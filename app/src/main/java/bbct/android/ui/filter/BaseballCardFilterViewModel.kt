package bbct.android.ui.filter

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class BaseballCardFilterViewModel : ViewModel() {
    val filterState: MutableState<BaseballCardFilterState> =
        mutableStateOf(BaseballCardFilterState())
}
