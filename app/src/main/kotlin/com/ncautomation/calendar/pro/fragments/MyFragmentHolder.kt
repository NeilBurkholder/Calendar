package com.ncautomation.calendar.pro.fragments

import android.graphics.Color
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import com.ncautomation.calendar.pro.databinding.DatePickerDarkBinding
import com.ncautomation.calendar.pro.databinding.DatePickerLightBinding
import com.ncautomation.commons.extensions.getContrastColor
import com.ncautomation.commons.extensions.getProperBackgroundColor
import org.joda.time.DateTime

abstract class MyFragmentHolder : Fragment() {
    abstract val viewType: Int

    abstract fun goToToday()

    abstract fun showGoToDateDialog()

    abstract fun refreshEvents()

    abstract fun shouldGoToTodayBeVisible(): Boolean

    abstract fun getNewEventDayCode(): String

    abstract fun printView()

    abstract fun getCurrentDate(): DateTime?

    fun getDatePickerView(): DatePicker {
        return if (requireActivity().getProperBackgroundColor().getContrastColor() == Color.WHITE) {
            DatePickerDarkBinding.inflate(layoutInflater).datePicker
        } else {
            DatePickerLightBinding.inflate(layoutInflater).datePicker
        }
    }
}
