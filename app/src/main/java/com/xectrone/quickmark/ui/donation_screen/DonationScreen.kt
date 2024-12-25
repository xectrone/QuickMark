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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.xectrone.quickmark.data.DataStore.saveSelectedDirectoryUri
import com.xectrone.quickmark.domain.billing.BillingManager
import com.xectrone.quickmark.ui.add_note_dialog.add_note_dialog_shortcut.addHomeScreenShortcut
import com.xectrone.quickmark.ui.theme.Constants
import com.xectrone.quickmark.ui.theme.CustomShape
import com.xectrone.quickmark.ui.theme.CustomTypography
import com.xectrone.quickmark.ui.theme.Dimen
import com.xectrone.quickmark.ui.theme.LocalCustomColorPalette
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
        backgroundColor = LocalCustomColorPalette.current.background,

        //region - Top Bar -
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(top = Dimen.Padding.statusBar),
                backgroundColor = LocalCustomColorPalette.current.background,
                contentColor = LocalCustomColorPalette.current.primary,
                title = { Text(text = "", style = CustomTypography.h2, textAlign = TextAlign.Center) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = Constants.Labels.BACK)
                    }
                },
                elevation = Dimen.TopBar.elevation
            )
        }
        //endregion
    ) {
        Column(
            modifier = Modifier
                .padding(Dimen.Padding.p4),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            //region - Note Folder -

            Text(
                modifier = Modifier.padding(bottom = Dimen.Padding.p4),
                text = "SUPPORT",
                style = CustomTypography.h2,
                textAlign = TextAlign.Center,
                color = LocalCustomColorPalette.current.primary
            )

            Text(
                modifier = Modifier.padding(bottom = Dimen.Padding.p4),
                text = Constants.DONATION_MSG,
                style = CustomTypography.titleSecondary,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                color = LocalCustomColorPalette.current.primary
            )

            CustomOutlineButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimen.Padding.p3),
                text = "Donation\n$7"
            ) {
                scope.launch {
                    billingManager.purchase(
                        activity = context as Activity,
                        productId = Constants.Donation.DONATION
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimen.Padding.p3),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CustomOutlineButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = Dimen.Padding.p3),
                    text = "ThankYou\n$1"
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
                    text = "Support\n$3"
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
