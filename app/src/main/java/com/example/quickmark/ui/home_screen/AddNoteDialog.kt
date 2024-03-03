package com.example.quickmark.ui.home_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.quickmark.domain.file_handling.FileHelper
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddNoteDialog(
    showDialog: Boolean,
    fileHelper : FileHelper,
    onDismiss: () -> Unit,
    onDone: (title: String) -> Unit,
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = { onDismiss() },
            properties = DialogProperties(dismissOnClickOutside = false)
        ) {
            Surface(
                modifier = Modifier
                    .width(300.dp)
                    .padding(16.dp),
                shape = MaterialTheme.shapes.medium,
                elevation = 8.dp,
                color = Color.White
            ) {
                var title by remember { mutableStateOf("") }

                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Enter Title") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (title.isNotEmpty()) {
                                onDone(title)
                            }

                        }
                    ) {
                        Text(text = "Done")
                    }
                }
            }
        }
    }
}
