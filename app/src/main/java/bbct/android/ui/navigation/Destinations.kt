package bbct.android.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object BaseballCardListDestination

@Serializable
object BaseballCardCreateDestination

@Serializable
data class BaseballCardEditDestination(val cardId: Long)

@Serializable
object BaseballCardFilterDestination

@Serializable
object AboutDestination
