@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.xectrone.quickmark.ui.donation_screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.xectrone.quickmark.data.DataStore.saveSelectedDirectoryUri
import com.xectrone.quickmark.domain.billing.BillingManager
import com.xectrone.quickmark.ui.add_note_dialog.add_note_dialog_shortcut.addHomeScreenShortcut
import com.xectrone.quickmark.ui.theme.Constants
import com.xectrone.quickmark.ui.theme.Dimen
import com.xectrone.quickmark.ui.utility.CustomOutlineButton
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DonationScreen(
    navController: NavController,
    billingManager: BillingManager
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = { Text(text = "", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = Constants.Labels.BACK)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(Dimen.Padding.p4),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Card at the top
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Column(
                    modifier = Modifier.padding(Dimen.Padding.p3),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = Dimen.Padding.p3),
                        text = "SUPPORT",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Divider(
                        modifier = Modifier.padding(vertical = 10.dp),
                        color = MaterialTheme.colorScheme.outline,
                        thickness = 1.dp
                    )
                    Text(
                        modifier = Modifier.padding(vertical = Dimen.Padding.p3),
                        text = Constants.DONATION_MSG,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            // Main donation button
            CustomOutlineButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimen.Padding.p4),
                text1 = "DONATION",
                text2 = "💌",
                color = MaterialTheme.colorScheme.primary,
            ) {
                scope.launch {
                    billingManager.purchase(
                        activity = context as Activity,
                        productId = Constants.Donation.DONATION
                    )
                }
            }
            // Row of two smaller buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CustomOutlineButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = Dimen.Padding.p3),
                    text1 = "THANK YOU",
                    text2 = "☕",
                    color = MaterialTheme.colorScheme.primary,
                ) {
                    scope.launch {
                        billingManager.purchase(
                            activity = context as Activity,
                            productId = Constants.Donation.THANKYOU
                        )
                    }
                }
                CustomOutlineButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = Dimen.Padding.p3),
                    text1 = "SUPPORT",
                    text2 = "🎁",
                    color = MaterialTheme.colorScheme.primary,
                ) {
                    scope.launch {
                        billingManager.purchase(
                            activity = context as Activity,
                            productId = Constants.Donation.SUPPORT
                        )
                    }
                }
            }
        }
    }
}
