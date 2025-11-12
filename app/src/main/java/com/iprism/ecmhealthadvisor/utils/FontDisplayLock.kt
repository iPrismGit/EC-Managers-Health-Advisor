package com.iprism.ecmhealthadvisor.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
import android.os.Build

object FontDisplayLock {

    fun wrap(context: Context): Context {
        val res = context.resources
        val config = Configuration(res.configuration)

        config.fontScale = 1.0f

        val defaultDensity = Resources.getSystem().displayMetrics.densityDpi
        config.densityDpi = defaultDensity

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            res.updateConfiguration(config, res.displayMetrics)
            context
        }
    }

    fun applyToResources(res: Resources) {
        val config = Configuration(res.configuration)
        val metrics = res.displayMetrics
        val defaultDensity = Resources.getSystem().displayMetrics.densityDpi

        if (config.fontScale != 1.0f || config.densityDpi != defaultDensity) {
            config.fontScale = 1.0f
            config.densityDpi = defaultDensity
            @Suppress("DEPRECATION")
            res.updateConfiguration(config, metrics)
        }
    }
}
