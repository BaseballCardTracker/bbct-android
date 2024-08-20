package bbct.android.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import bbct.android.R
import bbct.android.data.BaseballCardDatabase
import bbct.android.ui.navigation.BaseballCardDetailsDestination
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
                BaseballCardListDestination.screen(navController, db)
            }
            composable(route = BaseballCardDetailsDestination.route) {
                BaseballCardDetailsDestination.screen(navController, db)
            }
            composable(route = BaseballCardFilterDestination.route) {
                BaseballCardFilterDestination.screen(navController, db)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBar(navController: NavController, navigationIcon: @Composable () -> Unit = {}) {
    TopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) },
        actions = { MainMenu(navController) },
        navigationIcon = navigationIcon,
    )
}

@Composable
fun MainMenu(navController: NavController) {
    IconButton(onClick = { navController.navigate(BaseballCardFilterDestination.route) }) {
        Icon(Icons.Default.Search, contentDescription = stringResource(id = R.string.filter_menu))
    }
}
