package com.example.todo_utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun convertLongToStringDateTime(deadlineMillis: Long): String {
    val dateFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
    return dateFormat.format(Date(deadlineMillis))
}

fun convertStringDateToLong(strDate: String): Long {
    val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    val date = LocalDate.parse(strDate, dateFormatter)
    val dateTime = date.atStartOfDay()
    return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
}

fun getCurrentTimeString(): String {
    val currentTime = LocalTime.now()
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    return currentTime.format(timeFormatter)
}

fun combineDateAndTime(date: Long, hour: Int, minute: Int): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = date
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)

    // Используем формат Medium для отображения даты и времени
    val formatter =
        SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.MEDIUM, SimpleDateFormat.SHORT)
    return formatter.format(calendar.time)
}

fun convertStringToDateTimeLong(dateTimeString: String): Long {
    // Формат Medium для даты и Short для времени
    val formatter =
        SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.MEDIUM, SimpleDateFormat.SHORT)

    // Парсим строку и получаем объект Date
    val date = formatter.parse(dateTimeString)

    // Возвращаем количество миллисекунд
    val x = date?.time ?: throw IllegalArgumentException("Неправильный формат даты/времени")
    return x
}
