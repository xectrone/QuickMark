package com.example.quickmark.domain.file_handling

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.quickmark.ui.theme.Constants
import java.io.File
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

    fun readMarkdownFile(fileName: String, context: Context): String {
        val adjustedFileName = adjustFileName(fileName)
        val file = File(directoryPath, adjustedFileName)
        var content = ""

        try {
            content = file.readText()
        } catch (e: IOException) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
        return content
    }

    fun editMarkdownFile(oldFileName: String,fileName: String, content: String, context: Context) {
        Log.d("#TAG", "editMarkdownFile: ")
        val adjustedFileName = adjustFileName(fileName)
        val adjustedOldFileName = adjustFileName(oldFileName)

        val file = File(directoryPath, adjustedOldFileName)
        try {
            file.writeText(content)
            file.renameTo(File(directoryPath, adjustedFileName))
        } catch (e: IOException) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
    }

    fun createMarkdownFile(fileName: String, content: String,context: Context) {
        // Check if the file name contains the .md extension
        Log.d("#TAG", "createMarkdownFile: ")
        val adjustedFileName = adjustFileName(fileName)
        val file = File(directoryPath, adjustedFileName)
        if (file.exists()) {
            Toast.makeText(context, Constants.ExceptionToast.FILE_ALREADY_EXIST, Toast.LENGTH_LONG).show()
            return
        }
        try {
            file.writeText(content)
        } catch (e: IOException) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
    }



    fun deleteMarkdownFile(fileName: String, context: Context){
        val file = File(directoryPath, fileName)

        if (file.exists() && file.isFile)
            file.delete()
        else
            Toast.makeText(context, Constants.ExceptionToast.GENERAL, Toast.LENGTH_LONG).show()
    }

    private fun adjustFileName(fileName:String):String{
        return if (!fileName.endsWith(".md", ignoreCase = true)) {
            "$fileName.md"
        } else {
            fileName
        }
    }
}

