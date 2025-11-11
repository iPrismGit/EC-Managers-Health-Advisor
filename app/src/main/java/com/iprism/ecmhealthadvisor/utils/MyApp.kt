package com.iprism.ecmhealthadvisor.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle

import kotlinx.coroutines.*

class MyApp : Application() {

    private lateinit var networkConnection: NetworkConnectionLIveData
    private var currentActivity: Activity? = null
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()

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
                    if (hasInternet) {
                        NoInternetDialog.dismiss()
                    } else {
                        NoInternetDialog.show(activity)
                    }
                }
            } else {
                NoInternetDialog.show(activity)
            }
        }
    }

}
