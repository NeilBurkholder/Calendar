package com.ncautomation.calendar.pro.extensions

import com.ncautomation.calendar.pro.helpers.Formatter
import com.ncautomation.calendar.pro.models.Event

fun Long.isTsOnProperDay(event: Event): Boolean {
    val dateTime = Formatter.getDateTimeFromTS(this)
    val power = Math.pow(2.0, (dateTime.dayOfWeek - 1).toDouble()).toInt()
    return event.repeatRule and power != 0
}
