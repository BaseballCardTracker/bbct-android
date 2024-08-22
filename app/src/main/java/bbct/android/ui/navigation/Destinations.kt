package bbct.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import bbct.android.data.BaseballCardDatabase
import bbct.android.ui.AboutScreen
import bbct.android.ui.BaseballCardCreateScreen
import bbct.android.ui.BaseballCardFilterScreen
import bbct.android.ui.BaseballCardListScreen

interface Destination {
    val route: String
    val screen: @Composable (NavController, BaseballCardDatabase) -> Unit
}

object BaseballCardListDestination : Destination {
    override val route = "baseball_card_list"
    override val screen: @Composable (NavController, BaseballCardDatabase) -> Unit =
        { navController: NavController, db: BaseballCardDatabase ->
            BaseballCardListScreen(
                navController,
                db,
            )
        }
}

object BaseballCardCreateDestination : Destination {
    override val route = "baseball_card_details"
    override val screen: @Composable (NavController, BaseballCardDatabase) -> Unit =
        { navController: NavController, db: BaseballCardDatabase ->
            BaseballCardCreateScreen(
                navController,
                db,
            )
        }
}

object BaseballCardFilterDestination : Destination {
    override val route = "baseball_card_filter"
    override val screen: @Composable (NavController, BaseballCardDatabase) -> Unit =
        { navController: NavController, db: BaseballCardDatabase ->
            BaseballCardFilterScreen(
                navController,
                db,
            )
        }
}

object AboutDestination : Destination {
    override val route = "about"
    override val screen: @Composable (NavController, BaseballCardDatabase) -> Unit =
        { navController: NavController, db: BaseballCardDatabase ->
            AboutScreen(
                navController,
                db,
            )
        }
}
