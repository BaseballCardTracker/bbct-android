package bbct.android.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun AutoComplete(
    label: @Composable () -> Unit,
    options: List<String>,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    var expanded by remember { mutableStateOf(false) }
    var filteredOpts by remember { mutableStateOf(options) }

    Column(modifier = modifier.width(IntrinsicSize.Min)) {
        TextField(
            label = label,
            value = value,
            onValueChange = {
                onValueChange(it)
                filteredOpts = options.filter { option ->
                    option.lowercase().contains(it.lowercase())
                }
                expanded = true
            },
        )

        if (expanded && filteredOpts.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
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
