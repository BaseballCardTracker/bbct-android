package bbct.android.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import bbct.android.R
import bbct.android.data.BaseballCardDatabase

@Composable
fun BaseballCardFilterScreen(
    navController: NavController,
    db: BaseballCardDatabase
) {
    Scaffold(
        topBar = {
            TopBar(
                navigationIcon = { BackIcon(navController) },
                actions = { OverflowMenu(navController = navController) }
            )
        },
        floatingActionButton = { ApplyFilterButton(navController) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        BaseballCardFilter(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun BaseballCardFilter(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row {
            Checkbox(
                checked = false,
                onCheckedChange = { /*TODO*/ })
            TextField(
                label = { Text(text = stringResource(id = R.string.brand)) },
                value = "",
                onValueChange = { /*TODO*/ })
        }
        Row {
            Checkbox(
                checked = false,
                onCheckedChange = { /*TODO*/ })
            TextField(
                label = { Text(text = stringResource(id = R.string.year)) },
                value = "",
                onValueChange = { /*TODO*/ })
        }
        Row {
            Checkbox(
                checked = false,
                onCheckedChange = { /*TODO*/ })
            TextField(
                label = { Text(text = stringResource(id = R.string.number)) },
                value = "",
                onValueChange = { /*TODO*/ })
        }
        Row {
            Checkbox(
                checked = false,
                onCheckedChange = { /*TODO*/ })
            TextField(
                label = { Text(text = stringResource(id = R.string.player_name)) },
                value = "",
                onValueChange = { /*TODO*/ })
        }
        Row {
            Checkbox(
                checked = false,
                onCheckedChange = { /*TODO*/ })
            TextField(
                label = { Text(text = stringResource(id = R.string.team)) },
                value = "",
                onValueChange = { /*TODO*/ })
        }
    }
}

@Composable
fun ApplyFilterButton(navController: NavController) {
    FloatingActionButton(onClick = { /* TODO */ }) {
        Icon(
            Icons.Default.Check,
            contentDescription = stringResource(id = R.string.filter_menu)
        )
    }
}
