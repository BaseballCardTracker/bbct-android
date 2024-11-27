package bbct.android.ui.details

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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import bbct.android.R
import bbct.android.data.BaseballCard
import bbct.android.data.BaseballCardDatabase
import bbct.android.ui.BackIcon
import bbct.android.ui.OverflowMenu
import bbct.android.ui.TopBar
import bbct.android.ui.components.AutoComplete
import bbct.android.ui.components.Select
import bbct.android.ui.navigation.AboutDestination
import kotlinx.coroutines.launch

data class BaseballCardState(
    var id: Long? = null,
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
        card._id,
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
            _id = id,
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
fun BaseballCardCreateScreen(
    navController: NavController,
    db: BaseballCardDatabase,
) {
    val viewModel: BaseballCardDetailsViewModel = viewModel()

    Scaffold(
        topBar = {
            TopBar(
                navigationIcon = { BackIcon(navController = navController) },
                title = {
                    Text(
                        stringResource(
                            id = R.string.bbct_title,
                            stringResource(id = R.string.create_card_title)
                        )
                    )
                },
                actions = { OverflowMenu(onAbout = { navController.navigate(AboutDestination) }) },
            )
        },
        floatingActionButton = {
            CreateCardButton(
                db,
                viewModel.baseballCardState,
                viewModel.errors,
                viewModel::validate,
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) { innerPadding ->
        BaseballCardDetails(
            state = viewModel.baseballCardState,
            db = db,
            errors = viewModel.errors,
            onValidate = viewModel::validate,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
fun BaseballCardEditScreen(
    navController: NavController,
    db: BaseballCardDatabase,
    cardId: Long,
) {
    val viewModel: BaseballCardDetailsViewModel = viewModel()

    LaunchedEffect(cardId) {
        val card = db.baseballCardDao.getBaseballCard(cardId)
        viewModel.baseballCardState.value = BaseballCardState(card)
    }

    Scaffold(
        topBar = {
            TopBar(
                navigationIcon = { BackIcon(navController = navController) },
                title = {
                    Text(
                        stringResource(
                            id = R.string.bbct_title,
                            stringResource(id = R.string.edit_card_title)
                        )
                    )
                },
                actions = { OverflowMenu(onAbout = { navController.navigate(AboutDestination) }) },
            )
        },
        floatingActionButton = {
            UpdateCardButton(
                navController,
                db,
                viewModel.baseballCardState,
                viewModel.errors,
                viewModel::validate,
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) { innerPadding ->
        BaseballCardDetails(
            state = viewModel.baseballCardState,
            db = db,
            errors = viewModel.errors,
            onValidate = viewModel::validate,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
fun BaseballCardDetails(
    state: MutableState<BaseballCardState>,
    db: BaseballCardDatabase,
    errors: MutableState<BaseballCardDetailsErrors>,
    onValidate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val brands = db.baseballCardDao.brands.collectAsState(initial = emptyList())
    var playerNames = db.baseballCardDao.playerNames.collectAsState(initial = emptyList())
    var teams = db.baseballCardDao.teams.collectAsState(initial = emptyList())

    val conditions = stringArrayResource(R.array.condition)
    val positions = stringArrayResource(R.array.positions)

    val scrollState = rememberScrollState()
    val textFieldModifier = Modifier.fillMaxWidth()

    Column(
        modifier = modifier
            .focusGroup()
            .padding(12.dp)
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
            label = { Text(stringResource(id = R.string.condition)) },
            options = conditions,
            selected = state.value.condition,
            onSelectedChange = {
                state.value = state.value.copy(condition = it)
                onValidate()
            },
            isError = !errors.value.brand.isValid,
            modifier = textFieldModifier,
        )
        AutoComplete(
            label = { Text(text = stringResource(id = R.string.brand)) },
            options = brands.value,
            value = state.value.brand,
            onValueChange = {
                state.value = state.value.copy(brand = it)
                onValidate()
            },
            isError = !errors.value.brand.isValid,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = textFieldModifier,
        )
        TextField(
            label = { Text(text = stringResource(id = R.string.year)) },
            value = state.value.year,
            onValueChange = {
                state.value = state.value.copy(year = it)
                onValidate()
            },
            isError = !errors.value.year.isValid,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            modifier = textFieldModifier,
        )
        TextField(
            label = { Text(text = stringResource(id = R.string.number)) },
            value = state.value.number,
            onValueChange = {
                state.value = state.value.copy(number = it)
                onValidate()
            },
            isError = !errors.value.number.isValid,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = textFieldModifier,
        )
        TextField(
            label = { Text(text = stringResource(id = R.string.value)) },
            value = state.value.value,
            onValueChange = {
                state.value = state.value.copy(value = it)
                onValidate()
            },
            isError = !errors.value.value.isValid,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            modifier = textFieldModifier,
        )
        TextField(
            label = { Text(text = stringResource(id = R.string.quantity)) },
            value = state.value.quantity,
            onValueChange = {
                state.value = state.value.copy(quantity = it)
                onValidate()
            },
            isError = !errors.value.quantity.isValid,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            modifier = textFieldModifier,
        )
        AutoComplete(
            label = { Text(text = stringResource(id = R.string.player_name)) },
            options = playerNames.value,
            value = state.value.playerName,
            onValueChange = {
                state.value = state.value.copy(playerName = it)
                onValidate()
            },
            isError = !errors.value.playerName.isValid,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = textFieldModifier,
        )
        AutoComplete(
            label = { Text(text = stringResource(id = R.string.team)) },
            options = teams.value,
            value = state.value.team,
            onValueChange = {
                state.value = state.value.copy(team = it)
                onValidate()
            },
            isError = !errors.value.team.isValid,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = textFieldModifier,
        )
        Select(
            label = { Text(stringResource(id = R.string.player_position)) },
            options = positions,
            selected = state.value.position,
            onSelectedChange = {
                state.value = state.value.copy(position = it)
                onValidate()
            },
            isError = !errors.value.brand.isValid,
            modifier = textFieldModifier,
        )
    }
}

@Composable
fun CreateCardButton(
    db: BaseballCardDatabase,
    state: MutableState<BaseballCardState>,
    errors: MutableState<BaseballCardDetailsErrors>,
    onValidate: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    FloatingActionButton(onClick = {
        onValidate()
        if (errors.value.isValid) {
            scope.launch {
                createCard(
                    db,
                    state.value
                )
                state.value = BaseballCardState()
            }
        }
    }) {
        Icon(
            Icons.Default.Check,
            contentDescription = stringResource(id = R.string.save_menu)
        )
    }
}

suspend fun createCard(
    db: BaseballCardDatabase,
    cardState: BaseballCardState,
) {
    db.baseballCardDao.insertBaseballCard(cardState.toBaseballCard())
}

@Composable
fun UpdateCardButton(
    navController: NavController,
    db: BaseballCardDatabase,
    state: MutableState<BaseballCardState>,
    errors: MutableState<BaseballCardDetailsErrors>,
    onValidate: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    FloatingActionButton(onClick = {
        onValidate()
        if (errors.value.isValid) {
            scope.launch {
                updateCard(
                    db,
                    state.value
                )
                navController.popBackStack()
            }
        }
    }) {
        Icon(
            Icons.Default.Check,
            contentDescription = stringResource(id = R.string.save_menu)
        )
    }
}

suspend fun updateCard(
    db: BaseballCardDatabase,
    cardState: BaseballCardState,
) {
    db.baseballCardDao.updateBaseballCard(cardState.toBaseballCard())
}
