package com.ncautomation.calendar.pro.dialogs

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.ncautomation.calendar.pro.R
import com.ncautomation.calendar.pro.databinding.DialogCustomEventRepeatIntervalBinding
import com.ncautomation.calendar.pro.helpers.DAY
import com.ncautomation.calendar.pro.helpers.MONTH
import com.ncautomation.calendar.pro.helpers.WEEK
import com.ncautomation.calendar.pro.helpers.YEAR
import com.ncautomation.commons.extensions.*

class CustomEventRepeatIntervalDialog(val activity: Activity, val callback: (seconds: Int) -> Unit) {
    private var dialog: AlertDialog? = null
    private val binding by activity.viewBinding(DialogCustomEventRepeatIntervalBinding::inflate)

    init {
        binding.dialogRadioView.check(R.id.dialog_radio_days)

        activity.getAlertDialogBuilder()
            .setPositiveButton(com.ncautomation.commons.R.string.ok) { _, _ -> confirmRepeatInterval() }
            .setNegativeButton(com.ncautomation.commons.R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(binding.root, this) { alertDialog ->
                    dialog = alertDialog
                    alertDialog.showKeyboard(binding.dialogCustomRepeatIntervalValue)
                }
            }
    }

    private fun confirmRepeatInterval() {
        val value = binding.dialogCustomRepeatIntervalValue.value
        val multiplier = getMultiplier(binding.dialogRadioView.checkedRadioButtonId)
        val days = Integer.valueOf(value.ifEmpty { "0" })
        callback(days * multiplier)
        activity.hideKeyboard()
        dialog?.dismiss()
    }

    private fun getMultiplier(id: Int) = when (id) {
        R.id.dialog_radio_weeks -> WEEK
        R.id.dialog_radio_months -> MONTH
        R.id.dialog_radio_years -> YEAR
        else -> DAY
    }
}
