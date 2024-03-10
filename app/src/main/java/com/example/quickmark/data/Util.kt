package com.example.quickmark.data

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
        // Add your custom validation rules here

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
}