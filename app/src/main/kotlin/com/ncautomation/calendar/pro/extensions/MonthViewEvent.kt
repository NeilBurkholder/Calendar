package com.ncautomation.calendar.pro.extensions

import com.ncautomation.calendar.pro.models.MonthViewEvent

fun MonthViewEvent.shouldStrikeThrough() = isTaskCompleted || isAttendeeInviteDeclined
