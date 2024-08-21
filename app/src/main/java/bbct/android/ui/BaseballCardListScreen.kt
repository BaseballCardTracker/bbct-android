package bbct.android.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import bbct.android.R
import bbct.android.data.BaseballCard
import bbct.android.data.BaseballCardDatabase
import bbct.android.ui.navigation.BaseballCardDetailsDestination
import kotlinx.coroutines.launch

data class BaseballCardSelectedState(var card: BaseballCard, var selected: Boolean)

@Composable
fun BaseballCardListScreen(
    navController: NavController,
    db: BaseballCardDatabase,
) {
    val scope = rememberCoroutineScope()
    val cards by db.baseballCardDao.baseballCards.collectAsState(initial = emptyList())
    val stateList by remember {
        derivedStateOf {
            cards.map { card ->
                BaseballCardSelectedState(
                    card,
                    false
                )
            }.toMutableStateList()
        }
    }
    val isAnySelected by remember {
        derivedStateOf {
            stateList.any { it.selected }
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                actions = {
                    Crossfade(isAnySelected, label = "") { target ->
                        Row {
                            if (target) {
                                ListMenu(
                                    onDeleteCards = { scope.launch { deleteCards(db, stateList) } }
                                )
                            } else {
                                MainMenu(navController)
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = { AddCardButton(navController) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        BaseballCardList(
            navController = navController,
            cards = stateList,
            onCardChanged = { index, card -> stateList[index] = card },
            contentPadding = innerPadding
        )
    }
}

@Composable
fun BaseballCardList(
    navController: NavController,
    cards: List<BaseballCardSelectedState>,
    onCardChanged: (Int, BaseballCardSelectedState) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        //Maybe include a top column to know what each column is?
        itemsIndexed(
            items = cards,
            key = { _, state -> state.card._id!! }
        ) { i, state ->
            BaseballCardRow(
                navController = navController,
                state = state,
                onSelectedChange = { onCardChanged(i, state.copy(selected = it)) }
            )
        }
    }
}

suspend fun deleteCards(db: BaseballCardDatabase, cards: List<BaseballCardSelectedState>) {
    db.baseballCardDao.deleteBaseballCards(cards.filter { it.selected }.map { it.card })
}

@Composable
fun BaseballCardRow(
    navController: NavController,
    state: BaseballCardSelectedState,
    onSelectedChange: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { navController.navigate(BaseballCardDetailsDestination.route) }
    ) {
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
