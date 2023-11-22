package com.ncautomation.calendar.pro.interfaces

import android.content.Context
import com.ncautomation.calendar.pro.models.DayMonthly
import org.joda.time.DateTime

interface MonthlyCalendar {
    fun updateMonthlyCalendar(context: Context, month: String, days: ArrayList<DayMonthly>, checkedEvents: Boolean, currTargetDate: DateTime)
}
