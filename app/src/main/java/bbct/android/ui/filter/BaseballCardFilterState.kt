package bbct.android.ui.filter

import kotlinx.serialization.Serializable

@Serializable
data class BaseballCardFilterState(
    var brand: String = "",
    var year: Int = -1,
    var number: String = "",
    var playerName: String = "",
    var team: String = "",
)
