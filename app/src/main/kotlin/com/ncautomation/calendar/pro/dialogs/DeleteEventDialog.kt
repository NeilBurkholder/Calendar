package com.ncautomation.calendar.pro.dialogs

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.ncautomation.calendar.pro.R
import com.ncautomation.calendar.pro.databinding.DialogDeleteEventBinding
import com.ncautomation.calendar.pro.helpers.DELETE_ALL_OCCURRENCES
import com.ncautomation.calendar.pro.helpers.DELETE_FUTURE_OCCURRENCES
import com.ncautomation.calendar.pro.helpers.DELETE_SELECTED_OCCURRENCE
import com.ncautomation.commons.extensions.beVisibleIf
import com.ncautomation.commons.extensions.getAlertDialogBuilder
import com.ncautomation.commons.extensions.setupDialogStuff
import com.ncautomation.commons.extensions.viewBinding

class DeleteEventDialog(
    val activity: Activity,
    eventIds: List<Long>,
    hasRepeatableEvent: Boolean,
    isTask: Boolean = false,
    val callback: (deleteRule: Int) -> Unit
) {
    private var dialog: AlertDialog? = null
    private val binding by activity.viewBinding(DialogDeleteEventBinding::inflate)

    init {
        binding.apply {
            deleteEventRepeatDescription.beVisibleIf(hasRepeatableEvent)
            deleteEventRadioView.beVisibleIf(hasRepeatableEvent)
            if (!hasRepeatableEvent) {
                deleteEventRadioView.check(R.id.delete_event_all)
            }

            if (eventIds.size > 1) {
                deleteEventRepeatDescription.setText(R.string.selection_contains_repetition)
            }

            if (isTask) {
                deleteEventRepeatDescription.setText(R.string.task_is_repeatable)
            } else {
                deleteEventRepeatDescription.setText(R.string.event_is_repeatable)
            }
        }

        activity.getAlertDialogBuilder()
            .setPositiveButton(com.ncautomation.commons.R.string.yes) { _, _ -> dialogConfirmed(binding) }
            .setNegativeButton(com.ncautomation.commons.R.string.no, null)
            .apply {
                activity.setupDialogStuff(binding.root, this) { alertDialog ->
                    dialog = alertDialog
                }
            }
    }

    private fun dialogConfirmed(binding: DialogDeleteEventBinding) {
        val deleteRule = when (binding.deleteEventRadioView.checkedRadioButtonId) {
            R.id.delete_event_all -> DELETE_ALL_OCCURRENCES
            R.id.delete_event_future -> DELETE_FUTURE_OCCURRENCES
            else -> DELETE_SELECTED_OCCURRENCE
        }
        dialog?.dismiss()
        callback(deleteRule)
    }
}
