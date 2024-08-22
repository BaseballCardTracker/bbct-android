package bbct.android.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import bbct.android.R
import bbct.android.ui.navigation.AboutDestination
import bbct.android.ui.navigation.BaseballCardFilterDestination

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBar(
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) },
        actions = actions,
        navigationIcon = navigationIcon,
    )
}

@Composable
fun BackIcon(navController: NavController) {
    IconButton(onClick = { navController.popBackStack() }) {
        Icon(
            Icons.AutoMirrored.Default.ArrowBack,
            contentDescription = stringResource(id = R.string.back)
        )
    }
}

@Composable
fun ListMenu(
    navController: NavController,
    isAnySelected: Boolean,
    onDeleteCards: () -> Unit,
    onSelectAll: () -> Unit,
) {
    Crossfade(isAnySelected, label = "") { target ->
        Row {
            if (target) {
                SelectedMenu(
                    onDeleteCards = onDeleteCards,
                    onSelectAll = onSelectAll,
                )
            } else {
                MainMenu(navController)
            }
        }
    }
}

@Composable
fun MainMenu(navController: NavController) {
    IconButton(onClick = { navController.navigate(BaseballCardFilterDestination.route) }) {
        Icon(Icons.Default.Search, contentDescription = stringResource(id = R.string.filter_menu))
    }
    OverflowMenu(navController)
}

@Composable
fun OverflowMenu(navController: NavController) {
    var showMenu by remember { mutableStateOf(false) }

    IconButton(onClick = { showMenu = !showMenu }) {
        Icon(Icons.Default.MoreVert, contentDescription = stringResource(id = R.string.more))
    }
    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { showMenu = false }
    ) {
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.about_menu)) },
            onClick = { navController.navigate(AboutDestination.route) })
    }
}

@Composable
fun SelectedMenu(onDeleteCards: () -> Unit, onSelectAll: () -> Unit) {
    IconButton(onClick = onDeleteCards) {
        Icon(
            Icons.Default.Delete,
            contentDescription = stringResource(id = R.string.delete_menu)
        )
    }
    IconButton(onClick = onSelectAll) {
        Icon(
            painterResource(id = R.drawable.select_all),
            contentDescription = stringResource(id = R.string.select_all_menu)
        )
    }
}
