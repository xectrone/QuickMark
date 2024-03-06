package com.example.quickmark.data

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object AppConverters
{

    fun fromTimestamp(value: Long): LocalDateTime
    {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneOffset.UTC)
    }
    fun toTimestamp(date: LocalDateTime): Long
    {
        return date.atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
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
}