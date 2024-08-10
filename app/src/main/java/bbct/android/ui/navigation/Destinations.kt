package bbct.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import bbct.android.ui.BaseballCardDetailsScreen
import bbct.android.ui.BaseballCardListScreen

interface Destination {
    val route: String
    val screen: @Composable (NavHostController) -> Unit
}

object BaseballCardListDestination : Destination {
    override val route = "baseball_card_list"
    override val screen: @Composable (navController: NavHostController) -> Unit =
        { navController -> BaseballCardListScreen(navController) }
}

object BaseballCardDetailsDestination : Destination {
    override val route = "baseball_card_details"
    override val screen: @Composable (NavHostController) -> Unit =
        { navController -> BaseballCardDetailsScreen(navController) }
}
