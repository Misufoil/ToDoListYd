package com.example.todolistyd

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.rememberNavController
import com.example.app_uikit.theme.ToDoListTheme
import com.example.todolistyd.navigation.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoListTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { padding ->
                    AppNavGraph(navController = rememberNavController(), Modifier, padding)
                }
            }
        }
        requestNotificationPermission()
    }

    fun requestNotificationPermission() {
        // Проверка, что мы на Android 13 или выше
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_NOTIFICATION_PERMISSION_CODE
                )
            } else {
                // Разрешение уже предоставлено, можно отправлять уведомление
                //sendNotification()
            }
        } else {
            // Разрешение не требуется для версий ниже Android 13
            //sendNotification()
        }
    }

    companion object {
        const val REQUEST_NOTIFICATION_PERMISSION_CODE = 1001
    }

}





