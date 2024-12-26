package com.xectrone.quickmark.domain.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams


import android.widget.Toast
import com.android.billingclient.api.ConsumeParams

class BillingManager(
    private val context: Context,
    private val onPurchaseComplete: (Purchase) -> Unit
) {
    private lateinit var billingClient: BillingClient
    private var isBillingClientReady = false

    fun setupBillingClient() {
        billingClient = BillingClient.newBuilder(context)
            .setListener { billingResult, purchases ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                    for (purchase in purchases) {
                        handlePurchase(purchase)
                    }
                } else {
                    Toast.makeText(context, "Purchase failed: ${billingResult.debugMessage}", Toast.LENGTH_LONG).show()
                }
            }
            .enablePendingPurchases()
            .build()

        connectBillingClient()
    }

    private fun connectBillingClient() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    isBillingClientReady = true
                    Toast.makeText(context, "Billing client setup completed.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Billing setup failed: ${billingResult.debugMessage}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onBillingServiceDisconnected() {
                isBillingClientReady = false
                Toast.makeText(context, "Billing service disconnected. Retrying...", Toast.LENGTH_SHORT).show()
                retryConnection()
            }
        })
    }

    private fun retryConnection() {
        // Retry connection with exponential backoff
        val maxRetries = 3
        var retryCount = 0
        val retryInterval = 2000L // 2 seconds

        while (!isBillingClientReady && retryCount < maxRetries) {
            try {
                Thread.sleep(retryInterval)
                connectBillingClient()
                retryCount++
            } catch (e: InterruptedException) {
                e.printStackTrace()
                break
            }
        }

        if (!isBillingClientReady) {
            Toast.makeText(context, "Failed to reconnect to billing service after $maxRetries attempts.", Toast.LENGTH_LONG).show()
        }
    }

    fun purchase(activity: Activity, productId: String) {
        if (!isBillingClientReady) {
            Toast.makeText(context, "Billing client is not ready. Cannot initiate purchase.", Toast.LENGTH_SHORT).show()
            return
        }

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(productId)
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                )
            )
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && productDetailsList.isNotEmpty()) {
                val productDetails = productDetailsList[0]
                val billingFlowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(
                        listOf(
                            BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetails)
                                .build()
                        )
                    )
                    .build()
                billingClient.launchBillingFlow(activity, billingFlowParams)
            } else {
                Toast.makeText(context, "Failed to query product details: ${billingResult.debugMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            // Consume the purchase if it is a consumable item
            val consumeParams = ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
            billingClient.consumeAsync(consumeParams) { billingResult, purchaseToken ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(context, "Purchase consumed successfully.", Toast.LENGTH_SHORT).show()
                    onPurchaseComplete(purchase)
                } else {
                    Toast.makeText(context, "Failed to consume purchase: ${billingResult.debugMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun release() {
        if (::billingClient.isInitialized) {
            billingClient.endConnection()
        }
    }
}

