package bbct.android.ui

import android.content.Context
import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import bbct.android.BuildConfig
import bbct.android.R
import bbct.android.data.BaseballCardCsvFileReader
import bbct.android.data.BaseballCardDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBar(
    navigationIcon: @Composable () -> Unit = {},
    title: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = title,
        actions = actions,
        navigationIcon = navigationIcon,
    )
}

@Composable
fun BackIcon(navController: NavController) {
    IconButton(onClick = { navController.popBackStack() }) {
        Icon(
            Icons.AutoMirrored.Default.ArrowBack,
            contentDescription = stringResource(id = R.string.back)
        )
    }
}

@Composable
fun CloseButton(onClose: () -> Unit) {
    IconButton(onClick = onClose) {
        Icon(
            Icons.Default.Close,
            contentDescription = stringResource(id = R.string.close)
        )
    }
}

@Composable
fun ListMenu(
    isAnySelected: Boolean,
    onFilterCards: () -> Unit,
    onAbout: () -> Unit,
    onDeleteCards: () -> Unit,
    onSelectAll: () -> Unit,
) {
    Crossfade(
        isAnySelected,
        label = ""
    ) { target ->
        Row {
            if (target) {
                SelectedMenu(
                    onDeleteCards = onDeleteCards,
                    onSelectAll = onSelectAll,
                )
            } else {
                MainMenu(
                    onFilterCards = onFilterCards,
                    onAbout = onAbout
                )
            }
        }
    }
}

@Composable
fun MainMenu(
    onFilterCards: () -> Unit,
    onAbout: () -> Unit
) {
    IconButton(onClick = onFilterCards) {
        Icon(
            Icons.Default.Search,
            contentDescription = stringResource(id = R.string.filter_menu)
        )
    }
    OverflowMenu(onAbout)
}

@Composable
fun OverflowMenu(onAbout: () -> Unit) {
    var showMenu by remember { mutableStateOf(false) }

    IconButton(onClick = { showMenu = !showMenu }) {
        Icon(
            Icons.Default.MoreVert,
            contentDescription = stringResource(id = R.string.more)
        )
    }
    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { showMenu = false }
    ) {
        if (BuildConfig.DEBUG) {
            val context = LocalContext.current
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.add_cards)) },
                onClick = { addCards(context) }
            )
        }

        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.about_menu)) },
            onClick = onAbout
        )
    }
}

@Composable
fun SelectedMenu(
    onDeleteCards: () -> Unit,
    onSelectAll: () -> Unit
) {
    IconButton(onClick = onDeleteCards) {
        Icon(
            Icons.Default.Delete,
            contentDescription = stringResource(id = R.string.delete_menu)
        )
    }
    IconButton(onClick = onSelectAll) {
        Icon(
            painterResource(id = R.drawable.select_all),
            contentDescription = stringResource(id = R.string.select_all_menu)
        )
    }
}

fun addCards(context: Context) {
    val db = BaseballCardDatabase.getInstance(
        context = context,
        dbName = "bbct.db"
    )
    val csvReader = BaseballCardCsvFileReader(
        context.assets.open("cards.csv"),
        hasColHeaders = true
    )

    CoroutineScope(Dispatchers.IO).launch {
        while (csvReader.hasNextBaseballCard()) {
            val card = csvReader.getNextBaseballCard()
            Log.d(
                "Add Cards Menu",
                "Adding card: $card"
            )
            db.baseballCardDao.insertBaseballCard(card)
        }
    }
}
