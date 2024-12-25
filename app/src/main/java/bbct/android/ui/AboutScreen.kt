package bbct.android.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import bbct.android.BuildConfig
import bbct.android.R

@Composable
fun AboutScreen(
    navController: NavController,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = {
                    Text(
                        text = stringResource(
                            id = R.string.bbct_title,
                            stringResource(id = R.string.about_title)
                        )
                    )
                },
                navigationIcon = { BackButton(navController = navController) })
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        About(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun About(modifier: Modifier = Modifier) {
    //https://stackoverflow.com/a/69549929/2781626
    Column(modifier = modifier.padding(12.dp)) {
        Row {
            Column {
                Image(
                    painter = painterResource(id = R.drawable.baseball),
                    contentDescription = stringResource(
                        id = R.string.app_name
                    ),
                )
            }
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    fontSize = 26.sp
                )
                Text(
                    text = stringResource(
                        id = R.string.version_text,
                        BuildConfig.VERSION_NAME
                    )
                )
            }
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Text(text = stringResource(id = R.string.copyright))
        Spacer(modifier = Modifier.padding(10.dp))
        Text(text = stringResource(id = R.string.about))
        Spacer(modifier = Modifier.padding(10.dp))
        Text(text = stringResource(id = R.string.email))
        Spacer(modifier = Modifier.padding(10.dp))
        Text(text = stringResource(id = R.string.website))
        Spacer(modifier = Modifier.padding(10.dp))
        Text(text = stringResource(id = R.string.license))
    }
}
