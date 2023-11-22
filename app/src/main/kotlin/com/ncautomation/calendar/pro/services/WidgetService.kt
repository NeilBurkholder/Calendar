package com.ncautomation.calendar.pro.services

import android.content.Intent
import android.widget.RemoteViewsService
import com.ncautomation.calendar.pro.adapters.EventListWidgetAdapter

class WidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent) = EventListWidgetAdapter(applicationContext, intent)
}
