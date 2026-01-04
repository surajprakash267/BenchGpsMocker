package com.bench.gpsmocker

import android.location.Location
import android.location.LocationManager
import android.os.SystemClock

class RouteSimulator(private val provider: String) {

    fun createLocation(
        lat: Double,
        lon: Double,
        speed: Float
    ): Location {
        return Location(provider).apply {
            latitude = lat
            longitude = lon
            this.speed = speed
            accuracy = 5f
            time = System.currentTimeMillis()
            elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
        }
    }
}
