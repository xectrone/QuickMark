package com.xectrone.quickmark.ui.add_edit_note_screen


import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.xectrone.quickmark.R
import com.xectrone.quickmark.domain.Util
import com.xectrone.quickmark.ui.theme.Constants
import com.xectrone.quickmark.ui.theme.CustomTypography
import com.xectrone.quickmark.ui.theme.Dimen
import com.xectrone.quickmark.ui.theme.LocalCustomColorPalette
import com.xectrone.quickmark.ui.theme.OnDarkCustomColorPalette
import kotlinx.coroutines.delay
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AddEditNoteScreen(
    fileUri: Uri?,
    navController: NavController,
    viewModel: AddEditNoteViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val noteTitle by viewModel.noteTitle
    val isFileModified by viewModel.isFileModified
    val isNewNote by viewModel.isNewNote
    var isValidFileName by remember { mutableStateOf(true) }

    // Preview mode state
    var isPreviewMode by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val focusRequester = remember { FocusRequester() }
    val scrollState = rememberScrollState() // ScrollState to control scrolling
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    // Local state for noteContent to prevent cursor jump
    var localNoteContent by rememberSaveable { mutableStateOf("") }

    // Sync localNoteContent with ViewModel only when fileUri or isNewNote changes
    LaunchedEffect(fileUri, isNewNote) {
        localNoteContent = viewModel.noteContent.value
    }

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
        containerColor = LocalCustomColorPalette.current.background,

        //region - Top Bar -
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors().copy(containerColor = LocalCustomColorPalette.current.background, navigationIconContentColor = LocalCustomColorPalette.current.primary),
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick =
                        {
                            navController.navigateUp()
                        }
                    )
                    { Icon(imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = Constants.Labels.BACK) }
                },
                actions = {
                    IconButton(
                        onClick = { isPreviewMode = !isPreviewMode }
                    ) {
                        if (isPreviewMode){
                            Icon(
                                imageVector = Icons.Rounded.Edit,
                                contentDescription =  "Edit Mode",
                                tint = LocalCustomColorPalette.current.primary
                            )
                        }
                        else{
                            Icon(
                                painterResource(id = R.drawable.baseline_article_24),
                                contentDescription = "Preview Mode",
                                tint = LocalCustomColorPalette.current.primary
                            )
                        }

                    }
                }
            )
        },
        //endregion

        //region - Floating Action Button -
        floatingActionButton =
        {
            FloatingActionButton(
                modifier = Modifier
                    .imePadding()
                    .padding(end = Dimen.Padding.p4, bottom = Dimen.Padding.p5),
                containerColor = LocalCustomColorPalette.current.accent,
                contentColor = LocalCustomColorPalette.current.background,
                onClick =
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
                            // Push localNoteContent to ViewModel before saving
                            viewModel.onNoteContentChange(localNoteContent)
                            if (isNewNote) {
                                viewModel.onCreateNote()
                                navController.navigateUp()
                            }
                            else {
                                viewModel.onEditNote()
                                navController.navigateUp()
                            }
                        }
                    }
                    else
                        Toast.makeText(context,Constants.ExceptionToast.VALID_TITLE, Toast.LENGTH_LONG).show()
                }
            )
            {
                Icon(painter = painterResource(id = R.drawable.round_save_24),
                    contentDescription = Constants.Labels.AddEdit.SAVE,
                    tint = LocalCustomColorPalette.current.background

                )
            }
        }
        //endregion

    ) { paddingValues ->
        if (isPreviewMode) {
            // Live Markdown Preview Mode
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = Dimen.Padding.p3)
                    .imePadding(),
                reverseLayout = true
            ) {
                item {
                    // Beautiful Card with Rounded Corners
                    val isDarkMode = LocalCustomColorPalette.current.background == OnDarkCustomColorPalette.background
                    
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Dimen.Padding.p2),
                        colors = CardDefaults.cardColors(
                            containerColor = LocalCustomColorPalette.current.surface,
                            disabledContainerColor = LocalCustomColorPalette.current.surface,
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(Dimen.Padding.p4)
                        ) {
                            // Title
                            Text(
                                text = noteTitle.ifBlank { "Untitled" },
                                style = CustomTypography.title(),
                                color = LocalCustomColorPalette.current.primary,
                                modifier = Modifier.padding(bottom = Dimen.Padding.p2)
                            )
                            
                            // Divider
                            Divider(
                                color = LocalCustomColorPalette.current.primary.copy(0.2f),
                                thickness = 1.dp,
                                modifier = Modifier.padding(vertical = Dimen.Padding.p2)
                            )
                            
                            // Markdown Content
                            val markdownContent = if (localNoteContent.isBlank()) {
                                "# Test Markdown\n\nThis is a **test** of the markdown preview.\n\n- Item 1\n- Item 2\n\n*Italic text* and `code`"
                            } else {
                                localNoteContent
                            }
                            
                            // Simple Markdown Renderer
        Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                val lines = markdownContent.split("\n")
                                var i = 0
                                while (i < lines.size) {
                                    val line = lines[i].trim()
                                    
                                    when {
                                        line.startsWith("# ") -> {
                                            Text(
                                                text = line.substring(2),
                                                style = CustomTypography.title().copy(
                                                    fontSize = 24.sp
                                                ),
                                                color = LocalCustomColorPalette.current.primary,
                                                modifier = Modifier.padding(vertical = 8.dp)
                                            )
                                        }
                                        line.startsWith("## ") -> {
                                            Text(
                                                text = line.substring(3),
                                                style = CustomTypography.title().copy(
                                                    fontSize = 20.sp
                                                ),
                                                color = LocalCustomColorPalette.current.primary,
                                                modifier = Modifier.padding(vertical = 6.dp)
                                            )
                                        }
                                        line.startsWith("### ") -> {
                                            Text(
                                                text = line.substring(4),
                                                style = CustomTypography.title().copy(
                                                    fontSize = 18.sp
                                                ),
                                                color = LocalCustomColorPalette.current.primary,
                                                modifier = Modifier.padding(vertical = 4.dp)
                                            )
                                        }
                                        line == "---" -> {
                                            // Horizontal divider
                                            Divider(
                                                color = LocalCustomColorPalette.current.primary.copy(0.3f),
                                                thickness = 1.dp,
                                                modifier = Modifier.padding(vertical = 16.dp)
                                            )
                                        }
                                        line.startsWith("- ") -> {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = "• ",
                                                    style = CustomTypography.body(),
                                                    color = LocalCustomColorPalette.current.primary
                                                )
                                                Text(
                                                    text = renderInlineMarkdown(line.substring(2)),
                                                    style = CustomTypography.body(),
                                                    color = LocalCustomColorPalette.current.primary
                                                )
                                            }
                                        }
                                        line.startsWith("1. ") -> {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = "${i + 1}. ",
                                                    style = CustomTypography.body(),
                                                    color = LocalCustomColorPalette.current.primary
                                                )
                                                Text(
                                                    text = renderInlineMarkdown(line.substring(3)),
                                                    style = CustomTypography.body(),
                                                    color = LocalCustomColorPalette.current.primary
                                                )
                                            }
                                        }
                                        line.trim().startsWith("```") -> {
                                            // Code block
                                            var codeBlock = ""
                                            i++
                                            while (i < lines.size && !lines[i].trim().startsWith("```")) {
                                                codeBlock += lines[i] + "\n"
                                                i++
                                            }
                                            // Skip over the closing backticks line
                                            if (i < lines.size && lines[i].trim().startsWith("```")) {
                                                i++
                                            }
                                            Text(
                                                text = codeBlock.trim(),
                                                style = CustomTypography.body().copy(
                                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                                                ),
                                                color = LocalCustomColorPalette.current.primary,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 8.dp)
                                                    .background(
                                                        color = LocalCustomColorPalette.current.primary.copy(0.1f),
                                                        shape = RoundedCornerShape(8.dp)
                                                    )
                                                    .padding(16.dp)
                                            )
                                        }
                                        line.startsWith("`") && line.endsWith("`") && line.length > 3 -> {
                                            Text(
                                                text = line.substring(1, line.length - 1),
                                                style = CustomTypography.body().copy(
                                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                                                ),
                                                color = LocalCustomColorPalette.current.primary,
                                                modifier = Modifier
                                                    .padding(vertical = 4.dp)
                                                    .background(
                                                        color = LocalCustomColorPalette.current.primary.copy(0.1f),
                                                        shape = RoundedCornerShape(4.dp)
                                                    )
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                        line.startsWith("> ") -> {
                                            Text(
                                                text = renderInlineMarkdown(line.substring(2)),
                                                style = CustomTypography.body().copy(
                                                    fontStyle = FontStyle.Italic
                                                ),
                                                color = LocalCustomColorPalette.current.primary.copy(0.7f),
                                                modifier = Modifier
                                                    .padding(vertical = 4.dp)
                                                    .padding(horizontal = 16.dp)
                                                    .border(
                                                        width = 2.dp,
                                                        color = LocalCustomColorPalette.current.primary.copy(0.3f),
                                                        shape = RoundedCornerShape(4.dp)
                                                    )
                                                    .padding(12.dp)
                                            )
                                        }
                                        line.isNotEmpty() -> {
                                            Text(
                                                text = renderInlineMarkdown(line),
                                                style = CustomTypography.body(),
                                                color = LocalCustomColorPalette.current.primary,
                                                modifier = Modifier.padding(vertical = 4.dp)
                                            )
                                        }
                                        else -> {
                                            // Empty line
                                            androidx.compose.foundation.layout.Spacer(
                                                modifier = Modifier.height(8.dp)
                                            )
                                        }
                                    }
                                    i++
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Edit Mode
            LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = Dimen.Padding.p3)
                    .imePadding(),
                reverseLayout = true
            ) {
                item {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .bringIntoViewRequester(bringIntoViewRequester)
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused) {
                                    coroutineScope.launch {
                                        bringIntoViewRequester.bringIntoView()
                                    }
                                }
                            },
                        value = localNoteContent,
                        onValueChange = { localNoteContent = it },
                        textStyle = CustomTypography.body(),
                        placeholder = {
                            Text(
                                text = "Write here...\n\n# Heading 1\n## Heading 2\n\n**Bold text**\n*Italic text*\n\n- List item 1\n- List item 2\n\n1. Numbered item 1\n2. Numbered item 2\n\n[Link text](https://example.com)\n\n`inline code`\n\n```\ncode block\n```\n\n```python\nprint('Hello')\n```",
                                style = CustomTypography.body(),
                                color = LocalCustomColorPalette.current.primary.copy(0.4f),
                            )
                        },
                        colors = TextFieldDefaults.colors().copy(
                            cursorColor = LocalCustomColorPalette.current.primary,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Red,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )
                }
                item {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = noteTitle,
                onValueChange = { viewModel.onNoteTitleChange(it) },
                textStyle = CustomTypography.title(),
                isError = !isValidFileName,
                placeholder ={
                     Text(
                         text = "Title",
                         style = CustomTypography.title(),
                         color = LocalCustomColorPalette.current.primary.copy(0.4f),
                     )
                },
                colors = TextFieldDefaults.colors().copy(
                    cursorColor = LocalCustomColorPalette.current.primary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Red,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                trailingIcon = {
                    IconButton(
                        onClick = { viewModel.onNoteTitleChange("") }
                    ) {
                        Icon(imageVector = Icons.Rounded.Clear, contentDescription =Constants.Labels.AddEdit.CLEAR, tint = LocalCustomColorPalette.current.primary.copy(0.4f))
                    }
                }
            )
                }
            }
        }
    }
}

