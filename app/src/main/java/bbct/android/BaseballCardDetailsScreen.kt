package bbct.android

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun BaseballCardDetailsScreen() {
    Column {
        Row {
            Checkbox(checked = false, onCheckedChange = { /* TODO */ })
            Text(text = stringResource(id = R.string.autograph_label))
        }
        Row {
            Text(text = stringResource(id = R.string.condition_label))
        }
        Row {
            Text(text = stringResource(id = R.string.brand_label))
            TextField(value = "Topps", onValueChange = { /* TODO */ })
        }
        Row {
            Text(text = stringResource(id = R.string.year_label))
            TextField(value = "1991", onValueChange = { /* TODO */ })
        }
        Row {
            Text(text = stringResource(id = R.string.number_label))
            TextField(value = "278", onValueChange = { /* TODO */ })
        }
        Row {
            Text(text = stringResource(id = R.string.value_label))
            TextField(value = "500", onValueChange = { /* TODO */ })
        }
        Row {
            Text(text = stringResource(id = R.string.count_label))
            TextField(value = "1", onValueChange = { /* TODO */ })
        }
        Row {
            Text(text = stringResource(id = R.string.player_name_label))
            TextField(value = "Alex Fernandez", onValueChange = { /* TODO */ })
        }
        Row {
            Text(text = stringResource(id = R.string.team_label))
            TextField(value = "White Sox", onValueChange = { /* TODO */ })
        }
        Row {
            Text(text = stringResource(id = R.string.player_position_label))
        }
    }
}
