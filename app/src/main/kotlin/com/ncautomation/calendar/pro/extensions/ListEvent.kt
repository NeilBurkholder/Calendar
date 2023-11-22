package com.ncautomation.calendar.pro.extensions

import com.ncautomation.calendar.pro.models.ListEvent

fun ListEvent.shouldStrikeThrough() = isTaskCompleted || isAttendeeInviteDeclined
