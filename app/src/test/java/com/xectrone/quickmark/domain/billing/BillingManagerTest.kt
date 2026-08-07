package com.xectrone.quickmark.domain.billing

import com.android.billingclient.api.BillingClient
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BillingManagerTest {
    @Test
    fun `transient billing responses stay silent`() {
        assertFalse(BillingManager.shouldShowUserFacingError(BillingClient.BillingResponseCode.USER_CANCELED))
        assertFalse(BillingManager.shouldShowUserFacingError(BillingClient.BillingResponseCode.SERVICE_DISCONNECTED))
        assertFalse(BillingManager.shouldShowUserFacingError(BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED))
    }

    @Test
    fun `real billing failures are surfaced`() {
        assertTrue(BillingManager.shouldShowUserFacingError(BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE))
        assertTrue(BillingManager.shouldShowUserFacingError(BillingClient.BillingResponseCode.BILLING_UNAVAILABLE))
    }
}
