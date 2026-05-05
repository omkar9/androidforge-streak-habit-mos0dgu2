package com.androidforge.streakhappit.core.extensions

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.LocalDate

fun LocalTime.toFormattedTimeString(pattern: String = "hh:mm a"): String {
    return this.format(DateTimeFormatter.ofPattern(pattern))
}

fun LocalDate.toFormattedDateString(pattern: String = "MMM dd, yyyy"): String {
    return this.format(DateTimeFormatter.ofPattern(pattern))
}