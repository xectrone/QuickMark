package com.xectrone.quickmark.ui.home_screen

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
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
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.xectrone.quickmark.ui.theme.Dimen
import com.xectrone.quickmark.R
import com.xectrone.quickmark.domain.navigation.Screen
import com.xectrone.quickmark.ui.theme.Constants
import com.xectrone.quickmark.ui.theme.CustomTypography
import com.xectrone.quickmark.ui.theme.LocalCustomColorPalette
import com.xectrone.quickmark.ui.utility.MessageScreen

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navController: NavHostController
) {
    val markdownFilesList by viewModel.markdownFilesList.collectAsStateWithLifecycle(emptyList())
    val selectionMode by viewModel.selectionMode
    val directoryUri by viewModel.directoryUri

    val isExpanded by viewModel.isExpanded

    LaunchedEffect(key1 = navController.currentBackStackEntry) {
        viewModel.observeDirectoryUri()
        viewModel.observeSortOption()
    }

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

//                navigationIcon = {
//                    IconButton(
//                        onClick =
//                        {
////                            scope.launch { scaffoldState.drawerState.open() }
//                        }
//                    )
//                    { Icon(imageVector = Icons.Rounded.Menu, contentDescription = Constants.Labels.HomeScreen.MENU) }
//                },
                actions = @androidx.compose.runtime.Composable {
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
                    IconButton(onClick = {viewModel.showMenu()})
                    {
                        Icon(painter = painterResource(id = R.drawable.round_sort_24), contentDescription = Constants.Labels.HomeScreen.CLEAR)
                        DropdownMenu(expanded = isExpanded, onDismissRequest = { viewModel.hideMenu() })
                        {
                            DropdownMenuItem(onClick = { viewModel.onSort(SortOptions.nameASC) })
                            { Text(text = Constants.Labels.SortOptions.nameASC) }

                            DropdownMenuItem(onClick = { viewModel.onSort(SortOptions.nameDESC) })
                            { Text(text = Constants.Labels.SortOptions.nameDESC) }

                            DropdownMenuItem(onClick = { viewModel.onSort(SortOptions.lastModifiedASC) })
                            { Text(text = Constants.Labels.SortOptions.lastModifiedASC) }

                            DropdownMenuItem(onClick = { viewModel.onSort(SortOptions.lastModifiedDESC) })
                            { Text(text = Constants.Labels.SortOptions.lastModifiedDESC) }
                        }

                    }

                    IconButton(
                        onClick =
                        {
                            viewModel.onClear()
                            navController.navigate(Screen.Setting.route)
                        }
                    )
                    { Icon(imageVector = Icons.Rounded.Settings, contentDescription = Constants.Labels.HomeScreen.SETTINGS) }
                },

                elevation = Dimen.TopBar.elevation
            )

        },
        floatingActionButton = {
            if (directoryUri != null)
            {
                FloatingActionButton(
                    modifier = Modifier.padding(end = Dimen.Padding.p4, bottom = Dimen.Padding.p5),
                    onClick = {
                        viewModel.onClear()
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
        if (directoryUri != null)
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = Dimen.Padding.p4)
            ){
                //region - List View -
                items(items = markdownFilesList, key ={it.fileName})
                {
                    NoteListItem(
                        modifier = Modifier.animateItemPlacement(
                            animationSpec = tween(durationMillis = 600)
                        ),
                        item = NoteSelectionListItem(fileName = it.fileName, fileContent = it.fileContent, fileUri = it.fileUri, lastModified = it.lastModified, isSelected = it.isSelected),
                        onClick =
                        {

                            if (selectionMode)
                                viewModel.onItemClick(it)
                            else
                                navController.navigate(route = Screen.AddEditNote.navArg(it.fileUri))
                        },
                        onLongClick = { viewModel.onItemLongClick(it) },
                    )
                }
                //endregion
            }
        else
            MessageScreen(message = Constants.SELECT_DIRECTORY_PATH_MSG)
    }
}

