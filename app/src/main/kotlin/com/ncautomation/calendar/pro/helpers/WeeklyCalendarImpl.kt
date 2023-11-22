package com.ncautomation.calendar.pro.helpers

import android.content.Context
import com.ncautomation.calendar.pro.extensions.eventsHelper
import com.ncautomation.calendar.pro.interfaces.WeeklyCalendar
import com.ncautomation.calendar.pro.models.Event
import com.ncautomation.commons.helpers.DAY_SECONDS
import com.ncautomation.commons.helpers.WEEK_SECONDS
import java.util.*

class WeeklyCalendarImpl(val callback: WeeklyCalendar, val context: Context) {
    var mEvents = ArrayList<Event>()

    fun updateWeeklyCalendar(weekStartTS: Long) {
        val endTS = weekStartTS + 2 * WEEK_SECONDS
        context.eventsHelper.getEvents(weekStartTS - DAY_SECONDS, endTS) {
            mEvents = it
            callback.updateWeeklyCalendar(it)
        }
    }
}
