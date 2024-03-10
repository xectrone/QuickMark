package com.example.quickmark.ui.add_note_dialog.add_note_dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.quickmark.ui.theme.Dimen
import com.example.quickmark.domain.file_handling.FileHelper
import com.example.quickmark.ui.theme.CustomShape
import com.example.quickmark.ui.theme.LocalCustomColorPalette
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun AddNoteDialog(
    viewModel: AddNoteViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    finish: () -> Unit
) {
    val noteContent by viewModel.noteContent
    val noteTitle by viewModel.noteTitle

    val textFieldColor = TextFieldDefaults.textFieldColors(
        textColor = LocalCustomColorPalette.current.primary,
        cursorColor = LocalCustomColorPalette.current.primary,
        placeholderColor = LocalCustomColorPalette.current.primary.copy(0.4f),
        backgroundColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Red
    )

    Dialog(
            onDismissRequest = { finish()  } ,
            properties = DialogProperties(
                dismissOnClickOutside = true,
                dismissOnBackPress = true,
                usePlatformDefaultWidth = false
            )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimen.Padding.p5),
            shape = RoundedCornerShape(Dimen.Padding.p3),
            elevation = Dimen.Padding.p2,
            color = LocalCustomColorPalette.current.surface

        ) {

            Column(
                modifier = Modifier.padding(Dimen.Padding.p3),
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = noteTitle,
                    onValueChange = { viewModel.onNoteTitleChange(it) },
                    placeholder = {
                        Text(
                            text = "Title",
                            style = MaterialTheme.typography.subtitle2
                        )
                                  },
                    colors = textFieldColor,
                    textStyle = MaterialTheme.typography.subtitle2
                )

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = noteContent,
                    onValueChange = { viewModel.onNoteContentChange(it) },
                    placeholder = { Text("Write here...") },
                    colors = textFieldColor,
                    textStyle = MaterialTheme.typography.subtitle1.copy(fontSize = 16.sp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    shape = RoundedCornerShape(Dimen.Padding.p2),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = LocalCustomColorPalette.current.primary,
                        contentColor = LocalCustomColorPalette.current.backgroundSecondary
                    ),
                    onClick = {
                        if (noteTitle.isNotEmpty()) {
                                viewModel.onSaveClick()
                            finish()
                        }

                    }
                ) {
                    Text(text = "Add")
                }
            }
        }
    }
}

