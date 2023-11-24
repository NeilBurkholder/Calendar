package com.ncautomation.calendar.pro.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import com.ncautomation.calendar.pro.extensions.eventsDB
import com.ncautomation.calendar.pro.extensions.notifyEvent
import com.ncautomation.calendar.pro.extensions.scheduleNextEventReminder
import com.ncautomation.calendar.pro.extensions.updateListWidget
import com.ncautomation.calendar.pro.helpers.EVENT_ID
import com.ncautomation.calendar.pro.helpers.Formatter
import com.ncautomation.calendar.pro.helpers.REMINDER_NOTIFICATION
import com.ncautomation.commons.helpers.ensureBackgroundThread

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakelock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "simplecalendar:notificationreceiver")
        wakelock.acquire(3000)

        ensureBackgroundThread {
            handleIntent(context, intent)
        }
    }

    private fun handleIntent(context: Context, intent: Intent) {
        val id = intent.getLongExtra(EVENT_ID, -1L)
        if (id == -1L) {
            return
        }

        context.updateListWidget()
        val event = context.eventsDB.getEventOrTaskWithId(id)
        if (event == null || event.getReminders().none { it.type == REMINDER_NOTIFICATION } || event.repetitionExceptions.contains(Formatter.getTodayCode())) {
            return
        }

        if (!event.repetitionExceptions.contains(Formatter.getDayCodeFromTS(event.startTS))) {
            context.notifyEvent(event)
        }
        context.scheduleNextEventReminder(event, false)
    }
}