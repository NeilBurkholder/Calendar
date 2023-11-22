package com.ncautomation.calendar.pro.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ncautomation.calendar.pro.extensions.*
import com.ncautomation.commons.helpers.ensureBackgroundThread

class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        ensureBackgroundThread {
            context.apply {
                scheduleAllEvents()
                notifyRunningEvents()
                recheckCalDAVCalendars(true) {}
                scheduleNextAutomaticBackup()
                checkAndBackupEventsOnBoot()
            }
        }
    }
}
