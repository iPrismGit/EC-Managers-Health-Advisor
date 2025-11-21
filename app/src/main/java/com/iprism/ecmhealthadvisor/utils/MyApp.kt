package com.iprism.ecmhealthadvisor.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.onesignal.OSNotificationReceivedEvent
import com.onesignal.OneSignal
import kotlinx.coroutines.*

class MyApp : Application() {

    private lateinit var networkConnection: NetworkConnectionLIveData
    private var currentActivity: Activity? = null
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var defaultDensityDpi: Int = Resources.getSystem().displayMetrics.densityDpi

    override fun onCreate() {
        super.onCreate()

        OneSignal.initWithContext(this)

        OneSignal.setAppId("e9cb5d03-036e-4f79-9a9c-d40882633bdb")
        Log.d("OneSignal", "Device is subscribed: " + OneSignal.getDeviceState()!!.isSubscribed())

        OneSignal.setNotificationWillShowInForegroundHandler(
            OneSignal.OSNotificationWillShowInForegroundHandler { notificationReceivedEvent: OSNotificationReceivedEvent? ->
                Log.d(
                    "OneSignalNotification",
                    "Title: " + notificationReceivedEvent!!.getNotification().getTitle()
                )
                Log.d(
                    "OneSignalNotification",
                    "Body: " + notificationReceivedEvent.getNotification().getBody()
                )
                notificationReceivedEvent.complete(notificationReceivedEvent.getNotification())
            })

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityResumed(activity: Activity) {
                currentActivity = activity
                appScope.launch {
                    val isNetwork = NetworkUtil.isNetworkAvailable(activity)
                    val hasInternet = if (isNetwork) NetworkUtil.hasInternetAccess() else false
                    if (!isNetwork || !hasInternet) {
                        NoInternetDialog.show(activity)
                    } else {
                        NoInternetDialog.dismiss()
                    }
                }
            }

            override fun onActivityPaused(activity: Activity) {
                if (currentActivity == activity) currentActivity = null
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })

        networkConnection = NetworkConnectionLIveData(this)
        networkConnection.observeForever { isConnected ->
            val activity = currentActivity ?: return@observeForever
            if (isConnected) {
                appScope.launch {
                    val hasInternet = NetworkUtil.hasInternetAccess()
                    if (hasInternet) NoInternetDialog.dismiss()
                    else NoInternetDialog.show(activity)
                }
            } else {
                NoInternetDialog.show(activity)
            }
        }
    }

    override fun attachBaseContext(base: Context?) {
        if (base != null) {
            super.attachBaseContext(FontDisplayLock.wrap(base))
        } else {
            super.attachBaseContext(base)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        FontDisplayLock.applyToResources(resources)
    }


    override fun getResources(): Resources {
        val res = super.getResources()
        val config = Configuration(res.configuration)

        if (config.fontScale != 1.0f || config.densityDpi != defaultDensityDpi) {
            config.fontScale = 1.0f
            config.densityDpi = defaultDensityDpi
            res.updateConfiguration(config, res.displayMetrics)
        }

        return res
    }

    private fun applyConfiguration(config: Configuration) {
        val res = super.getResources()
        val metrics = res.displayMetrics
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            res.updateConfiguration(config, metrics)
        }
    }

    private fun getLockedContext(base: Context): Context {
        val config = Configuration(base.resources.configuration)
        config.fontScale = 1.0f
        config.densityDpi = defaultDensityDpi
        return base.createConfigurationContext(config)
    }
}
