package bbct.android

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import bbct.android.ui.theme.BbctTheme

@Composable
fun App() {
    val navController = rememberNavController()

    BbctTheme {
        Scaffold(
            topBar = { TopBar() },
            floatingActionButton = { AddCardButton(navController) },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = BaseballCardListDestination.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = BaseballCardListDestination.route) { BaseballCardListDestination.screen() }
                composable(route = BaseballCardDetailsDestination.route) { BaseballCardDetailsDestination.screen() }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBar() {
    TopAppBar(title = { Text(stringResource(id = R.string.app_name)) }, actions = { MainMenu() })
}

@Composable
fun MainMenu() {
    IconButton(onClick = { /*TODO*/ }) {
        Icon(Icons.Default.Search, contentDescription = stringResource(id = R.string.filter_menu))
    }
}

@Composable
fun AddCardButton(navController: NavController) {
    FloatingActionButton(onClick = { navController.navigate(BaseballCardDetailsDestination.route) }) {
        Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.add_menu))
    }
}
