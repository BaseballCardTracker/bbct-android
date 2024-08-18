package bbct.android.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import bbct.android.R
import bbct.android.data.BaseballCard
import bbct.android.data.BaseballCardDatabase
import bbct.android.ui.navigation.BaseballCardDetailsDestination

val cards = listOf(
    BaseballCard(
        _id = 1,
        autographed = false,
        condition = "Excellent",
        brand = "Topps",
        year = 1991,
        number = "278",
        value = 500,
        quantity = 1,
        playerName = "Alex Fernandez",
        team = "White Sox",
        position = "Pitcher"
    ),
    BaseballCard(
        _id = 2,
        autographed = true,
        condition = "Mint",
        brand = "Topps",
        year = 1974,
        number = "175",
        value = 1000,
        quantity = 1,
        playerName = "Bob Stanley",
        team = "Red Sox",
        position = "Pitcher"
    ),
    BaseballCard(
        _id = 3,
        autographed = false,
        condition = "Very Good",
        brand = "Topps",
        year = 1985,
        number = "201",
        value = 200,
        quantity = 1,
        playerName = "Vince Coleman",
        team = "Cardinals",
        position = "Left Field",
    ),
)

@Composable
fun BaseballCardListScreen(navController: NavController, db: BaseballCardDatabase) {
    Scaffold(
        topBar = { TopBar(navController) },
        floatingActionButton = { AddCardButton(navController) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        BaseballCardList(navController, cards, modifier = Modifier.padding(innerPadding))
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
