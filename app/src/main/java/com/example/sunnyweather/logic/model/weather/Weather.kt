package com.example.sunnyweather.logic.model.weather

data class Weather(val realtime: RealtimeResponse.Realtime, val daily: DailyResponse.Daily)
