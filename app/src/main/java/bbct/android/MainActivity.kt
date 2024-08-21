package bbct.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import bbct.android.data.BaseballCardDatabase
import bbct.android.ui.App
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = BaseballCardDatabase.getInstance(this, BaseballCardDatabase.DATABASE_NAME)
        setContent {
            App(db)
        }
    }
}
