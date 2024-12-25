package bbct.android.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import bbct.android.data.BaseballCardDatabase
import bbct.android.ui.details.CreateScreen
import bbct.android.ui.details.EditScreen
import bbct.android.ui.list.ListScreen
import bbct.android.ui.navigation.AboutDestination
import bbct.android.ui.navigation.BaseballCardCreateDestination
import bbct.android.ui.navigation.BaseballCardEditDestination
import bbct.android.ui.navigation.BaseballCardListDestination
import bbct.android.ui.theme.AppTheme

@Composable
fun App(db: BaseballCardDatabase) {
    val navController = rememberNavController()

    AppTheme {
        NavHost(
            navController = navController,
            startDestination = BaseballCardListDestination,
        ) {
            composable<BaseballCardListDestination> {
                ListScreen(
                    navController,
                    db,
                )
            }
            composable<BaseballCardCreateDestination> {
                CreateScreen(
                    navController,
                    db,
                )
            }
            composable<BaseballCardEditDestination> { backStackEntry ->
                var destination: BaseballCardEditDestination = backStackEntry.toRoute()
                EditScreen(
                    navController,
                    db,
                    destination.cardId,
                )
            }
            composable<AboutDestination> {
                AboutScreen(
                    navController
                )
            }
        }
    }
}
