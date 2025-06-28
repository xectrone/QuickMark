package com.xectrone.quickmark

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
        billingManager.setupBillingClient()
        setContent {
            QuickMarkTheme {
                val navController = rememberNavController()
                HomeScreenNavGraph(navController = navController, billingManager = billingManager)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        billingManager.release()
    }
}








