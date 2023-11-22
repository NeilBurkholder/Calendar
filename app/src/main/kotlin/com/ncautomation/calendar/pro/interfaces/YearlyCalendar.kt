package com.ncautomation.calendar.pro.interfaces

import android.util.SparseArray
import com.ncautomation.calendar.pro.models.DayYearly
import java.util.*

interface YearlyCalendar {
    fun updateYearlyCalendar(events: SparseArray<ArrayList<DayYearly>>, hashCode: Int)
}
