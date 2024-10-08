package com.example.todo_utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Calendar
import java.util.Locale


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


fun convertStringTimeToLong(timeStr: String): Long {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val time = LocalTime.parse(timeStr, timeFormatter)
    // Используем текущую дату для создания LocalDateTime
    val currentDate = LocalDate.now()
    val dateTime = LocalDateTime.of(currentDate, time)
    // Преобразуем в миллисекунды с начала эпохи
    return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun getCurrentTimeString(): String {
    val currentTime = LocalTime.now()
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    return currentTime.format(timeFormatter)
}

fun formatTime(hour: Int, minute: Int): String {
    return String.format(locale = Locale.ENGLISH, "%02d:%02d", hour, minute)
}

fun convertStringDateTimeToLong(dateStr: String, timeStr: String): Long {
    val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG) // Убедитесь, что формат даты соответствует вашему формату
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")


    val date = LocalDate.parse(dateStr, dateFormatter)
    val time = LocalTime.parse(timeStr, timeFormatter)

    val dateTime = LocalDateTime.of(date, time)
    return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun combineDateAndTimeToLong(date: Long, hour: Int, minute: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = date
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    return calendar.timeInMillis
}

fun combineDateAndTime(date: Long, hour: Int, minute: Int): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = date
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)

    // Используем формат Medium для отображения даты и времени
    val formatter = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.MEDIUM, SimpleDateFormat.SHORT)
    return formatter.format(calendar.time)
}

fun convertStringToDateTimeLong(dateTimeString: String): Long {
    // Формат Medium для даты и Short для времени
    val formatter = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.MEDIUM, SimpleDateFormat.SHORT)

    // Парсим строку и получаем объект Date
    val date = formatter.parse(dateTimeString)

    // Возвращаем количество миллисекунд
    val x = date?.time ?: throw IllegalArgumentException("Неправильный формат даты/времени")
    return x
}
