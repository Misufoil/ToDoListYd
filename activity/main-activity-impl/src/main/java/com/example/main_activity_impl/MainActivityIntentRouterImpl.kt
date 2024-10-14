package com.example.main_activity_impl

import android.content.Context
import android.content.Intent
import com.example.main_activity_api.MainActivityIntentRouter
import javax.inject.Inject

class MainActivityIntentRouterImpl @Inject constructor() : MainActivityIntentRouter {
    override fun launch(context: Context, id: String): Intent {
        val intent = MainActivity.newInstance(context, id)
        return intent
    }
}