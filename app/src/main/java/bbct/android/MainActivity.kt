package bbct.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import bbct.android.data.BaseballCardDatabase
import bbct.android.ui.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = BaseballCardDatabase.getInstance(this)
        setContent {
            App()
        }
    }
}
