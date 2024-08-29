package bbct.android.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import bbct.android.R
import bbct.android.data.BaseballCardDatabase


data class BaseballCardFilterState(
    var brand: String = "",
    var year: String = "",
    var number: String = "",
    var playerName: String = "",
    var team: String = "",
)

@Composable
fun BaseballCardFilterScreen(
    navController: NavController,
    db: BaseballCardDatabase,
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
        val filterState = remember { mutableStateOf(BaseballCardFilterState()) }
        BaseballCardFilter(
            filterState,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun BaseballCardFilter(
    filterState: MutableState<BaseballCardFilterState>,
    modifier: Modifier = Modifier,
) {
    val textFieldModifier = Modifier.fillMaxWidth()

    Column(modifier = modifier.padding(12.dp)) {
        Row {
            Checkbox(
                checked = false,
                onCheckedChange = { /*TODO*/ })
            TextField(
                label = { Text(text = stringResource(id = R.string.brand)) },
                value = filterState.value.brand,
                onValueChange = { filterState.value = filterState.value.copy(brand = it) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = textFieldModifier,
            )
        }
        Row {
            Checkbox(
                checked = false,
                onCheckedChange = { /*TODO*/ })
            TextField(
                label = { Text(text = stringResource(id = R.string.year)) },
                value = filterState.value.year,
                onValueChange = { filterState.value = filterState.value.copy(year = it) },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
                modifier = textFieldModifier,
            )
        }
        Row {
            Checkbox(
                checked = false,
                onCheckedChange = { /*TODO*/ })
            TextField(
                label = { Text(text = stringResource(id = R.string.number)) },
                value = filterState.value.number,
                onValueChange = { filterState.value = filterState.value.copy(number = it) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = textFieldModifier,
            )
        }
        Row {
            Checkbox(
                checked = false,
                onCheckedChange = { /*TODO*/ })
            TextField(
                label = { Text(text = stringResource(id = R.string.player_name)) },
                value = filterState.value.playerName,
                onValueChange = { filterState.value = filterState.value.copy(playerName = it) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = textFieldModifier,
            )
        }
        Row {
            Checkbox(
                checked = false,
                onCheckedChange = { /*TODO*/ })
            TextField(
                label = { Text(text = stringResource(id = R.string.team)) },
                value = filterState.value.team,
                onValueChange = { filterState.value = filterState.value.copy(team = it) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = textFieldModifier,
            )
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
