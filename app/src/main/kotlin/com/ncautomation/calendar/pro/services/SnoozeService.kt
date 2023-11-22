package com.ncautomation.calendar.pro.services

import android.app.IntentService
import android.content.Intent
import com.ncautomation.calendar.pro.extensions.config
import com.ncautomation.calendar.pro.extensions.eventsDB
import com.ncautomation.calendar.pro.extensions.rescheduleReminder
import com.ncautomation.calendar.pro.helpers.EVENT_ID

class SnoozeService : IntentService("Snooze") {
    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val eventId = intent.getLongExtra(EVENT_ID, 0L)
            val event = eventsDB.getEventOrTaskWithId(eventId)
            rescheduleReminder(event, config.snoozeTime)
        }
    }
}
