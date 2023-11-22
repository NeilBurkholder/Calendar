package com.ncautomation.calendar.pro.dialogs

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.ncautomation.calendar.pro.R
import com.ncautomation.calendar.pro.databinding.DialogCustomPeriodPickerBinding
import com.ncautomation.commons.extensions.*
import com.ncautomation.commons.helpers.DAY_SECONDS
import com.ncautomation.commons.helpers.MONTH_SECONDS
import com.ncautomation.commons.helpers.WEEK_SECONDS

class CustomPeriodPickerDialog(val activity: Activity, val callback: (value: Int) -> Unit) {
    private var dialog: AlertDialog? = null
    private val binding by activity.viewBinding(DialogCustomPeriodPickerBinding::inflate)

    init {
        binding.dialogCustomPeriodValue.setText("")
        binding.dialogRadioView.check(R.id.dialog_radio_days)
        activity.getAlertDialogBuilder()
            .setPositiveButton(com.ncautomation.commons.R.string.ok) { _, _ -> confirmReminder() }
            .setNegativeButton(com.ncautomation.commons.R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(binding.root, this) { alertDialog ->
                    dialog = alertDialog
                    alertDialog.showKeyboard(binding.dialogCustomPeriodValue)
                }
            }
    }

    private fun calculatePeriod(selectedPeriodValue: Int, selectedPeriodValueType: Int) = when (selectedPeriodValueType) {
        R.id.dialog_radio_days -> selectedPeriodValue * DAY_SECONDS
        R.id.dialog_radio_weeks -> selectedPeriodValue * WEEK_SECONDS
        else -> selectedPeriodValue * MONTH_SECONDS
    }

    private fun confirmReminder() {
        val value = binding.dialogCustomPeriodValue.value
        val type = binding.dialogRadioView.checkedRadioButtonId
        val periodValue = value.ifEmpty { "0" }
        val period = calculatePeriod(Integer.valueOf(periodValue), type)
        callback(period)
        activity.hideKeyboard()
        dialog?.dismiss()
    }
}
