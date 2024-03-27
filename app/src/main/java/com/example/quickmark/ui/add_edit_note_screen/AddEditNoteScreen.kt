package com.example.quickmark.ui.add_edit_note_screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material.icons.rounded.Edit
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
import com.example.quickmark.data.Util
import com.example.quickmark.ui.theme.Constants
import com.example.quickmark.ui.theme.CustomTypography
import com.example.quickmark.ui.theme.Dimen
import com.example.quickmark.ui.theme.LocalCustomColorPalette

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddEditNoteScreen(
    fileName: String,
    navController: NavController,
    viewModel: AddEditNoteViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val noteContent by viewModel.noteContent
    val noteTitle by viewModel.noteTitle
    val edit by viewModel.edit
    var isValidFileName by remember { mutableStateOf(true) }
    var isNewNote by remember { mutableStateOf(true) }

    val context = LocalContext.current

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(fileName){
        if (fileName.isNotBlank()) {
            viewModel.setContent(fileName)
            isNewNote = false
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
                    { Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null) }
                },
//                actions = {
//                    if(edit)
//                        IconButton(
//                            onClick = {
//                                viewModel.onSaveClick(isNewNote)
//                            }
//                        )
//                        { Icon(painter = painterResource(id = R.drawable.round_save_24), contentDescription = "Save Note") }
//                    else
//                        IconButton(
//                            onClick = { viewModel.onEditClick() }
//                        )
//                        { Icon(imageVector = Icons.Rounded.Edit, contentDescription = null) }
//                },
                elevation = Dimen.TopBar.elevation
            )

        },
        //endregion

        //region - Floating Action Button -
        floatingActionButton =
        {
            if (isValidFileName)
            {
                if(edit)
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
                                    viewModel.onEditNote(fileName)
                            }
                            else{
                                Toast.makeText(context,Constants.ExceptionToast.VALID_TITLE, Toast.LENGTH_LONG).show()
                            }

                        }
                    )
                    {
                        Icon(painter = painterResource(id = R.drawable.round_save_24),
                            contentDescription = "Add",
                            tint = LocalCustomColorPalette.current.background

                        )
                    }
                }
                else
                {
                    FloatingActionButton(
                        modifier = Modifier
                            .imePadding()
                            .padding(end = Dimen.Padding.p4, bottom = Dimen.Padding.p5),
                        backgroundColor = LocalCustomColorPalette.current.accent,
                        onClick =
                        {
                            viewModel.onEditClick()
                        }
                    )
                    {
                        Icon(imageVector = Icons.Rounded.Edit,
                            contentDescription = "Add",
                            tint = LocalCustomColorPalette.current.background


                        )
                    }
                }

            }

        }
        //endregion

    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = Dimen.Padding.p4)
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = noteTitle,
                onValueChange = {
                    viewModel.onNoteTitleChange(it)
                    isValidFileName = if(isNewNote)
                        (Util.isValidFileName(it) && !viewModel.isNoteExists(it))
                    else
                        Util.isValidFileName(it)
                                },
                textStyle = CustomTypography.title,
                readOnly = !edit,
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
                    IconButton(onClick = { viewModel.onNoteTitleChange("") }) {
                        Icon(imageVector = Icons.Rounded.Clear, contentDescription ="Clear Note Title", tint = LocalCustomColorPalette.current.primary.copy(0.4f))
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
                readOnly = !edit,
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