package com.xectrone.quickmark

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.xectrone.flashup.ui.theme.QuickMarkTheme
import com.xectrone.quickmark.domain.billing.BillingManager
import com.xectrone.quickmark.domain.navigation.HomeScreenNavGraph


class MainActivity : ComponentActivity() {
    private lateinit var billingManager: BillingManager

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_QuickMark)

        billingManager = BillingManager(
            context = this,
            onPurchaseComplete = {
                Toast.makeText(this, "Thank you for your support!", Toast.LENGTH_LONG).show()
            }
        )
        billingManager.setupBillingClient()

        setContent {
            QuickMarkTheme {
                val navController = rememberAnimatedNavController()
                HomeScreenNavGraph(navController = navController, billingManager = billingManager)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        billingManager.release()
    }
}






