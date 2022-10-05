package com.example.sunnyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

// 获取全局 context
class SunnyWeatherApplication : Application() {
    companion object {
        const val TOKEN = "SYN93YNYegIsJqJG"

        @SuppressLint("StaticFieldLeak")
        lateinit var context : Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}