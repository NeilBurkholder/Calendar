package com.ncautomation.calendar.pro.dialogs

import androidx.appcompat.app.AlertDialog
import com.ncautomation.calendar.pro.activities.SimpleActivity
import com.ncautomation.calendar.pro.adapters.FilterEventTypeAdapter
import com.ncautomation.calendar.pro.databinding.DialogFilterEventTypesBinding
import com.ncautomation.calendar.pro.extensions.eventsHelper
import com.ncautomation.commons.extensions.getAlertDialogBuilder
import com.ncautomation.commons.extensions.setupDialogStuff
import com.ncautomation.commons.extensions.viewBinding

class SelectEventTypesDialog(val activity: SimpleActivity, selectedEventTypes: Set<String>, val callback: (HashSet<String>) -> Unit) {
    private var dialog: AlertDialog? = null
    private val binding by activity.viewBinding(DialogFilterEventTypesBinding::inflate)

    init {
        activity.eventsHelper.getEventTypes(activity, false) {
            binding.filterEventTypesList.adapter = FilterEventTypeAdapter(activity, it, selectedEventTypes)

            activity.getAlertDialogBuilder()
                .setPositiveButton(com.ncautomation.commons.R.string.ok) { _, _ -> confirmEventTypes() }
                .setNegativeButton(com.ncautomation.commons.R.string.cancel, null)
                .apply {
                    activity.setupDialogStuff(binding.root, this) { alertDialog ->
                        dialog = alertDialog
                    }
                }
        }
    }

    private fun confirmEventTypes() {
        val adapter = binding.filterEventTypesList.adapter as FilterEventTypeAdapter
        val selectedItems = adapter.getSelectedItemsList()
            .map { it.toString() }
            .toHashSet()
        callback(selectedItems)
        dialog?.dismiss()
    }
}
