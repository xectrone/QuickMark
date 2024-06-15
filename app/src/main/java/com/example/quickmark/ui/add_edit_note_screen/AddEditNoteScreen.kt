package com.example.quickmark.ui.add_edit_note_screen

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.navigation.NavController
import com.example.quickmark.R
import com.example.quickmark.domain.Util
import com.example.quickmark.ui.theme.Constants
import com.example.quickmark.ui.theme.CustomTypography
import com.example.quickmark.ui.theme.Dimen
import com.example.quickmark.ui.theme.LocalCustomColorPalette
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddEditNoteScreen(
    fileUri: Uri?,
    navController: NavController,
    viewModel: AddEditNoteViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val noteContent by viewModel.noteContent
    val noteTitle by viewModel.noteTitle
    val isFileModified by viewModel.isFileModified
    val isNewNote by viewModel.isNewNote
    var isValidFileName by remember { mutableStateOf(true) }

    val context = LocalContext.current

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(fileUri){
        if (fileUri != null) {
            viewModel.setFileUri(fileUri = fileUri.toString())
            viewModel.setContent()
            viewModel.toggleIsNewNote()
        }

    }
    LaunchedEffect(Unit) {
        if (isNewNote){
            delay(200)
            focusRequester.requestFocus()
        }
    }



    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = LocalCustomColorPalette.current.background,

        //region - Top Bar -
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(top = Dimen.Padding.statusBar),
                backgroundColor = LocalCustomColorPalette.current.background,
                contentColor = LocalCustomColorPalette.current.primary,
                title = {},
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

        },
        //endregion

        //region - Floating Action Button -
        floatingActionButton =
        {
            if (isValidFileName && isFileModified)
            {
                FloatingActionButton(
                        modifier = Modifier
                            .imePadding()
                            .padding(end = Dimen.Padding.p4, bottom = Dimen.Padding.p5),
                        backgroundColor = LocalCustomColorPalette.current.accent,
                        onClick =
                        {
                            if(noteTitle.isNotBlank()){
                                if (isNewNote) {
                                    viewModel.onCreateNote()
                                    navController.navigateUp()
                                }
                                else
                                    viewModel.onEditNote()
                            }
                            else{
                                Toast.makeText(context,Constants.ExceptionToast.VALID_TITLE, Toast.LENGTH_LONG).show()
                            }

                        }
                    )
                    {
                        Icon(painter = painterResource(id = R.drawable.round_save_24),
                            contentDescription = Constants.Labels.AddEdit.SAVE,
                            tint = LocalCustomColorPalette.current.background

                        )
                    }
            }




        }
        //endregion

    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = Dimen.Padding.p4)
                .verticalScroll(state = rememberScrollState(), reverseScrolling = true)
                .imePadding()
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = noteTitle,
                onValueChange = {
                    viewModel.onNoteTitleChange(it)
                    isValidFileName = Util.isValidFileName(it) && !viewModel.isNoteExists()
                                },
                textStyle = CustomTypography.title,
                isError = !isValidFileName,
                placeholder ={
                     Text(
                         text = "Title",
                         style = CustomTypography.title
                     )
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colors.primary,
                    cursorColor = MaterialTheme.colors.primary,
                    placeholderColor = MaterialTheme.colors.primary.copy(0.4f),
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Red
                ),
                trailingIcon = {
                    IconButton(
                        onClick = { viewModel.onNoteTitleChange("") }
                    ) {
                        Icon(imageVector = Icons.Rounded.Clear, contentDescription =Constants.Labels.AddEdit.CLEAR, tint = LocalCustomColorPalette.current.primary.copy(0.4f))
                    }
                }
            )

            TextField(
                modifier = Modifier
                    .fillMaxSize()
                    .focusRequester(focusRequester),
                value = noteContent,
                onValueChange = { viewModel.onNoteContentChange(it)},
                textStyle = CustomTypography.body,
                placeholder ={
                    Text(
                        text = "Type here...",
                        style = CustomTypography.body.copy(fontStyle = FontStyle.Italic),
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colors.primary,
                    cursorColor = MaterialTheme.colors.primary,
                    placeholderColor = MaterialTheme.colors.primary.copy(0.4f),
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Red
                )
            )
        }
    }
}