@Composable
private fun renderInlineMarkdown(text: String): AnnotatedString {
    return buildAnnotatedString {
        var currentIndex = 0
        while (currentIndex < text.length) {
            when {
                text.startsWith("**", currentIndex) -> {
                    val endIndex = text.indexOf("**", currentIndex + 2)
                    if (endIndex != -1) {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(text.substring(currentIndex + 2, endIndex))
                        }
                        currentIndex = endIndex + 2
                    } else {
                        append(text[currentIndex])
                        currentIndex++
                    }
                }
                text.startsWith("*", currentIndex) && !text.startsWith("**", currentIndex) -> {
                    val endIndex = text.indexOf("*", currentIndex + 1)
                    if (endIndex != -1 && endIndex != currentIndex + 1) {
                        withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                            append(text.substring(currentIndex + 1, endIndex))
                        }
                        currentIndex = endIndex + 1
                    } else {
                        append(text[currentIndex])
                        currentIndex++
                    }
                }
                text.startsWith("~~", currentIndex) -> {
                    val endIndex = text.indexOf("~~", currentIndex + 2)
                    if (endIndex != -1) {
                        withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                            append(text.substring(currentIndex + 2, endIndex))
                        }
                        currentIndex = endIndex + 2
                    } else {
                        append(text[currentIndex])
                        currentIndex++
                    }
                }
                text.startsWith("`", currentIndex) -> {
                    val endIndex = text.indexOf("`", currentIndex + 1)
                    if (endIndex != -1) {
                        withStyle(SpanStyle(fontFamily = FontFamily.Monospace)) {
                            append(text.substring(currentIndex + 1, endIndex))
                        }
                        currentIndex = endIndex + 1
                    } else {
                        append(text[currentIndex])
                        currentIndex++
                    }
                }
                else -> {
                    append(text[currentIndex])
                    currentIndex++
                }
            }
        }
    }
}