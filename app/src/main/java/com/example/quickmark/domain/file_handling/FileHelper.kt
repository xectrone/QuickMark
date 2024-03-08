package com.example.quickmark.domain.file_handling

import android.util.Log
import java.io.File
import java.io.FileFilter
import java.io.IOException

class FileHelper(private val directoryPath: String) {

    fun getAllMarkdownFiles(): List<File> {
        val directory = File(directoryPath)
        val markdownFiles = mutableListOf<File>()
        if (directory.exists() && directory.isDirectory) {
            directory.listFiles()?.let { files ->
                for (file in files) {
                    if (file.isFile && file.extension.equals("md", ignoreCase = true)) {
                        markdownFiles.add(file)
                    }
                }
            }
        }
        return markdownFiles
    }

    fun readMarkdownFile(fileName: String): String {
        val file = File(directoryPath, fileName)
        val stringBuilder = StringBuilder()

        try {
            file.forEachLine {
                stringBuilder.appendLine(it)
            }
        } catch (e: IOException) {
            // Handle IOException
        }

        return stringBuilder.toString()
    }

    fun saveMarkdownFile(fileName: String, content: String) {
        val file = File(directoryPath, fileName)
        try {
            file.writeText(content)
            Log.d("#TAG", "saveMarkdownFile: Saved !")
        } catch (e: IOException) {
            Log.d("#TAG", "saveMarkdownFile: $e")

        }
    }


    fun deleteMarkdownFile(fileName: String): Boolean {
        val file = File(directoryPath, fileName)

        return if (file.exists() && file.isFile) {
            file.delete()
        } else {
            false
        }
    }
}

