package com.xectrone.quickmark.ui.add_note_dialog.add_note_dialog

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.xectrone.quickmark.R
import com.xectrone.quickmark.domain.Util
import com.xectrone.quickmark.ui.theme.Dimen
import com.xectrone.quickmark.ui.theme.Constants
import com.xectrone.quickmark.ui.theme.LocalCustomColorPalette

@Composable
fun AddNoteDialog(
    viewModel: AddNoteViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    finish: () -> Unit
) {
    val noteContent by viewModel.noteContent
    val noteTitle by viewModel.noteTitle
    val context = LocalContext.current
    val activity = context as? Activity
    val directoryUri by viewModel.directoryUri
    var isValidFileName by remember { mutableStateOf(true) }
    val focusRequester = remember { FocusRequester() }

    fun onDone() {
        val isValidTitle = Util.isValidFileName(noteTitle)
        val isAlreadyExits = viewModel.isNoteExists()
        isValidFileName = isValidTitle && !isAlreadyExits
        if (noteTitle.isNotBlank()) {
            if (directoryUri == null)
                Toast.makeText(context, Constants.SELECT_DIRECTORY_PATH_MSG, Toast.LENGTH_LONG).show()
            else if (!isValidTitle)
                Toast.makeText(context, Constants.ExceptionToast.NO_VALID_FILE_NAME, Toast.LENGTH_LONG).show()
            else if (isAlreadyExits)
                Toast.makeText(context, Constants.ExceptionToast.FILE_ALREADY_EXIST, Toast.LENGTH_LONG).show()
            else {
                viewModel.onSaveClick()
                activity?.finishAndRemoveTask()
            }
        } else
            Toast.makeText(context, Constants.ExceptionToast.VALID_TITLE, Toast.LENGTH_LONG).show()
    }

    val textFieldColors = TextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
        errorTextColor = MaterialTheme.colorScheme.error,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        errorContainerColor = Color.Transparent,
        cursorColor = MaterialTheme.colorScheme.primary,
        errorCursorColor = MaterialTheme.colorScheme.error,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = MaterialTheme.colorScheme.error,
        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
        errorPlaceholderColor = MaterialTheme.colorScheme.error.copy(alpha = 0.4f)
    )

    Dialog(
        onDismissRequest = { activity?.finishAndRemoveTask() },
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true,
            usePlatformDefaultWidth = false
        ),
    ) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimen.Padding.p5),
            shape = RoundedCornerShape(Dimen.Padding.p3),
            tonalElevation = Dimen.Padding.p2,
            color = MaterialTheme.colorScheme.surface
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimen.Padding.p3)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = noteTitle,
                            onValueChange = { viewModel.onNoteTitleChange(it) },
                            placeholder = {
                                Text(
                                    text = "Title",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            },
                            colors = textFieldColors,
                            isError = !isValidFileName,
                            textStyle = MaterialTheme.typography.titleMedium,
                            trailingIcon = {
                                IconButton(onClick = { viewModel.onNoteTitleChange("") }) {
                                    Icon(
                                        modifier = Modifier.size(18.dp),
                                        imageVector = Icons.Rounded.Clear,
                                        contentDescription = Constants.Labels.AddEdit.CLEAR,
                                        tint = MaterialTheme.colorScheme.onSurface.copy(0.4f)
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = { onDone() }),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            value = noteContent,
                            onValueChange = { viewModel.onNoteContentChange(it) },
                            placeholder = { Text("Write here...", style = MaterialTheme.typography.bodyLarge) },
                            colors = textFieldColors,
                            textStyle = MaterialTheme.typography.bodyLarge,
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    IconButton(
                        modifier = Modifier.align(Alignment.End),
                        onClick = { onDone() }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_send_24),
                            contentDescription = Constants.Labels.AddEdit.SAVE,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

