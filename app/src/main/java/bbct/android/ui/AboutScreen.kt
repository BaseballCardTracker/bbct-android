package bbct.android.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import bbct.android.R
import bbct.android.data.BaseballCardDatabase

@Composable
fun AboutScreen(navController: NavController, db: BaseballCardDatabase) {
    Scaffold(
        topBar = { TopBar(navigationIcon = { BackIcon(navController = navController) }) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        About(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun About(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = stringResource(id = R.string.app_name))
        Text(text = stringResource(id = R.string.version_text))
        Text(text = stringResource(id = R.string.copyright))
        Text(text = stringResource(id = R.string.about))
        Text(text = stringResource(id = R.string.email))
        Text(text = stringResource(id = R.string.website))
        Text(text = stringResource(id = R.string.license))
    }
}
