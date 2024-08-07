package bbct.android

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import bbct.android.ui.theme.BbctTheme

@Composable
fun App() {
    BbctTheme {
        Scaffold(topBar = { TopBar() }, modifier = Modifier.fillMaxSize()) { innerPadding ->
            BaseballCardList(modifier = Modifier.padding(innerPadding))
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBar() {
    TopAppBar(title = { Text("BBCT") })
}
