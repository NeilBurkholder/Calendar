package com.ncautomation.calendar.pro.dialogs

import android.app.Activity
import com.ncautomation.calendar.pro.databinding.DialogVerticalLinearLayoutBinding
import com.ncautomation.calendar.pro.databinding.MyCheckboxBinding
import com.ncautomation.calendar.pro.extensions.withFirstDayOfWeekToFront
import com.ncautomation.commons.extensions.getAlertDialogBuilder
import com.ncautomation.commons.extensions.setupDialogStuff
import com.ncautomation.commons.extensions.viewBinding
import com.ncautomation.commons.views.MyAppCompatCheckbox

class RepeatRuleWeeklyDialog(val activity: Activity, val curRepeatRule: Int, val callback: (repeatRule: Int) -> Unit) {
    private val binding by activity.viewBinding(DialogVerticalLinearLayoutBinding::inflate)

    init {
        val days = activity.resources.getStringArray(com.ncautomation.commons.R.array.week_days)
        var checkboxes = ArrayList<MyAppCompatCheckbox>(7)
        for (i in 0..6) {
            val pow = Math.pow(2.0, i.toDouble()).toInt()
            MyCheckboxBinding.inflate(activity.layoutInflater).root.apply {
                isChecked = curRepeatRule and pow != 0
                text = days[i]
                id = pow
                checkboxes.add(this)
            }
        }

        checkboxes = activity.withFirstDayOfWeekToFront(checkboxes)
        checkboxes.forEach {
            binding.dialogVerticalLinearLayout.addView(it)
        }

        activity.getAlertDialogBuilder()
            .setPositiveButton(com.ncautomation.commons.R.string.ok) { _, _ -> callback(getRepeatRuleSum()) }
            .setNegativeButton(com.ncautomation.commons.R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(binding.root, this)
            }
    }

    private fun getRepeatRuleSum(): Int {
        var sum = 0
        val cnt = binding.dialogVerticalLinearLayout.childCount
        for (i in 0 until cnt) {
            val child = binding.dialogVerticalLinearLayout.getChildAt(i)
            if (child is MyAppCompatCheckbox) {
                if (child.isChecked)
                    sum += child.id
            }
        }
        return sum
    }
}
