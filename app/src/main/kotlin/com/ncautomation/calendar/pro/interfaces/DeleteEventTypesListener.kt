package com.ncautomation.calendar.pro.interfaces

import com.ncautomation.calendar.pro.models.EventType
import java.util.*

interface DeleteEventTypesListener {
    fun deleteEventTypes(eventTypes: ArrayList<EventType>, deleteEvents: Boolean): Boolean
}
