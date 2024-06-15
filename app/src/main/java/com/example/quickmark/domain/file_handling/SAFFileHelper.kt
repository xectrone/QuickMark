package com.example.quickmark.domain.file_handling

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import com.example.quickmark.ui.theme.Constants
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream

object SAFFileHelper {
    fun getAllMarkdownFiles(directoryUri: Uri, context: Context): List<Uri> {
        val markdownFiles = mutableListOf<Uri>()

        val contentResolver: ContentResolver = context.contentResolver
        val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(
            directoryUri,
            DocumentsContract.getTreeDocumentId(directoryUri)
        )

        val cursor = contentResolver.query(childrenUri, arrayOf(
            DocumentsContract.Document.COLUMN_DOCUMENT_ID,
            DocumentsContract.Document.COLUMN_DISPLAY_NAME,
            DocumentsContract.Document.COLUMN_MIME_TYPE
        ), null, null, null)

        cursor?.use {
            while (it.moveToNext()) {
                val displayName = it.getString(1)
                val mimeType = it.getString(2)

                if (mimeType == "text/markdown" || displayName.endsWith(".md")) {
                    val fileUri = Uri.parse("${directoryUri.toString().removeSuffix("/children")}%2F$displayName")
                    markdownFiles.add(fileUri)
                }
            }
        }
        return markdownFiles
    }

