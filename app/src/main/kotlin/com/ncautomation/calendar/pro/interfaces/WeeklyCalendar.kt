package com.ncautomation.calendar.pro.interfaces

import com.ncautomation.calendar.pro.models.Event

interface WeeklyCalendar {
    fun updateWeeklyCalendar(events: ArrayList<Event>)
}
