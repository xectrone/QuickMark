package com.example.quickmark.ui.settings_screen

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
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
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.quickmark.R
import com.example.quickmark.ui.add_note_dialog.add_note_dialog_shortcut.addHomeScreenShortcut
import com.example.quickmark.ui.theme.Constants
import com.example.quickmark.ui.theme.CustomShape
import com.example.quickmark.ui.theme.CustomTypography
import com.example.quickmark.ui.theme.Dimen
import com.example.quickmark.ui.theme.LocalCustomColorPalette
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsScreen(
    viewModel: SettingViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val directoryPath by viewModel.directoryPath
    val editDirectoryPath by viewModel.editDirectoryPath

    LaunchedEffect(true){
        viewModel.getDirectoryPath()
    }

    //region - Directory Picker Launcher -
    val dirPickerLauncher = rememberLauncherForActivityResult(
        contract = PermissibleOpenDocumentTreeContract(true),
        onResult = { maybeUri ->
            maybeUri?.let { uri ->
                val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                if (checkUriPersisted(context.contentResolver, uri)) {
                    context.contentResolver.releasePersistableUriPermission(uri, flags)
                }
                context.contentResolver.takePersistableUriPermission(uri, flags)
                val path = getFilePath(uri)
                scope.launch {
                    if (path != null) {
                        viewModel.setDirectoryPath(path)
                        viewModel.onSaveDirectoryPathClick()
                    }
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
                title =
                {
                    Text(text = "Settings", style =  CustomTypography.h2)
                },

                navigationIcon = {
                    IconButton(
                        onClick =
                        {
                            navController.navigateUp()
                        }
                    )
                    { Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = Constants.Labels.BACK) }
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
            //region - Directory Path -
            Text(
                modifier = Modifier.padding(bottom = Dimen.Padding.p2),
                text = "Directory Path",
                style = CustomTypography.titleSecondary,
                color = LocalCustomColorPalette.current.primary
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimen.Padding.p2),
                value = directoryPath,
                onValueChange = { viewModel.setDirectoryPath(it) },
                trailingIcon ={
                    if(editDirectoryPath)
                        IconButton(onClick = { viewModel.onSaveDirectoryPathClick()}) {
                            Icon(painter = painterResource(id = R.drawable.round_save_24),
                                contentDescription = Constants.Labels.SettingScreen.SAVE_DIRECTORY_PATH)
                        }
                    else
                        IconButton(onClick = { viewModel.onEditDirectoryPathClick() }) {
                            Icon(
                                imageVector = Icons.Rounded.Edit,
                                contentDescription = Constants.Labels.SettingScreen.EDIT_DIRECTORY_PATH
                            )
                        }

                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = LocalCustomColorPalette.current.surface,
                    textColor = LocalCustomColorPalette.current.secondary,
                    unfocusedBorderColor = LocalCustomColorPalette.current.tertiary,
                    focusedBorderColor = LocalCustomColorPalette.current.secondary
                ),
                enabled = editDirectoryPath,
                shape = CustomShape.textField,

            )

            Button(
                modifier = Modifier
                    .padding(bottom = Dimen.Padding.p2)
                    .align(Alignment.End),
                shape = CustomShape.button,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = LocalCustomColorPalette.current.accentSecondary,
                    contentColor = LocalCustomColorPalette.current.background
                ),
                onClick = {
                    dirPickerLauncher.launch(Uri.EMPTY)
                }

            ) {
                Text(
                    text = "Choose Directory"
                )
            }
            //endregion


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
                    Text(
                        text = "Create"
                    )
                }
            }




        }

    }











}

//region - URI handler -
fun checkUriPersisted(contentResolver: ContentResolver, uri: Uri): Boolean {
    return contentResolver.persistedUriPermissions.any { perm -> perm.uri == uri }
}

class PermissibleOpenDocumentTreeContract(
    private val write: Boolean = false,
) : ActivityResultContracts.OpenDocumentTree() {
    override fun createIntent(context: Context, input: Uri?): Intent {
        val intent = super.createIntent(context, input)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        if (write) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        intent.addFlags(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)

        return intent
    }
}

fun getFilePath(uri: Uri): String? {
    if ("content" == uri.scheme) {
        // Retrieve the document ID from the URI
        val documentId = DocumentsContract.getTreeDocumentId(uri)
        val split = documentId.split(":").toTypedArray()
        if (split.isNotEmpty()) {
            // Extract the volume and path from the document ID
            val volume = split[0]
            val path = if (split.size > 1) {
                split[1]
            } else {
                ""
            }
            val rootPath = Environment.getExternalStorageDirectory().absolutePath

            return if (volume == "primary")
                "$rootPath/$path"
            else
                null
        }
    }
    return null
}

//endregion