package bbct.android.ui.components

import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoComplete(
    label: @Composable () -> Unit,
    options: List<String>,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
) {
    var expanded by remember { mutableStateOf(false) }
    var filteredOpts by remember { mutableStateOf(options) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }) {
        TextField(
            label = label,
            value = value,
            onValueChange = {
                onValueChange(it)
                filteredOpts = options.filter { option ->
                    option
                        .lowercase()
                        .contains(it.lowercase())
                }
                expanded = true
            },
            isError = isError,
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = modifier.menuAnchor(),
            keyboardOptions = keyboardOptions,
        )
        if (!filteredOpts.isEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }) {
                filteredOpts.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            onValueChange(option)
                            filteredOpts = emptyList()
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
