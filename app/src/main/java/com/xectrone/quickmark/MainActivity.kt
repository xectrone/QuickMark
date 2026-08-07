package com.xectrone.quickmark

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import com.xectrone.quickmark.domain.billing.BillingManager
import com.xectrone.quickmark.domain.navigation.HomeScreenNavGraph
import androidx.compose.material3.*
import androidx.navigation.compose.rememberNavController
import com.xectrone.quickmark.ui.theme.QuickMarkTheme
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {
    private lateinit var billingManager: BillingManager

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // Setting Material 3 theme (defined in themes.xml)
        setTheme(R.style.Theme_QuickMark)
        // Initialize BillingManager
        billingManager = BillingManager(
            context = this,
            onPurchaseComplete = {
                Toast.makeText(this, "Thank you for your support!", Toast.LENGTH_LONG).show()
            }
        )
        // Billing is initialized lazily when the user actually starts a purchase flow.
        // This avoids noisy background toasts during normal app use.
        
        // Handle share intent
        val sharedText = handleShareIntent(intent)
        
        setContent {
            QuickMarkTheme {
                val navController = rememberNavController()
                HomeScreenNavGraph(
                    navController = navController, 
                    billingManager = billingManager,
                    sharedText = sharedText
                )
            }
        }
    }

    private fun handleShareIntent(intent: Intent): String? {
        return when (intent.action) {
            Intent.ACTION_SEND -> {
                if (intent.type == "text/plain") {
                    intent.getStringExtra(Intent.EXTRA_TEXT)
                } else null
            }
            else -> null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        billingManager.release()
    }
}








