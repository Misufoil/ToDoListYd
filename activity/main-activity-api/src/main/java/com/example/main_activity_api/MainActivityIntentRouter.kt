package com.example.main_activity_api

import android.content.Context
import android.content.Intent

interface MainActivityIntentRouter {
    fun launch(context: Context, id: String): Intent
}