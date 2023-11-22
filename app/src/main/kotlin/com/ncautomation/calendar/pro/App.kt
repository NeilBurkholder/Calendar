package com.ncautomation.calendar.pro

import androidx.multidex.MultiDexApplication
import com.ncautomation.commons.extensions.checkUseEnglish

class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        checkUseEnglish()
    }
}
