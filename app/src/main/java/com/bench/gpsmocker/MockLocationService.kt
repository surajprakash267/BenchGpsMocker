package com.bench.gpsmocker

import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import kotlin.math.cos

class MockLocationService : Service() {

    private lateinit var locationManager: LocationManager
    private var provider = LocationManager.GPS_PROVIDER

    private val handler = Handler(Looper.getMainLooper())
    private var running = false

    private var lat = 0.0
    private var lon = 0.0
    private var speedMps = 0f   // meters / second

    private val updateRunnable = object : Runnable {
        override fun run() {
            if (!running) return

            // Move location based on speed (simple east movement)
            val delta = speedMps / 111000.0
            lon += delta / cos(Math.toRadians(lat))

            pushLocation(lat, lon, speedMps)

            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate() {
        super.onCreate()

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        try {
            locationManager.addTestProvider(
                provider,
                false, false, false, false,
                true, true, true,
                0, 5
            )
        } catch (_: Exception) {}

        locationManager.setTestProviderEnabled(provider, true)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        lat = intent?.getDoubleExtra("lat", 0.0) ?: 0.0
        lon = intent?.getDoubleExtra("lon", 0.0) ?: 0.0
        speedMps = intent?.getFloatExtra("speed", 0f) ?: 0f

        Log.i(
            "BenchGpsMocker",
            "Service START → lat=$lat lon=$lon speed=${speedMps * 3.6}km/h"
        )

        running = true
        handler.post(updateRunnable)

        return START_STICKY
    }

    private fun pushLocation(lat: Double, lon: Double, speed: Float) {
        val location = Location(provider).apply {
            latitude = lat
            longitude = lon
            accuracy = 3f
            this.speed = speed
            time = System.currentTimeMillis()
            elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
        }

        locationManager.setTestProviderLocation(provider, location)

        Log.i(
            "BenchGpsMocker",
            "Injected → lat=$lat lon=$lon speed=${speed * 3.6}km/h"
        )
    }

    override fun onDestroy() {
        running = false
        handler.removeCallbacks(updateRunnable)
        locationManager.removeTestProvider(provider)
        Log.i("BenchGpsMocker", "Service STOPPED")
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
