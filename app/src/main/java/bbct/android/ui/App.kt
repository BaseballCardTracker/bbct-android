package bbct.android.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import bbct.android.data.BaseballCardDatabase
import bbct.android.ui.navigation.AboutDestination
import bbct.android.ui.navigation.BaseballCardCreateDestination
import bbct.android.ui.navigation.BaseballCardEditDestination
import bbct.android.ui.navigation.BaseballCardFilterDestination
import bbct.android.ui.navigation.BaseballCardListDestination
import bbct.android.ui.theme.AppTheme

@Composable
fun App(db: BaseballCardDatabase) {
    val navController = rememberNavController()

    AppTheme {
        NavHost(
            navController = navController,
            startDestination = BaseballCardListDestination.route,
        ) {
            composable(route = BaseballCardListDestination.route) {
                BaseballCardListDestination.screen(
                    navController,
                    db
                )
            }
            composable(route = BaseballCardCreateDestination.route) {
                BaseballCardCreateDestination.screen(
                    navController,
                    db
                )
            }
            composable(
                route = BaseballCardEditDestination.route,
                arguments = listOf(navArgument("cardId") { type = NavType.LongType }),
            ) { backStackEntry ->
                val cardId = backStackEntry.arguments?.getLong("cardId")
                BaseballCardEditDestination.screen(
                    navController,
                    db,
                    cardId!!
                )
            }
            composable(route = BaseballCardFilterDestination.route) {
                BaseballCardFilterDestination.screen(
                    navController,
                    db
                )
            }
            composable(route = AboutDestination.route) {
                AboutDestination.screen(
                    navController,
                    db
                )
            }
        }
    }
}
