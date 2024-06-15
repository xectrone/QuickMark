package com.example.quickmark.domain

import android.net.Uri
import android.util.Base64
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Date
import java.util.Locale

object Util
{
    fun isValidFileName(text: String): Boolean {
        return text.matches(Regex("[a-zA-Z0-9-_\\s.]*"))
    }

    fun fromTimestamp(value: Long): LocalDateTime
    {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneOffset.systemDefault())
    }
    fun dateToTimestamp(date: LocalDateTime): Long
    {
        return date.atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli()
    }

    fun formattedDate(date: LocalDateTime): String
    {
        return date.format(
            DateTimeFormatter.ofLocalizedDateTime(
                FormatStyle.MEDIUM,
                FormatStyle.SHORT
            )
        )
    }

    fun defaultFileName(): String = SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss", Locale.getDefault()).format(Date())

    fun encodeUri(uri: Uri?):String {
        if (uri == null)
            return ""
        val uriString = uri.toString()
        return Base64.encodeToString(uriString.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)

    }
    fun decodeUri(encodedUri: String): Uri? {
        val decodedBytes = Base64.decode(encodedUri, Base64.NO_WRAP)
        val decodedString = String(decodedBytes, Charsets.UTF_8)
        if (decodedString.isNullOrBlank())
            return null
        return Uri.parse(decodedString)
    }
}