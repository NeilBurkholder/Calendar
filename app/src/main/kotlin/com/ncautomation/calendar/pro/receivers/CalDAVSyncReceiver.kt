package com.ncautomation.calendar.pro.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ncautomation.calendar.pro.extensions.config
import com.ncautomation.calendar.pro.extensions.recheckCalDAVCalendars
import com.ncautomation.calendar.pro.extensions.refreshCalDAVCalendars
import com.ncautomation.calendar.pro.extensions.updateWidgets

class CalDAVSyncReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (context.config.caldavSync) {
            context.refreshCalDAVCalendars(context.config.caldavSyncedCalendarIds, false)
        }

        context.recheckCalDAVCalendars(true) {
            context.updateWidgets()
        }
    }
}
