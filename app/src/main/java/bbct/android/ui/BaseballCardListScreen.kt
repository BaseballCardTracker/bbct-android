package bbct.android.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import bbct.android.R
import bbct.android.data.BaseballCard
import bbct.android.data.BaseballCardDatabase
import bbct.android.ui.navigation.BaseballCardDetailsDestination

data class BaseballCardSelectedState(var card: BaseballCard, var selected: Boolean)

@Composable
fun BaseballCardListScreen(navController: NavController, db: BaseballCardDatabase) {
    Scaffold(
        topBar = { TopBar(actions = { MainMenu(navController) }) },
        floatingActionButton = { AddCardButton(navController) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        val cards = db.baseballCardDao.baseballCards.collectAsState(initial = emptyList())
        val stateList by remember {
            derivedStateOf {
                mutableStateListOf(
                    *(cards.value.map { card ->
                        BaseballCardSelectedState(
                            card,
                            false
                        )
                    }).toTypedArray()
                )
            }
        }
        BaseballCardList(navController, stateList, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun BaseballCardList(
    navController: NavController,
    cards: MutableList<BaseballCardSelectedState>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(
            items = cards,
            key = { i, state -> state.card._id!! }
        ) { i, state ->
            BaseballCardRow(
                navController = navController,
                state = state,
                onSelectedChange = { cards[i] = state.copy(selected = it) }
            )
        }
    }
}

@Composable
fun BaseballCardRow(
    navController: NavController,
    state: BaseballCardSelectedState,
    onSelectedChange: (Boolean) -> Unit,
) {
    Row(modifier = Modifier.clickable { navController.navigate(BaseballCardDetailsDestination.route) }) {
        Checkbox(
            checked = state.selected,
            onCheckedChange = onSelectedChange,
        )
        Text(text = state.card.brand, modifier = Modifier.weight(0.2f))
        Text(text = "${state.card.year}", modifier = Modifier.weight(0.15f))
        Text(text = state.card.number, modifier = Modifier.weight(0.15f))
        Text(text = state.card.playerName, modifier = Modifier.weight(0.5f))
    }
}

@Composable
fun AddCardButton(navController: NavController) {
    FloatingActionButton(onClick = { navController.navigate(BaseballCardDetailsDestination.route) }) {
        Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.add_menu))
    }
}
