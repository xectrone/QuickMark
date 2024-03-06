package com.example.quickmark.ui.settings_screen

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quickmark.R
import com.example.quickmark.data.datastore.DataStoreManager
import com.example.quickmark.domain.navigation.Screen
import com.example.quickmark.ui.theme.CustomTypography
import com.example.quickmark.ui.theme.Dimen
import com.example.quickmark.ui.theme.LocalCustomColorPalette
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsScreen(
//    viewModel: SettingViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val isDirectoryPicked = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val dataStoreManager = remember{ DataStoreManager.getInstance(context)}
    val directoryPathFlow = dataStoreManager.directoryPathFlow
    var directoryPath by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(directoryPathFlow){
        directoryPathFlow.collect{
            directoryPath = it
        }
    }

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
                        dataStoreManager.saveDirectoryPath(path)
                        isDirectoryPicked.value = true
                    }
                }
            }
        }
    )


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = LocalCustomColorPalette.current.background,
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(top = Dimen.Padding.statusBar),
                backgroundColor = LocalCustomColorPalette.current.background,
                contentColor = LocalCustomColorPalette.current.primary,
                title =
                {
                    Text(text = "Settings", style =  CustomTypography.heading)
                },

                navigationIcon = {
                    IconButton(
                        onClick =
                        {
                            navController.navigateUp()
                        }
                    )
                    { Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null) }
                },
                elevation = Dimen.TopBar.elevation
            )

        }


    ) {
        Column(
            modifier = Modifier
                .padding(top = Dimen.Padding.statusBar)
        ) {
            Text(text = "Directory")
            TextField(
                value = directoryPath.toString(), onValueChange = {it}
            )
            Button(onClick = { dirPickerLauncher.launch(Uri.EMPTY) }) {
                Text(text = "Choose Directory")
            }


        }

    }











}

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