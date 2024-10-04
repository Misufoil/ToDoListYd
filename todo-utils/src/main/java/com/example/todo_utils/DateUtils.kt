package com.example.todo_utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


fun convertDateToString(date: LocalDate): String {
    val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    return date.format(dateFormatter)
}

fun convertLongToStringDate(time: Long): String {
    val instant = Instant.ofEpochMilli(time)
    val date = instant.atZone(ZoneId.systemDefault()).toLocalDate()
    return convertDateToString(date)
}

fun convertStringDateToLong(strDate: String): Long {
    val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    val date = LocalDate.parse(strDate, dateFormatter)
    val dateTime = date.atStartOfDay()
    return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
}
