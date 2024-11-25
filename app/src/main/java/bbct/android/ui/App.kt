package bbct.android.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import bbct.android.data.BaseballCardDatabase
import bbct.android.ui.details.BaseballCardCreateScreen
import bbct.android.ui.details.BaseballCardEditScreen
import bbct.android.ui.filter.BaseballCardFilterScreen
import bbct.android.ui.list.BaseballCardListScreen
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
            startDestination = BaseballCardListDestination,
        ) {
            composable<BaseballCardListDestination> {
                BaseballCardListScreen(
                    navController,
                    db,
                )
            }
            composable<BaseballCardCreateDestination> {
                BaseballCardCreateScreen(
                    navController,
                    db,
                )
            }
            composable<BaseballCardEditDestination> { backStackEntry ->
                var destination: BaseballCardEditDestination = backStackEntry.toRoute()
                BaseballCardEditScreen(
                    navController,
                    db,
                    destination.cardId,
                )
            }
            composable<BaseballCardFilterDestination> {
                BaseballCardFilterScreen(
                    navController,
                )
            }
            composable<AboutDestination> {
                AboutScreen(
                    navController,
                    db
                )
            }
        }
    }
}
