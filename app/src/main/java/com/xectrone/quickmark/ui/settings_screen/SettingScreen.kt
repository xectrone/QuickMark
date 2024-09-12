package com.xectrone.quickmark.ui.settings_screen

import android.annotation.SuppressLint
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
import androidx.navigation.NavController
import com.xectrone.quickmark.data.DataStore.saveSelectedDirectoryUri
import com.xectrone.quickmark.ui.add_note_dialog.add_note_dialog_shortcut.addHomeScreenShortcut
import com.xectrone.quickmark.ui.theme.Constants
import com.xectrone.quickmark.ui.theme.CustomShape
import com.xectrone.quickmark.ui.theme.CustomTypography
import com.xectrone.quickmark.ui.theme.Dimen
import com.xectrone.quickmark.ui.theme.LocalCustomColorPalette
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    //region - Directory Picker Launcher -
    val dirPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree(),
        onResult = { maybeUri ->
            maybeUri?.let { uri ->
                val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                context.contentResolver.takePersistableUriPermission(uri, flags)

                scope.launch {
                    val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(uri, DocumentsContract.getTreeDocumentId(uri))
                    saveSelectedDirectoryUri(context, childrenUri)
                }
            }
        }
    )
    //endregion

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = LocalCustomColorPalette.current.background,

        //region - Top Bar -
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(top = Dimen.Padding.statusBar),
                backgroundColor = LocalCustomColorPalette.current.background,
                contentColor = LocalCustomColorPalette.current.primary,
                title = { Text(text = "Settings", style = CustomTypography.h2) },
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
                .padding(Dimen.Padding.p4)
        ) {
            //region - Note Folder -
            Text(
                modifier = Modifier.padding(bottom = Dimen.Padding.p2),
                text = "Note Folder",
                style = CustomTypography.titleSecondary,
                color = LocalCustomColorPalette.current.primary
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(bottom = Dimen.Padding.p2),
                    text = "Select Note Folder",
                    style = CustomTypography.textPrimary,
                    color = LocalCustomColorPalette.current.secondary
                )

                Button(
                    modifier = Modifier
                        .padding(bottom = Dimen.Padding.p2),
                    shape = CustomShape.button,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = LocalCustomColorPalette.current.accentSecondary,
                        contentColor = LocalCustomColorPalette.current.background
                    ),
                    onClick = {
                        dirPickerLauncher.launch(Uri.EMPTY)
                    }
                ) {
                    Text(text = "Choose Directory")
                }
            }
            //endregion

            //region - Shortcut -

            Divider(
                modifier = Modifier.padding(vertical = Dimen.Padding.p2),
                color = LocalCustomColorPalette.current.quaternary
            )

            Text(
                modifier = Modifier.padding(vertical = Dimen.Padding.p2),
                text = "Shortcut",
                style = CustomTypography.titleSecondary,
                color = LocalCustomColorPalette.current.primary
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(bottom = Dimen.Padding.p2),
                    text = "Add Shortcut to Home Screen",
                    style = CustomTypography.textPrimary,
                    color = LocalCustomColorPalette.current.secondary
                )
                Button(
                    modifier = Modifier
                        .padding(bottom = Dimen.Padding.p2),
                    shape = CustomShape.button,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = LocalCustomColorPalette.current.accentSecondary,
                        contentColor = LocalCustomColorPalette.current.background
                    ),
                    onClick = {
                        addHomeScreenShortcut(context)
                    }
                ) {
                    Text(text = "Create")
                }
            }

            //endregion
        }
    }
}