    fun createFile(fileName: String, content: String, directoryUri: Uri, context: Context) {
        // Ensure the new file name has the ".md" extension
        val newFileNameWithExtension = if (fileName.endsWith(".md", ignoreCase = true)) {
            fileName.trim()
        } else {
            "${fileName.trim()}.md"
        }
        // Create the new file using the DocumentsContract API
        val contentResolver = context.contentResolver
        val mimeType = "text/markdown"
        val newFileUri: Uri? = DocumentsContract.createDocument(
            contentResolver,
            directoryUri,
            mimeType,
            newFileNameWithExtension
        )

        if (newFileUri == null) {
            Toast.makeText(context, "Failed to create new file.", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            contentResolver.openOutputStream(newFileUri, "w")?.use { outputStream ->
                outputStream.write(content.toByteArray())
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to write content: ${e.message}", Toast.LENGTH_SHORT).show()
            // Clean up by deleting the newly created file
            DocumentsContract.deleteDocument(contentResolver, newFileUri)
        }
    }

    fun editFile(newFileName: String, newFileContent: String, fileUri: Uri, context: Context): Uri? {
        // Get current file name without the extension
        val currentFileName = getFileName(fileUri, context)

        // Determine if renaming is needed
        val needsRename = newFileName != currentFileName

        // Get the content resolver
        val contentResolver = context.contentResolver

        // Rename the file if needed
        var updatedUri = fileUri
        if (needsRename) {
            // Check if a file with the new name already exists in the same directory
            val parentUri = DocumentsContract.buildDocumentUriUsingTree(
                fileUri,
                DocumentsContract.getTreeDocumentId(fileUri).substringBeforeLast(":")
            )

            // Update the file's display name
            val newFileNameWithExtension = "${newFileName.trim()}.md"
            val contentValues = ContentValues().apply {
                put(DocumentsContract.Document.COLUMN_DISPLAY_NAME, newFileNameWithExtension)
            }

            updatedUri = DocumentsContract.renameDocument(contentResolver, fileUri, newFileNameWithExtension)
                ?: return null // Return null if rename fails
        }

        // Update the file's content
        try {
            contentResolver.openOutputStream(updatedUri, "w")?.use { outputStream ->
                outputStream.write(newFileContent.toByteArray())
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to write content: ${e.message}", Toast.LENGTH_SHORT).show()
            return null
        }

        // Return the updated URI
        return updatedUri
    }

    fun deleteFile(fileUri: Uri, context: Context): Boolean {
        return try {
            // Obtain the content resolver
            val contentResolver = context.contentResolver

            // Use DocumentsContract to delete the file
            DocumentsContract.deleteDocument(contentResolver, fileUri)
            // If successful, return true
            true
        } catch (e: Exception) {
            // If there's an error, log it and return false
            Toast.makeText(context, "Failed to delete file: ${e.message}", Toast.LENGTH_SHORT).show()
            false
        }
    }

    // Check if a file exists
    fun isFileExists(
        newFileName: String,
        directoryUri: Uri,
        context: Context
    ): Boolean {
        val newFileNameWithoutExtension = newFileName.trim().substringBeforeLast(".md")
        val markdownFiles = getAllMarkdownFiles(directoryUri, context)
        for (existingFileUri in markdownFiles) {
            val existingFileName = getFileName(existingFileUri, context)
            if (existingFileName == newFileNameWithoutExtension) {
                return true
            }
        }
        return false
    }

    fun getFile(uri: Uri, context: Context): File {
        var tempFile = File.createTempFile("tempFile", ".tmp", context.cacheDir)
        return try {
            // Get the input stream for the given URI
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)

            // Extract the original file name
            val fileName = getFileName(uri, context)?: null

            // Create a file in the cache directory with the original file name
            tempFile = File(context.cacheDir, fileName)

            // Write data from input stream to the file
            inputStream?.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }

            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            tempFile
        }
    }

    fun getFileName(fileUri: Uri, context: Context): String {
        val documentFile = DocumentFile.fromSingleUri(context, fileUri)
        return documentFile?.name?.substringBeforeLast(".md") ?: ""
    }
    // Read a markdown file


    // Edit a markdown file


    fun renameFile(newFileName: String, fileUri: Uri, context: Context): Uri? {
        val contentResolver = context.contentResolver

        // Ensure the new file name ends with ".md"
        val newFileNameWithExtension = if (newFileName.endsWith(".md", ignoreCase = true)) {
            newFileName.trim()
        } else {
            "$newFileName.md"
        }

        // Get the parent directory Uri
        val parentUri = DocumentsContract.buildDocumentUriUsingTree(
            fileUri,
            DocumentsContract.getTreeDocumentId(fileUri)
        )

        // Create the new file with the new name
        val newFileUri = DocumentsContract.createDocument(
            contentResolver,
            parentUri,
            "text/markdown",
            newFileNameWithExtension
        ) ?: return null // Return null if the creation fails

        return try {
            // Copy the contents from the old file to the new file
            contentResolver.openInputStream(fileUri)?.use { inputStream ->
                contentResolver.openOutputStream(newFileUri)?.use { outputStream ->
                    copyStream(inputStream, outputStream)
                }
            }

            // Delete the old file
            DocumentsContract.deleteDocument(contentResolver, fileUri)

            // Return the Uri of the newly created file
            newFileUri
        } catch (e: Exception) {
            // If something goes wrong, delete the new file and return null
            DocumentsContract.deleteDocument(contentResolver, newFileUri)
            e.printStackTrace()
            null
        }
    }

    private fun copyStream(input: InputStream, output: OutputStream) {
        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (input.read(buffer).also { bytesRead = it } != -1) {
            output.write(buffer, 0, bytesRead)
        }
        output.flush()
    }

    fun getFileContent(fileUri: Uri, context: Context): String {
        return try {
            context.contentResolver.openInputStream(fileUri)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    reader.readText()
                }
            }?:""
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to read file: ${e.message}", Toast.LENGTH_LONG).show()
            ""
        }
    }

    // Check if a file is modified





    // Delete a markdown file


    // Helper method to write content to a URI
    private fun writeToUri(content: String, fileUri: Uri, context: Context) {
        context.contentResolver.openOutputStream(fileUri)?.use { outputStream ->
            outputStream.writer().use { it.write(content) }
        }
    }

    // Helper method to get document display name from URI
    private fun getDocumentName(fileUri: Uri, context:Context): String {
        val contentResolver: ContentResolver = context.contentResolver
        val cursor = contentResolver.query(fileUri, arrayOf(DocumentsContract.Document.COLUMN_DISPLAY_NAME), null, null, null)
        var name = ""
        cursor?.use {
            if (it.moveToFirst()) {
                name = it.getString(0)
            }
        }
        return name
    }

    fun isFileModified(newFileName: String, newFileContent:String, fileUri: Uri, context: Context): Boolean {
        if (newFileContent != getFileContent(fileUri = fileUri, context = context))
            return true
        if (newFileName != getFileName(fileUri = fileUri, context = context))
            return true
        return false
    }
}
