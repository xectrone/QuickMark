package com.xectrone.quickmark.ui.add_note_dialog.add_note_dialog

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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

    var isValidFileName by remember { mutableStateOf(true) }
    val focusRequester = remember { FocusRequester() }

    fun onDone()
    {
        val isValidTitle = Util.isValidFileName(noteTitle)
        val isAlreadyExits = viewModel.isNoteExists()
        isValidFileName = isValidTitle && !isAlreadyExits
        if(noteTitle.isNotBlank()){
            if(!isValidTitle)
                Toast.makeText(context,Constants.ExceptionToast.NO_VALID_FILE_NAME, Toast.LENGTH_LONG).show()
            else if(isAlreadyExits)
                Toast.makeText(context,Constants.ExceptionToast.FILE_ALREADY_EXIST, Toast.LENGTH_LONG).show()
            else{
                viewModel.onSaveClick()
                finish()

            }
        }
        else
            Toast.makeText(context,Constants.ExceptionToast.VALID_TITLE, Toast.LENGTH_LONG).show()
    }


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
                    isError = !isValidFileName,
                    textStyle = MaterialTheme.typography.subtitle2,
                    trailingIcon = {
                        IconButton(onClick = { viewModel.onNoteTitleChange("") }) {
                            Icon(modifier= Modifier.size(18.dp) ,imageVector = Icons.Rounded.Clear, contentDescription = Constants.Labels.AddEdit.CLEAR, tint = LocalCustomColorPalette.current.primary.copy(0.4f))
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { onDone() }),
                )

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    value = noteContent,
                    onValueChange = { viewModel.onNoteContentChange(it) },
                    placeholder = { Text("Write here...") },
                    colors = textFieldColor,
                    textStyle = MaterialTheme.typography.subtitle1.copy(fontSize = 16.sp),
                )

                Spacer(modifier = Modifier.height(16.dp))

                IconButton(
                    modifier = Modifier
                        .align(Alignment.End),
                    onClick = {  onDone() }
                ) {
                    Icon(painter = painterResource(id = R.drawable.baseline_send_24), contentDescription = Constants.Labels.AddEdit.SAVE, tint = LocalCustomColorPalette.current.accent)
                }

//                Button(
//                    shape = RoundedCornerShape(Dimen.Padding.p2),
//                    colors = ButtonDefaults.buttonColors(
//                        backgroundColor = LocalCustomColorPalette.current.primary,
//                        contentColor = LocalCustomColorPalette.current.backgroundSecondary
//                    ),
//                    enabled = isValidFileName,
//                    onClick = { onDone() }
//                ) {
//                    Text(text = "Done", color = LocalCustomColorPalette.current.primary)
//                }
            }
        }
    }

}

