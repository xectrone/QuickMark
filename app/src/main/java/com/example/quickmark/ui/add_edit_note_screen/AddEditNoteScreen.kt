package com.example.quickmark.ui.add_edit_note_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.navigation.NavController
import com.example.quickmark.R
import com.example.quickmark.ui.theme.CustomTypography
import com.example.quickmark.ui.theme.Dimen
import com.example.quickmark.ui.theme.LocalCustomColorPalette

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    viewModel: AddEditNoteViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

) {
    val noteContent by viewModel.noteContent
    val noteTitle by viewModel.noteTitle
    val edit by viewModel.edit



    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = LocalCustomColorPalette.current.background,
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
                actions = {
                    if(edit)
                        IconButton(
                            onClick = { viewModel.onSaveClick() }
                        )
                        { Icon(painter = painterResource(id = R.drawable.round_save_24), contentDescription = null) }
                    else
                        IconButton(
                            onClick = { viewModel.onEditClick() }
                        )
                        { Icon(imageVector = Icons.Rounded.Edit, contentDescription = null) }
                },
                elevation = Dimen.TopBar.elevation
            )

        }


    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = Dimen.Padding.p4)
        ) {
            TextField(
                value = noteTitle,
                onValueChange = { viewModel.onNoteTitleChange(it)},
                textStyle = CustomTypography.title,
                readOnly = !edit,
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
                )

            )

            TextField(
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
                ),

            )
        }
    }
}