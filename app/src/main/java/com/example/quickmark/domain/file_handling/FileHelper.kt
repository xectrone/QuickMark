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

    fun editMarkdownFile(oldFileName: String, fileName: String, content: String, context: Context): String? {
        val adjustedFileName = adjustFileName(fileName)
        val adjustedOldFileName = adjustFileName(oldFileName)
        val file = File(directoryPath, adjustedOldFileName)

        if (content == file.readText() && adjustedFileName == adjustedOldFileName) {
            return null
        }

        try {
            file.writeText(content)
            val newFile = File(directoryPath, adjustedFileName)
            if (file.renameTo(newFile)) {
                return newFile.nameWithoutExtension
            } else {
                throw IOException("Failed to rename the file")
            }
        } catch (e: IOException) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
        return null
    }


    fun createMarkdownFile(fileName: String, content: String,context: Context) {
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

    fun isFileExists(fileName: String):Boolean{
        val adjustedFileName = adjustFileName(fileName)
        val file = File(directoryPath, adjustedFileName)
        return file.exists()
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
            "${fileName.trim()}.md"
        } else {
            fileName
        }
    }
}

