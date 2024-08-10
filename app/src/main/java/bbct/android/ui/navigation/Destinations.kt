package bbct.android.ui.navigation

import androidx.compose.runtime.Composable
import bbct.android.ui.BaseballCardDetailsScreen
import bbct.android.ui.BaseballCardListScreen

interface Destination {
    val route: String
    val screen: @Composable () -> Unit
}

object BaseballCardListDestination : Destination {
    override val route = "baseball_card_list"
    override val screen: @Composable () -> Unit = { BaseballCardListScreen() }
}

object BaseballCardDetailsDestination : Destination {
    override val route = "baseball_card_details"
    override val screen: @Composable () -> Unit = { BaseballCardDetailsScreen() }
}
