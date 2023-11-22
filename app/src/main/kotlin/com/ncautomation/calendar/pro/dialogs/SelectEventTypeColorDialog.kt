package com.ncautomation.calendar.pro.dialogs

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.ncautomation.calendar.pro.R
import com.ncautomation.calendar.pro.adapters.CheckableColorAdapter
import com.ncautomation.calendar.pro.databinding.DialogSelectColorBinding
import com.ncautomation.calendar.pro.views.AutoGridLayoutManager
import com.ncautomation.commons.dialogs.ColorPickerDialog
import com.ncautomation.commons.extensions.getAlertDialogBuilder
import com.ncautomation.commons.extensions.setupDialogStuff
import com.ncautomation.commons.extensions.viewBinding

class SelectEventTypeColorDialog(val activity: Activity, val colors: IntArray, var currentColor: Int, val callback: (color: Int) -> Unit) {
    private var dialog: AlertDialog? = null
    private val binding by activity.viewBinding(DialogSelectColorBinding::inflate)

    init {
        val colorAdapter = CheckableColorAdapter(activity, colors, currentColor) { color ->
            callback(color)
            dialog?.dismiss()
        }

        binding.colorGrid.apply {
            val width = activity.resources.getDimensionPixelSize(R.dimen.smaller_icon_size)
            val spacing = activity.resources.getDimensionPixelSize(com.ncautomation.commons.R.dimen.small_margin) * 2
            layoutManager = AutoGridLayoutManager(context = activity, itemWidth = width + spacing)
            adapter = colorAdapter
        }

        activity.getAlertDialogBuilder()
            .setNegativeButton(com.ncautomation.commons.R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(binding.root, this, R.string.color) {
                    dialog = it
                }

                if (colors.isEmpty()) {
                    showCustomColorPicker()
                }
            }
    }

    private fun showCustomColorPicker() {
        ColorPickerDialog(activity, currentColor) { wasPositivePressed, color ->
            if (wasPositivePressed) {
                callback(color)
            }

            dialog?.dismiss()
        }
    }
}
