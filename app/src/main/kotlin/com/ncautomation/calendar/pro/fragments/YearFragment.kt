package com.ncautomation.calendar.pro.fragments

import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ncautomation.calendar.pro.activities.MainActivity
import com.ncautomation.calendar.pro.databinding.FragmentYearBinding
import com.ncautomation.calendar.pro.databinding.SmallMonthViewHolderBinding
import com.ncautomation.calendar.pro.databinding.TopNavigationBinding
import com.ncautomation.calendar.pro.extensions.config
import com.ncautomation.calendar.pro.extensions.getProperDayIndexInWeek
import com.ncautomation.calendar.pro.extensions.getViewBitmap
import com.ncautomation.calendar.pro.extensions.printBitmap
import com.ncautomation.calendar.pro.helpers.YEAR_LABEL
import com.ncautomation.calendar.pro.helpers.YearlyCalendarImpl
import com.ncautomation.calendar.pro.interfaces.NavigationListener
import com.ncautomation.calendar.pro.interfaces.YearlyCalendar
import com.ncautomation.calendar.pro.models.DayYearly
import com.ncautomation.commons.extensions.applyColorFilter
import com.ncautomation.commons.extensions.getProperPrimaryColor
import com.ncautomation.commons.extensions.getProperTextColor
import com.ncautomation.commons.extensions.updateTextColors
import org.joda.time.DateTime

class YearFragment : Fragment(), YearlyCalendar {
    private var mYear = 0
    private var mFirstDayOfWeek = 0
    private var isPrintVersion = false
    private var lastHash = 0
    private var mCalendar: YearlyCalendarImpl? = null

    var listener: NavigationListener? = null

    private lateinit var binding: FragmentYearBinding
    private lateinit var topNavigationBinding: TopNavigationBinding
    private lateinit var monthHolders: List<SmallMonthViewHolderBinding>

    private val monthResIds = arrayOf(
        com.ncautomation.commons.R.string.january,
        com.ncautomation.commons.R.string.february,
        com.ncautomation.commons.R.string.march,
        com.ncautomation.commons.R.string.april,
        com.ncautomation.commons.R.string.may,
        com.ncautomation.commons.R.string.june,
        com.ncautomation.commons.R.string.july,
        com.ncautomation.commons.R.string.august,
        com.ncautomation.commons.R.string.september,
        com.ncautomation.commons.R.string.october,
        com.ncautomation.commons.R.string.november,
        com.ncautomation.commons.R.string.december
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentYearBinding.inflate(inflater, container, false)
        topNavigationBinding = TopNavigationBinding.bind(binding.root)
        monthHolders = arrayListOf(
            binding.month1Holder, binding.month2Holder, binding.month3Holder, binding.month4Holder, binding.month5Holder, binding.month6Holder,
            binding.month7Holder, binding.month8Holder, binding.month9Holder, binding.month10Holder, binding.month11Holder, binding.month12Holder
        ).apply {
            forEachIndexed { index, it ->
                it.monthLabel.text = getString(monthResIds[index])
            }
        }

        mYear = requireArguments().getInt(YEAR_LABEL)
        requireContext().updateTextColors(binding.calendarWrapper)
        setupMonths()
        setupButtons()

        mCalendar = YearlyCalendarImpl(this, requireContext(), mYear)
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        mFirstDayOfWeek = requireContext().config.firstDayOfWeek
    }

    override fun onResume() {
        super.onResume()
        val firstDayOfWeek = requireContext().config.firstDayOfWeek
        if (firstDayOfWeek != mFirstDayOfWeek) {
            mFirstDayOfWeek = firstDayOfWeek
            setupMonths()
        }
        updateCalendar()
    }

    fun updateCalendar() {
        mCalendar?.getEvents(mYear)
    }

    private fun setupMonths() {
        val dateTime = DateTime().withYear(mYear).withHourOfDay(12)
        monthHolders.forEachIndexed { index, monthHolder ->
            val monthOfYear = index + 1
            val monthView = monthHolder.smallMonthView
            val curTextColor = when {
                isPrintVersion -> resources.getColor(com.ncautomation.commons.R.color.theme_light_text_color)
                else -> requireContext().getProperTextColor()
            }

            monthHolder.monthLabel.setTextColor(curTextColor)
            val firstDayOfMonth = dateTime.withMonthOfYear(monthOfYear).withDayOfMonth(1)
            monthView.firstDay = requireContext().getProperDayIndexInWeek(firstDayOfMonth)
            val numberOfDays = dateTime.withMonthOfYear(monthOfYear).dayOfMonth().maximumValue
            monthView.setDays(numberOfDays)
            monthView.setOnClickListener {
                (activity as MainActivity).openMonthFromYearly(DateTime().withDate(mYear, monthOfYear, 1))
            }
        }

        if (!isPrintVersion) {
            val now = DateTime()
            markCurrentMonth(now)
        }
    }

    private fun setupButtons() {
        val textColor = requireContext().getProperTextColor()
        topNavigationBinding.topLeftArrow.apply {
            applyColorFilter(textColor)
            background = null
            setOnClickListener {
                listener?.goLeft()
            }

            val pointerLeft = requireContext().getDrawable(com.ncautomation.commons.R.drawable.ic_chevron_left_vector)
            pointerLeft?.isAutoMirrored = true
            setImageDrawable(pointerLeft)
        }

        topNavigationBinding.topRightArrow.apply {
            applyColorFilter(textColor)
            background = null
            setOnClickListener {
                listener?.goRight()
            }

            val pointerRight = requireContext().getDrawable(com.ncautomation.commons.R.drawable.ic_chevron_right_vector)
            pointerRight?.isAutoMirrored = true
            setImageDrawable(pointerRight)
        }

        topNavigationBinding.topValue.apply {
            setTextColor(requireContext().getProperTextColor())
            setOnClickListener {
                (activity as MainActivity).showGoToDateDialog()
            }
        }
    }

    private fun markCurrentMonth(now: DateTime) {
        if (now.year == mYear) {
            val monthOfYear = now.monthOfYear
            val monthHolder = monthHolders[monthOfYear - 1]
            monthHolder.monthLabel.setTextColor(requireContext().getProperPrimaryColor())
            monthHolder.smallMonthView.todaysId = now.dayOfMonth
        }
    }

    override fun updateYearlyCalendar(events: SparseArray<ArrayList<DayYearly>>, hashCode: Int) {
        if (!isAdded) {
            return
        }

        if (hashCode == lastHash) {
            return
        }

        lastHash = hashCode
        monthHolders.forEachIndexed { index, monthHolder ->
            val monthView = monthHolder.smallMonthView
            val monthOfYear = index + 1
            monthView.setEvents(events.get(monthOfYear))
        }

        topNavigationBinding.topValue.post {
            topNavigationBinding.topValue.text = mYear.toString()
        }
    }

    fun printCurrentView() {
        isPrintVersion = true
        setupMonths()
        toggleSmallMonthPrintModes()

        requireContext().printBitmap(binding.calendarWrapper.getViewBitmap())

        isPrintVersion = false
        setupMonths()
        toggleSmallMonthPrintModes()
    }

    private fun toggleSmallMonthPrintModes() {
        monthHolders.forEach {
            it.smallMonthView.togglePrintMode()
        }
    }
}
