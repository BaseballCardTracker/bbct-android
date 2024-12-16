package bbct.android.ui.filter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import bbct.android.R
import bbct.android.ui.CloseButton
import bbct.android.ui.TopBar

@Composable
fun BaseballCardFilterScreen(
    onApplyFilter: (BaseballCardFilterState) -> Unit,
    onClose: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                navigationIcon = { CloseButton(onClose) },
                title = {
                    Text(
                        stringResource(
                            id = R.string.filter_cards_title,
                        )
                    )
                },
            )
        },
        floatingActionButton = { ApplyFilterButton(onApplyFilter) },
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) { innerPadding ->
        val viewModel: BaseballCardFilterViewModel = viewModel()
        BaseballCardFilter(
            viewModel.filterState,
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

    Column(modifier = modifier) {
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
                value = if (filterState.value.year == -1) "" else filterState.value.year.toString(),
                onValueChange = {
                    val year = if (it == "") -1 else it.toInt()
                    filterState.value = filterState.value.copy(year = year)
                },
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
fun ApplyFilterButton(onApplyFilter: (BaseballCardFilterState) -> Unit) {
    val viewModel: BaseballCardFilterViewModel = viewModel()
    FloatingActionButton(onClick = { onApplyFilter(viewModel.filterState.value) }) {
        Icon(
            Icons.Default.Check,
            contentDescription = stringResource(id = R.string.filter_menu)
        )
    }
}
