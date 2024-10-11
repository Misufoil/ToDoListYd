package com.example.main_activity_impl

import android.app.Activity
import com.example.main_activity_api.MainActivityLauncher
import javax.inject.Inject

class MainActivityLauncherImpl @Inject constructor() : MainActivityLauncher {
    override fun launch(activity: Activity, id: String) {
        val intent = MainActivity.newInstance(activity, id)
        activity.startActivity(intent)
    }
}