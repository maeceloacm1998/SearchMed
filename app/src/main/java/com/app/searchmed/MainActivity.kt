package com.app.searchmed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.app.home.ui.di.HomeModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidLogger()
            androidContext(this@MainActivity)
            modules(listOf(
                HomeModule.modules
            ))
        }

        setContent {
            SearchMedApp()
        }
    }
}