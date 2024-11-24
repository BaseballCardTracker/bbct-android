package bbct.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import bbct.android.data.BaseballCardDatabase
import bbct.android.ui.AboutScreen
import bbct.android.ui.details.BaseballCardCreateScreen
import bbct.android.ui.details.BaseballCardEditScreen
import bbct.android.ui.filter.BaseballCardFilterScreen
import bbct.android.ui.list.BaseballCardListScreen

object BaseballCardListDestination {
    const val route = "baseball_card_list"
    val screen: @Composable (NavController, BaseballCardDatabase) -> Unit = { navController, db ->
        BaseballCardListScreen(
            navController,
            db,
        )
    }
}

object BaseballCardCreateDestination {
    const val route = "baseball_card_details"
    val screen: @Composable (NavController, BaseballCardDatabase) -> Unit = { navController, db ->
        BaseballCardCreateScreen(
            navController,
            db,
        )
    }
}

object BaseballCardEditDestination {
    const val route = "baseball_card_details/{cardId}"
    val screen: @Composable (NavController, BaseballCardDatabase, Long) -> Unit =
        { navController, db, cardId ->
            BaseballCardEditScreen(
                navController,
                db,
                cardId,
            )
        }

}

fun NavController.navigate(cardId: Long) {
    this.navigate("baseball_card_details/$cardId")
}

object BaseballCardFilterDestination {
    const val route = "baseball_card_filter"
    val screen: @Composable (NavController, BaseballCardDatabase) -> Unit = { navController, db ->
        BaseballCardFilterScreen(
            navController,
        )
    }
}

object AboutDestination {
    const val route = "about"
    val screen: @Composable (NavController, BaseballCardDatabase) -> Unit = { navController, db ->
        AboutScreen(
            navController,
            db,
        )
    }
}
