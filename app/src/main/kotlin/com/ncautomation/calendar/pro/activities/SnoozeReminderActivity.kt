package com.ncautomation.calendar.pro.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ncautomation.calendar.pro.extensions.config
import com.ncautomation.calendar.pro.extensions.eventsDB
import com.ncautomation.calendar.pro.extensions.rescheduleReminder
import com.ncautomation.calendar.pro.helpers.EVENT_ID
import com.ncautomation.commons.extensions.showPickSecondsDialogHelper
import com.ncautomation.commons.helpers.ensureBackgroundThread

class SnoozeReminderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showPickSecondsDialogHelper(config.snoozeTime, true, cancelCallback = { dialogCancelled() }) {
            ensureBackgroundThread {
                val eventId = intent.getLongExtra(EVENT_ID, 0L)
                val event = eventsDB.getEventOrTaskWithId(eventId)
                config.snoozeTime = it / 60
                rescheduleReminder(event, it / 60)
                runOnUiThread {
                    finishActivity()
                }
            }
        }
    }

    private fun dialogCancelled() {
        finishActivity()
    }

    private fun finishActivity() {
        finish()
        overridePendingTransition(0, 0)
    }
}
