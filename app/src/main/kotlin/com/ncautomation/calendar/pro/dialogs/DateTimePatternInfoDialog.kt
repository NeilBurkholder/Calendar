package com.ncautomation.calendar.pro.dialogs

import com.ncautomation.calendar.pro.databinding.DatetimePatternInfoLayoutBinding
import com.ncautomation.commons.activities.BaseSimpleActivity
import com.ncautomation.commons.extensions.getAlertDialogBuilder
import com.ncautomation.commons.extensions.setupDialogStuff
import com.ncautomation.commons.extensions.viewBinding

class DateTimePatternInfoDialog(activity: BaseSimpleActivity) {
    val binding by activity.viewBinding(DatetimePatternInfoLayoutBinding::inflate)

    init {
        activity.getAlertDialogBuilder()
            .setPositiveButton(com.ncautomation.commons.R.string.ok) { _, _ -> { } }
            .apply {
                activity.setupDialogStuff(binding.root, this)
            }
    }
}
