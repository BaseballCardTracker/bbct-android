package bbct.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import bbct.android.data.BaseballCardDatabase
import bbct.android.ui.AboutScreen
import bbct.android.ui.BaseballCardCreateScreen
import bbct.android.ui.BaseballCardFilterScreen
import bbct.android.ui.BaseballCardListScreen

object BaseballCardListDestination {
    const val route = "baseball_card_list"
    val screen: @Composable (NavController, BaseballCardDatabase) -> Unit =
        { navController, db ->
            BaseballCardListScreen(
                navController,
                db,
            )
        }
}

object BaseballCardCreateDestination {
    const val route = "baseball_card_details"
    val screen: @Composable (NavController, BaseballCardDatabase) -> Unit =
        { navController, db ->
            BaseballCardCreateScreen(
                navController,
                db,
            )
        }
}

object BaseballCardFilterDestination {
    const val route = "baseball_card_filter"
    val screen: @Composable (NavController, BaseballCardDatabase) -> Unit =
        { navController, db ->
            BaseballCardFilterScreen(
                navController,
                db,
            )
        }
}

object AboutDestination {
    const val route = "about"
    val screen: @Composable (NavController, BaseballCardDatabase) -> Unit =
        { navController, db ->
            AboutScreen(
                navController,
                db,
            )
        }
}
