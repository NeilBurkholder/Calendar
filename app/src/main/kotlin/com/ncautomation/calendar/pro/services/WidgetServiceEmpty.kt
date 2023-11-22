package com.ncautomation.calendar.pro.services

import android.content.Intent
import android.widget.RemoteViewsService
import com.ncautomation.calendar.pro.adapters.EventListWidgetAdapterEmpty

class WidgetServiceEmpty : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent) = EventListWidgetAdapterEmpty(applicationContext)
}
