package com.example.quickmark.ui.home_screen

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.quickmark.ui.theme.Dimen
import com.example.quickmark.R
import com.example.quickmark.domain.navigation.Screen
import com.example.quickmark.ui.theme.Constants
import com.example.quickmark.ui.theme.CustomTypography
import com.example.quickmark.ui.theme.LocalCustomColorPalette
import com.example.quickmark.ui.utility.MessageScreen

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navController: NavHostController
) {
    val markdownFilesList by viewModel.markdownFilesList.collectAsStateWithLifecycle(emptyList())
    val selectionMode by viewModel.selectionMode
    val directoryPath by viewModel.directoryPath.collectAsStateWithLifecycle("NONE")

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
                    Text(text = stringResource(id = R.string.app_name), style =  CustomTypography.h2)
                },

                navigationIcon = {
                    IconButton(
                        onClick =
                        {
//                            scope.launch { scaffoldState.drawerState.open() }
                        }
                    )
                    { Icon(imageVector = Icons.Rounded.Menu, contentDescription = Constants.Labels.HomeScreen.MENU) }
                },
                actions = {
                    if(selectionMode) {
                        IconButton(
                            onClick = { viewModel.onDelete() }
                        )
                        { Icon(imageVector = Icons.Rounded.Delete, contentDescription = Constants.Labels.HomeScreen.DELETE) }

                        IconButton(
                            onClick = { viewModel.onClear() }
                        )
                        { Icon(imageVector = Icons.Rounded.Clear, contentDescription = Constants.Labels.HomeScreen.CLEAR) }
                    }

                    IconButton(
                        onClick =
                        {
                            navController.navigate(Screen.Setting.route)
                        }
                    )
                    { Icon(imageVector = Icons.Rounded.Settings, contentDescription = Constants.Labels.HomeScreen.SETTINGS) }
                },

                elevation = Dimen.TopBar.elevation
            )

        },
        floatingActionButton = {
            if (directoryPath.isNotBlank())
            {
                FloatingActionButton(
                    modifier = Modifier.padding(end = Dimen.Padding.p4, bottom = Dimen.Padding.p5),
                    onClick = {
                        navController.navigate(Screen.AddEditNote.route)
                    },
                    backgroundColor = MaterialTheme.colors.secondary
                )
                {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = Constants.Labels.HomeScreen.ADD,
                        tint = MaterialTheme.colors.background
                    )
                }
            }
        }

    )
    {
        if (directoryPath.isNotBlank())
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = Dimen.Padding.p4)
            ){
                //region - List View -
                items(markdownFilesList.sortedByDescending { it.note.lastModified() })
                {
                    NoteListItem(
                        item = NoteSelectionListItem(it.note,it.isSelected),
                        onClick =
                        {
                            if (selectionMode)
                                viewModel.onItemClick(it)
                            else
                                navController.navigate(route = Screen.AddEditNote.navArg(it.note.nameWithoutExtension))
                        },
                        onLongClick = { viewModel.onItemLongClick(it) }
                    )
                }
                //endregion
            }
        else
            MessageScreen(message = Constants.SELECT_DIRECTORY_PATH_MSG)
    }
}

