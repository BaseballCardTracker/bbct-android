package bbct.android

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

val cards = listOf(
    BaseballCard(
        id = 1,
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
        id = 2,
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
        id = 3,
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
fun BaseballCardList(modifier: Modifier) {
    LazyColumn(modifier = modifier) {
        items(items = cards, key = {card: BaseballCard -> card.id }) { card ->
            BaseballCardRow(card)
        }
    }
}

@Composable
fun BaseballCardRow(card: BaseballCard) {
    Text(text = "${card.playerName} - ${card.year}")
}
