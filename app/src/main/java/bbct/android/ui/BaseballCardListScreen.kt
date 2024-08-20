package bbct.android.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import bbct.android.R
import bbct.android.data.BaseballCard
import bbct.android.data.BaseballCardDatabase
import bbct.android.ui.navigation.BaseballCardDetailsDestination

@Composable
fun BaseballCardListScreen(navController: NavController, db: BaseballCardDatabase) {
    Scaffold(
        topBar = { TopBar(navController) },
        floatingActionButton = { AddCardButton(navController) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        val cards = db.baseballCardDao.baseballCards.collectAsState(initial = emptyList())
        BaseballCardList(navController, cards.value, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun BaseballCardList(
    navController: NavController,
    cards: List<BaseballCard>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(
            items = cards,
            key = { card: BaseballCard -> card._id!! }
        ) { card: BaseballCard ->
            BaseballCardRow(card, navController)
        }
    }
}

@Composable
fun BaseballCardRow(card: BaseballCard, navController: NavController) {
    Row(modifier = Modifier.clickable { navController.navigate(BaseballCardDetailsDestination.route) }) {
        Checkbox(checked = false, onCheckedChange = { /*TODO*/ })
        Text(text = card.brand, modifier = Modifier.weight(0.2f))
        Text(text = "${card.year}", modifier = Modifier.weight(0.15f))
        Text(text = card.number, modifier = Modifier.weight(0.15f))
        Text(text = card.playerName, modifier = Modifier.weight(0.5f))
    }
}

@Composable
fun AddCardButton(navController: NavController) {
    FloatingActionButton(onClick = { navController.navigate(BaseballCardDetailsDestination.route) }) {
        Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.add_menu))
    }
}
