package bbct.android.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class BaseballCardDetailsViewModel : ViewModel() {
    var baseballCardState = mutableStateOf(BaseballCardState())
}
