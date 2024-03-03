package com.example.quickmark.ui.home_screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.encrypsy.ui.theme.Dimen
import com.example.quickmark.domain.file_handling.FileHelper
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val fileHelper = FileHelper("/storage/emulated/0/0.MEDIA/qn")
    var showDialog by remember { mutableStateOf(false) }

    val fileList = fileHelper.getAllMarkdownFiles()

    Scaffold(
        Modifier.padding(top = Dimen.Padding.statusBar),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDialog = true

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
        }


    )
    {



        AddNoteDialog(
            showDialog = showDialog,
            fileHelper = fileHelper,
            onDismiss = { showDialog = false },
            onDone = { title ->
                fileHelper.saveMarkdownFile(
                    fileName = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(Date()) + ".md",
                    content = title
                )
                showDialog = false
            }
        )






        LazyColumn{
            //region - Flashcard List View -
            items(fileList)
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

