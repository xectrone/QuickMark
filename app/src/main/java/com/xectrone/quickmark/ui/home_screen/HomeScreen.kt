@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.xectrone.quickmark.ui.home_screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.xectrone.quickmark.ui.theme.Dimen
import com.xectrone.quickmark.R
import com.xectrone.quickmark.domain.navigation.Screen
import com.xectrone.quickmark.ui.theme.Constants
import com.xectrone.quickmark.ui.theme.LocalCustomColorPalette
import com.xectrone.quickmark.ui.utility.MessageScreen
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.statusBarsPadding

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

    val context = LocalContext.current
    var backPressHandled by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = navController.currentBackStackEntry) {
        viewModel.observeDirectoryUri()
        viewModel.observeSortOption()
    }

    LaunchedEffect(key1 = backPressHandled) {
        delay(2000)
        backPressHandled = true
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar =
        {
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.primary
                ),
                title =
                {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                actions = {
                    if(selectionMode) {
                        IconButton(
                            onClick = { viewModel.onDelete() }
                        ) {
                            Icon(imageVector = Icons.Rounded.Delete, contentDescription = Constants.Labels.HomeScreen.DELETE, tint = MaterialTheme.colorScheme.primary)
                        }
                        IconButton(
                            onClick = { viewModel.onClear() }
                        ) {
                            Icon(imageVector = Icons.Rounded.Clear, contentDescription = Constants.Labels.HomeScreen.CLEAR, tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    IconButton(onClick = {viewModel.showMenu()}) {
                        Icon(painter = painterResource(id = R.drawable.round_sort_24), contentDescription = Constants.Labels.HomeScreen.SORT, tint = MaterialTheme.colorScheme.primary)
                        DropdownMenu(expanded = isExpanded, onDismissRequest = { viewModel.hideMenu() }) {
                            DropdownMenuItem(text = { Text(text = Constants.Labels.SortOptions.nameASC) }, onClick = { viewModel.onSort(SortOptions.nameASC) })
                            DropdownMenuItem(text = { Text(text = Constants.Labels.SortOptions.nameDESC) }, onClick = { viewModel.onSort(SortOptions.nameDESC) })
                            DropdownMenuItem(text = { Text(text = Constants.Labels.SortOptions.lastModifiedASC) }, onClick = { viewModel.onSort(SortOptions.lastModifiedASC) })
                            DropdownMenuItem(text = { Text(text = Constants.Labels.SortOptions.lastModifiedDESC) }, onClick = { viewModel.onSort(SortOptions.lastModifiedDESC) })
                        }
                    }
                    IconButton(
                        onClick = {
                            viewModel.onClear()
                            navController.navigate(Screen.Donation.route)
                        }
                    ) {
                        Icon(painterResource(id = R.drawable.round_volunteer_activism_24), contentDescription = Constants.Labels.HomeScreen.SETTINGS, tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(
                        onClick = {
                            viewModel.onClear()
                            navController.navigate(Screen.Setting.route)
                        }
                    ) {
                        Icon(imageVector = Icons.Rounded.Settings, contentDescription = Constants.Labels.HomeScreen.SETTINGS, tint = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        },
        floatingActionButton = {
            if (directoryUri != null && viewModel.hasFileAccessPermission()) {
                FloatingActionButton(
                    modifier = Modifier.padding(end = Dimen.Padding.p4, bottom = Dimen.Padding.p5),
                    onClick = {
                        viewModel.onClear()
                        navController.navigate(Screen.AddEditNote.route)
                    },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = Constants.Labels.HomeScreen.ADD,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    ) { innerPadding ->
        if (directoryUri != null && viewModel.hasFileAccessPermission())
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
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

    //region - Back Press Handler -
    BackHandler(enabled = backPressHandled) {
        if(selectionMode)
            viewModel.onClear()
        else {
            Toast.makeText(context, Constants.Toast.DOUBLE_BACK, Toast.LENGTH_SHORT).show()
            backPressHandled = false
        }
    }
    //endregion

}

