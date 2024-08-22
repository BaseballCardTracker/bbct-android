package bbct.android.ui

import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import bbct.android.R
import bbct.android.data.BaseballCard
import bbct.android.data.BaseballCardDatabase
import kotlinx.coroutines.launch

data class BaseballCardState(
    var autographed: Boolean = false,
    var condition: String = "",
    var brand: String = "",
    var year: String = "",
    var number: String = "",
    var value: String = "",
    var quantity: String = "",
    var playerName: String = "",
    var team: String = "",
    var position: String = "",
) {
    constructor(card: BaseballCard) : this(
        card.autographed,
        card.condition,
        card.brand,
        card.year.toString(),
        card.number,
        (card.value / 100.0).toString(),
        card.quantity.toString(),
        card.playerName,
        card.team,
        card.position
    )

    fun toBaseballCard(): BaseballCard {
        return BaseballCard(
            autographed = autographed,
            condition = condition,
            brand = brand,
            year = year.toInt(),
            number = number,
            value = (value.toDouble() * 100).toInt(),
            quantity = quantity.toInt(),
            playerName = playerName,
            team = team,
            position = position
        )
    }
}

@Composable
fun BaseballCardCreateScreen(navController: NavController, db: BaseballCardDatabase) {
    val state = remember { mutableStateOf(BaseballCardState()) }

    Scaffold(
        topBar = {
            TopBar(
                navigationIcon = { BackIcon(navController = navController) },
                actions = { OverflowMenu(navController) },
            )
        },
        floatingActionButton = { CreateCardButton(db, state) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        BaseballCardDetails(state, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun BaseballCardEditScreen(navController: NavController, db: BaseballCardDatabase, cardId: Long) {
    val state = remember { mutableStateOf(BaseballCardState()) }
    LaunchedEffect(cardId) {
        val card = db.baseballCardDao.getBaseballCard(cardId)
        state.value = BaseballCardState(card)
    }

    Scaffold(
        topBar = {
            TopBar(
                navigationIcon = { BackIcon(navController = navController) },
                actions = { OverflowMenu(navController) },
            )
        },
        floatingActionButton = { UpdateCardButton(db, state) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        BaseballCardDetails(state, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun BaseballCardDetails(
    state: MutableState<BaseballCardState>,
    modifier: Modifier = Modifier,
) {
    val conditions = stringArrayResource(R.array.condition)
    val positions = stringArrayResource(R.array.positions)

    val scrollState = rememberScrollState()
    val textFieldModifier = Modifier.fillMaxWidth()

    Column(
        modifier = modifier
            .focusGroup()
            .padding(12.dp)
            .imePadding()
            .verticalScroll(scrollState),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = state.value.autographed,
                onCheckedChange = { state.value = state.value.copy(autographed = it) },
            )
            Text(text = stringResource(id = R.string.autographed))
        }
        Select(
            labelText = stringResource(id = R.string.condition),
            options = conditions,
            selected = state.value.condition,
            onSelectedChange = { state.value = state.value.copy(condition = it) },
            modifier = textFieldModifier,
        )
        TextField(
            label = { Text(text = stringResource(id = R.string.brand)) },
            value = state.value.brand,
            onValueChange = { state.value = state.value.copy(brand = it) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = textFieldModifier,
        )
        TextField(
            label = { Text(text = stringResource(id = R.string.year)) },
            value = state.value.year,
            onValueChange = { state.value = state.value.copy(year = it) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            modifier = textFieldModifier,
        )
        TextField(
            label = { Text(text = stringResource(id = R.string.number)) },
            value = state.value.number,
            onValueChange = { state.value = state.value.copy(number = it) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = textFieldModifier,
        )
        TextField(
            label = { Text(text = stringResource(id = R.string.value)) },
            value = state.value.value,
            onValueChange = { state.value = state.value.copy(value = it) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            modifier = textFieldModifier,
        )
        TextField(
            label = { Text(text = stringResource(id = R.string.quantity)) },
            value = state.value.quantity,
            onValueChange = { state.value = state.value.copy(quantity = it) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            modifier = textFieldModifier,
        )
        TextField(
            label = { Text(text = stringResource(id = R.string.player_name)) },
            value = state.value.playerName,
            onValueChange = { state.value = state.value.copy(playerName = it) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = textFieldModifier,
        )
        TextField(
            label = { Text(text = stringResource(id = R.string.team)) },
            value = state.value.team,
            onValueChange = { state.value = state.value.copy(team = it) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = textFieldModifier,
        )
        Select(
            labelText = stringResource(id = R.string.player_position),
            options = positions,
            selected = state.value.position,
            onSelectedChange = { state.value = state.value.copy(position = it) },
            modifier = textFieldModifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Select(
    labelText: String,
    options: Array<String>,
    selected: String,
    onSelectedChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        TextField(
            label = { Text(text = labelText) },
            readOnly = true,
            value = selected,
            onValueChange = { /* Do nothing */ },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = modifier.menuAnchor()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        onSelectedChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun CreateCardButton(
    db: BaseballCardDatabase,
    state: MutableState<BaseballCardState>,
) {
    val scope = rememberCoroutineScope()
    FloatingActionButton(onClick = { scope.launch { createCard(db, state) } }) {
        Icon(Icons.Default.Check, contentDescription = stringResource(id = R.string.save_menu))
    }
}

suspend fun createCard(db: BaseballCardDatabase, cardState: MutableState<BaseballCardState>) {
    val newCard = cardState.value.toBaseballCard()
    db.baseballCardDao.insertBaseballCard(newCard)
    cardState.value = BaseballCardState()
}

@Composable
fun UpdateCardButton(
    db: BaseballCardDatabase,
    state: MutableState<BaseballCardState>,
) {
    val scope = rememberCoroutineScope()
    FloatingActionButton(onClick = { scope.launch { updateCard(db, state) } }) {
        Icon(Icons.Default.Check, contentDescription = stringResource(id = R.string.save_menu))
    }
}

suspend fun updateCard(db: BaseballCardDatabase, cardState: MutableState<BaseballCardState>) {
    val newCard = cardState.value.toBaseballCard()
    db.baseballCardDao.updateBaseballCard(newCard)
    cardState.value = BaseballCardState()
}
