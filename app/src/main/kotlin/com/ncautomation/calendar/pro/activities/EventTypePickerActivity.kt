package com.ncautomation.calendar.pro.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ncautomation.calendar.pro.R
import com.ncautomation.calendar.pro.extensions.launchNewEventIntent
import com.ncautomation.calendar.pro.extensions.launchNewTaskIntent
import com.ncautomation.commons.dialogs.RadioGroupDialog
import com.ncautomation.commons.models.RadioItem

class EventTypePickerActivity : AppCompatActivity() {
    private val TYPE_EVENT = 0
    private val TYPE_TASK = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val items = arrayListOf(
            RadioItem(TYPE_EVENT, getString(R.string.event)),
            RadioItem(TYPE_TASK, getString(R.string.task))
        )
        RadioGroupDialog(this, items = items, cancelCallback = { dialogCancelled() }) {
            val checkedId = it as Int
            if (checkedId == TYPE_EVENT) {
                launchNewEventIntent()
            } else if (checkedId == TYPE_TASK) {
                launchNewTaskIntent()
            }
            finish()
        }
    }

    private fun dialogCancelled() {
        finish()
    }
}
