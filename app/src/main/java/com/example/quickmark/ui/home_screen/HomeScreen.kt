package com.example.quickmark.ui.home_screen

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.quickmark.ui.theme.Dimen
import com.example.quickmark.R
import com.example.quickmark.domain.file_handling.FileHelper
import com.example.quickmark.domain.navigation.Screen
import com.example.quickmark.ui.add_note_dialog.AddNoteActivity
import com.example.quickmark.ui.theme.CustomTypography
import com.example.quickmark.ui.theme.LocalCustomColorPalette

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navController: NavHostController
) {
    val markdownFiles by viewModel.markdownFiles.collectAsStateWithLifecycle(emptyList())
    Log.d("#TAG", "HomeScreen: $markdownFiles")
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    Scaffold(
        backgroundColor = LocalCustomColorPalette.current.background,
        topBar =
        {
            TopAppBar(
                modifier = Modifier.padding(top = Dimen.Padding.statusBar),
                backgroundColor = LocalCustomColorPalette.current.background,
                contentColor = LocalCustomColorPalette.current.primary,
                title =
                {
                    Text(text = stringResource(id = R.string.app_name), style =  CustomTypography.heading)
                },

                navigationIcon = {
                    IconButton(
                        onClick =
                        {
//                            scope.launch { scaffoldState.drawerState.open() }
                        }
                    )
                    { Icon(imageVector = Icons.Rounded.Menu, contentDescription = null) }
                },
                actions = {
                    IconButton(
                        onClick =
                        {
                            navController.navigate(Screen.Setting.route)
                        }
                    )
                    { Icon(imageVector = Icons.Rounded.Settings, contentDescription = null) }
                },

                elevation = Dimen.TopBar.elevation
            )

        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(end = Dimen.Padding.p4, bottom = Dimen.Padding.p5),
                onClick = {
                    val intent = Intent(context, AddNoteActivity::class.java)
                    launcher.launch(intent)
                },
                backgroundColor = MaterialTheme.colors.secondary
            )
            {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colors.background
                )
            }
        },
    )
    {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = Dimen.Padding.p4)
        ){
            //region - Flashcard List View -
            items(markdownFiles.sortedByDescending { it.lastModified() })
            {
                NoteListItem(
                    item = NoteSelectionListItem(it,false),
                    onClick =
                    {
                        if (false)
//                            viewModel.onItemClick(it)
                        else {
//                            searchAppBarViewModel.updateSearchAppBarState(SearchAppBarState.CLOSED)
//                            navController.navigate(route = Screen.AddEditFlashcard.navArg(parentId, it.flashcard.id))
                        }
                    },
                    onLongClick = {
//                        viewModel.onItemLongClick(it)
                    }
                )
            }
        }
        //endregion
    }
}









    // Camera permission state
//
//    val storagePermissionState = rememberPermissionState(
//        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//    )
//
//    Scaffold(Modifier.padding(top = Dimen.Padding.statusBar)) {
//        when (storagePermissionState.status) {
//            // If the camera permission is granted, then show screen with the feature enabled
//            PermissionStatus.Granted -> {
//                Text("Camera permission Granted")
//            }
//            is PermissionStatus.Denied -> {
//                Column {
//                    val textToShow = if ((storagePermissionState.status as PermissionStatus.Denied).shouldShowRationale) {
//                        // If the user has denied the permission but the rationale can be shown,
//                        // then gently explain why the app requires this permission
//                        "The camera is important for this app. Please grant the permission."
//                    } else {
//                        // If it's the first time the user lands on this feature, or the user
//                        // doesn't want to be asked again for this permission, explain that the
//                        // permission is required
//                        "Camera permission required for this feature to be available. " +
//                                "Please grant the permission"
//                    }
//                    Text(textToShow)
//                    Button(onClick = { storagePermissionState.launchPermissionRequest() }) {
//                        Text("Request permission")
//                    }
//                }
//            }
//        }
//    }
//}

