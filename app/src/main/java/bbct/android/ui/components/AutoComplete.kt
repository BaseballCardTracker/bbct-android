package bbct.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup

// Credit: https://stackoverflow.com/a/79016933/1440565
@Composable
fun AutoComplete(
    label: @Composable () -> Unit,
    options: List<String>,
    value: TextFieldValue, // I use here TextFieldValue instead of String to be able to control the cursor position
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    completionThreshold: Int = 1, // Number of chars that the user must type before completion start
    dropDownBackgroundColor: Color = Color.White,
    dropDownItemHeight: Dp = 48.dp
) {
    var filteredOpts by remember { mutableStateOf(emptyList<String>()) }

    // Keep track of text field size to be able to position
    // the popup correctly
    var textFieldSize by remember { mutableStateOf(IntSize.Zero) }

    Box {
        TextField(
            label = label,
            value = value,
            onValueChange = {
                onValueChange(it)
                filteredOpts = options.filter { option ->
                    it.text.length >= completionThreshold &&
                        option
                            .lowercase()
                            .contains(it.text.lowercase())
                }
            },
            isError = isError,
            keyboardOptions = keyboardOptions,
            modifier = modifier.onSizeChanged {
                textFieldSize = it
            }
        )

        if (filteredOpts.isNotEmpty()) {
            Popup(
                offset = IntOffset(
                    x = 0,
                    y = textFieldSize.height
                ),
                onDismissRequest = {
                    filteredOpts = emptyList()
                }
            ) {
                Column(
                    modifier = Modifier
                        .background(color = dropDownBackgroundColor)
                        .shadow(elevation = 2.dp)
                ) {
                    filteredOpts.forEach { option ->

                        // We use here box instead of DropdownMenuItem because
                        // we don't want it to fill the max width like DropdownMenuItem
                        // does. See the source code of DropdownMenuItem for more info
                        Box(
                            modifier = Modifier
                                .size(
                                    width = with(LocalDensity.current) { textFieldSize.width.toDp() },
                                    height = dropDownItemHeight
                                )
                                .clickable {
                                    onValueChange(
                                        TextFieldValue(
                                            text = option,

                                            // put the cursor at the end of the selected text
                                            selection = TextRange(option.length)
                                        )
                                    )
                                    filteredOpts = emptyList()
                                },
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(text = option)
                        }
                    }
                }
            }
        }
    }
}
