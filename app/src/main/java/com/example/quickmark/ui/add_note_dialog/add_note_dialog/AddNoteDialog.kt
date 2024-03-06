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
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun AddNoteDialog(
    finish: () -> Unit
) {
    val fileHelper = FileHelper("/storage/emulated/0/0.MEDIA/qn")

    val textFieldColor = TextFieldDefaults.textFieldColors(
        textColor = MaterialTheme.colors.primary,
        cursorColor = MaterialTheme.colors.primary,
        placeholderColor = MaterialTheme.colors.primary.copy(0.4f),
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
            color = MaterialTheme.colors.surface

        ) {
            var title by remember { mutableStateOf(SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.getDefault()).format(Date())) }
            var content by remember { mutableStateOf("")}

            Column(
                modifier = Modifier.padding(Dimen.Padding.p3),
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = title,
                    onValueChange = { title = it },
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
                    value = content,
                    onValueChange = { content = it },
                    placeholder = { Text("Write here...") },
                    colors = textFieldColor,
                    textStyle = MaterialTheme.typography.subtitle1.copy(fontSize = 16.sp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (title.isNotEmpty()) {
                            fileHelper.saveMarkdownFile(
                                fileName = "$title.md",
                                content = content
                            )
                            finish()
                        }

                    }
                ) {
                    Text(text = "Done")
                }
            }
        }
    }
}

