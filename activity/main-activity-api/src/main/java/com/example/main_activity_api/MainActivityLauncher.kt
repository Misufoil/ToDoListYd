package com.example.main_activity_api

import android.app.Activity

interface MainActivityLauncher {
    fun launch(activity: Activity, id: String)
}