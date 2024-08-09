package bbct.android

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

@Composable
fun BaseballCardDetailsScreen(context: Context = LocalContext.current) {
    val resources = context.resources
    val conditions = resources.getStringArray(R.array.condition)
    val positions = resources.getStringArray(R.array.positions)

    Column {
        Row {
            Checkbox(checked = false, onCheckedChange = { /* TODO */ })
            Text(text = stringResource(id = R.string.autograph_label))
        }
        Row {
            Select(
                labelText = stringResource(id = R.string.condition_label),
                options = conditions,
                selected = conditions[0],
                onSelectedChange = { /* TODO */ })
        }
        Row {
            TextField(
                label = { Text(text = stringResource(id = R.string.brand_label)) },
                value = "Topps",
                onValueChange = { /* TODO */ })
        }
        Row {
            TextField(
                label = { Text(text = stringResource(id = R.string.year_label)) },
                value = "1991",
                onValueChange = { /* TODO */ }
            )
        }
        Row {
            TextField(
                label = { Text(text = stringResource(id = R.string.number_label)) },
                value = "278",
                onValueChange = { /* TODO */ })
        }
        Row {
            TextField(
                label = { Text(text = stringResource(id = R.string.value_label)) },
                value = "500",
                onValueChange = { /* TODO */ })
        }
        Row {
            TextField(
                label = { Text(text = stringResource(id = R.string.count_label)) },
                value = "1",
                onValueChange = { /* TODO */ })
        }
        Row {
            TextField(
                label = { Text(text = stringResource(id = R.string.player_name_label)) },
                value = "Alex Fernandez",
                onValueChange = { /* TODO */ })
        }
        Row {
            TextField(
                label = { Text(text = stringResource(id = R.string.team_label)) },
                value = "White Sox",
                onValueChange = { /* TODO */ })
        }
        Row {
            Select(
                labelText = stringResource(id = R.string.player_position_label),
                options = positions,
                selected = positions[0],
                onSelectedChange = { /* TODO */ })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Select(
    labelText: String,
    options: Array<String>,
    selected: String,
    onSelectedChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        TextField(
            label = { Text(text = labelText) },
            readOnly = true,
            value = selected,
            onValueChange = { /* TODO */ },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        onSelectedChange(option)
                        expanded = false
                    })
            }
        }
    }
}
