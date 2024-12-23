package bbct.android.ui.list

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import bbct.android.BuildConfig
import bbct.android.R
import bbct.android.data.BaseballCard
import bbct.android.data.BaseballCardDatabase
import bbct.android.ui.ListMenu
import bbct.android.ui.TopBar
import bbct.android.ui.filter.BaseballCardFilterScreen
import bbct.android.ui.filter.BaseballCardFilterState
import bbct.android.ui.navigation.AboutDestination
import bbct.android.ui.navigation.BaseballCardCreateDestination
import bbct.android.ui.navigation.BaseballCardEditDestination
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import kotlinx.coroutines.launch

data class BaseballCardSelectedState(
    var card: BaseballCard,
    var selected: Boolean,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseballCardListScreen(
    navController: NavController,
    db: BaseballCardDatabase,
) {
    val scope = rememberCoroutineScope()
    val viewModel: BaseballCardListViewModel =
        viewModel(factory = BaseballCardListViewModelFactory(db.baseballCardDao))
    val cards by viewModel.baseballCards.collectAsStateWithLifecycle(initialValue = emptyList())
    val stateList by remember {
        derivedStateOf {
            cards
                .map { card ->
                    BaseballCardSelectedState(
                        card,
                        false
                    )
                }
                .toMutableStateList()
        }
    }
    val isAnySelected by remember {
        derivedStateOf {
            stateList.any { it.selected }
        }
    }
    val sheetState = rememberModalBottomSheetState()
    var showFilterBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(
                title = { Text(stringResource(id = R.string.app_name)) },
                actions = {
                    ListMenu(
                        isAnySelected = isAnySelected,
                        isFiltered = viewModel.isFiltered.value,
                        onFilterCards = { showFilterBottomSheet = true },
                        onClearFilter = { viewModel.applyFilter(BaseballCardFilterState()) },
                        onAbout = { navController.navigate(AboutDestination) },
                        onDeleteCards = {
                            scope.launch {
                                deleteCards(
                                    db,
                                    stateList
                                )
                            }
                        },
                        onSelectAll = { selectAll(stateList) },
                    )
                }
            )
        },
        floatingActionButton = { AddCardButton(navController) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            if (BuildConfig.APPLICATION_ID == "bbct.android") {
                AdBanner()
            }

            BaseballCardList(
                navController = navController,
                cards = stateList,
                onCardChanged = { index, card -> stateList[index] = card },
            )

            if (showFilterBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showFilterBottomSheet = false },
                    sheetState = sheetState
                ) {
                    BaseballCardFilterScreen(
                        onApplyFilter = { filter ->
                            viewModel.applyFilter(filter)
                            showFilterBottomSheet = false
                        },
                        onClose = { showFilterBottomSheet = false }
                    )
                }
            }
        }
    }
}

@Composable
private fun AdBanner(modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = context.getString(R.string.banner_ad_unit_id)
                loadAd(
                    AdRequest
                        .Builder()
                        .build()
                )
                adListener = object : AdListener() {
                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        Log.d(
                            "AdBanner",
                            "Failed to load ad: ${p0.message}"
                        )
                    }

                    override fun onAdLoaded() {
                        Log.d(
                            "AdBanner",
                            "Ad loaded"
                        )
                    }

                    override fun onAdClicked() {
                        Log.d(
                            "AdBanner",
                            "Ad clicked"
                        )
                    }

                    override fun onAdClosed() {
                        Log.d(
                            "AdBanner",
                            "Ad closed"
                        )
                    }

                    override fun onAdImpression() {
                        Log.d(
                            "AdBanner",
                            "Ad impression"
                        )
                    }

                    override fun onAdOpened() {
                        Log.d(
                            "AdBanner",
                            "Ad opened"
                        )
                    }

                    override fun onAdSwipeGestureClicked() {
                        Log.d(
                            "AdBanner",
                            "Ad swipe gesture clicked"
                        )
                    }
                }
            }
        }
    )
}

private suspend fun deleteCards(
    db: BaseballCardDatabase,
    cards: List<BaseballCardSelectedState>,
) {
    db.baseballCardDao.deleteBaseballCards(
        cards
            .filter { it.selected }
            .map { it.card })
}

private fun selectAll(stateList: SnapshotStateList<BaseballCardSelectedState>) {
    stateList.forEachIndexed { i, card ->
        stateList[i] = card.copy(selected = true)
    }
}

@Composable
fun BaseballCardList(
    navController: NavController,
    cards: List<BaseballCardSelectedState>,
    onCardChanged: (Int, BaseballCardSelectedState) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        itemsIndexed(
            items = cards,
            key = { _, state -> state.card._id!! }
        ) { i, state ->
            BaseballCardRow(
                navController = navController,
                state = state,
                onSelectedChange = {
                    onCardChanged(
                        i,
                        state.copy(selected = it)
                    )
                }
            )
        }
    }
}

@Composable
fun BaseballCardRow(
    navController: NavController,
    state: BaseballCardSelectedState,
    onSelectedChange: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            navController.navigate(BaseballCardEditDestination(state.card._id!!))
        }
    ) {
        Checkbox(
            checked = state.selected,
            onCheckedChange = onSelectedChange,
        )
        Text(
            text = state.card.brand,
            modifier = Modifier.weight(0.2f)
        )
        Text(
            text = "${state.card.year}",
            modifier = Modifier.weight(0.15f)
        )
        Text(
            text = state.card.number,
            modifier = Modifier.weight(0.15f)
        )
        Text(
            text = state.card.playerName,
            modifier = Modifier.weight(0.5f)
        )
    }
}

@Composable
fun AddCardButton(navController: NavController) {
    FloatingActionButton(onClick = { navController.navigate(BaseballCardCreateDestination) }) {
        Icon(
            Icons.Default.Add,
            contentDescription = stringResource(id = R.string.add_menu)
        )
    }
}
