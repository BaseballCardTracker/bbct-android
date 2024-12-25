package bbct.android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import bbct.android.data.BaseballCardDatabase
import bbct.android.ui.App
import com.google.android.gms.ads.MobileAds
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = BaseballCardDatabase.getInstance(
            this,
            BaseballCardDatabase.DATABASE_NAME
        )
        setContent {
            App(db)
        }
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            Log.d(
                "MainActivity",
                "Initializing Google Mobile Ads SDK..."
            )
            MobileAds.initialize(this@MainActivity) {}
        }
    }
}
