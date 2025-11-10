package com.iprism.ecmhealthadvisor.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.util.DisplayMetrics

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        fixDisplayAndFontScale(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        fixDisplayAndFontScale(this)
    }

    @SuppressLint("WrongConstant")
    private fun fixDisplayAndFontScale(context: Context) {
        val resources = context.resources
        val configuration = Configuration(resources.configuration)
        val metrics = resources.displayMetrics

        configuration.fontScale = 1.0f

        val stableDensity = DisplayMetrics.DENSITY_DEVICE_STABLE
        configuration.densityDpi = stableDensity
        metrics.densityDpi = stableDensity

        resources.updateConfiguration(configuration, metrics)
    }
}
