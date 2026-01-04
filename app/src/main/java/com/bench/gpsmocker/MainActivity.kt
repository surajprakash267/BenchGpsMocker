package com.bench.gpsmocker

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var speedSeekBar: SeekBar
    private lateinit var tvSpeedValue: TextView

    private var currentSpeed = 0f   // km/h

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val latInput = findViewById<EditText>(R.id.latInput)
        val lonInput = findViewById<EditText>(R.id.lonInput)

        val startBtn = findViewById<Button>(R.id.startBtn)
        val stopBtn = findViewById<Button>(R.id.stopBtn)

        val haBtn = findViewById<Button>(R.id.btnHA)
        val hbBtn = findViewById<Button>(R.id.btnHB)
        val hcBtn = findViewById<Button>(R.id.btnHC)

        speedSeekBar = findViewById(R.id.speedSeekBar)
        tvSpeedValue = findViewById(R.id.tvSpeedValue)

        // ---------------- SPEED BAR ----------------
        speedSeekBar.max = 120
        speedSeekBar.progress = 0
        tvSpeedValue.text = "Speed: 0 km/h"

        speedSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                currentSpeed = progress.toFloat()
                tvSpeedValue.text = "Speed: $progress km/h"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // ---------------- START ----------------
        startBtn.setOnClickListener {
            val latStr = latInput.text.toString().trim()
            val lonStr = lonInput.text.toString().trim()

            if (latStr.isEmpty() || lonStr.isEmpty()) {
                Log.e("BenchGpsMocker", "Lat/Lon empty")
                return@setOnClickListener
            }

            val lat = latStr.toDouble()
            val lon = lonStr.toDouble()

            val speedMps = currentSpeed / 3.6f

            Log.i(
                "BenchGpsMocker",
                "START → lat=$lat lon=$lon speed=$currentSpeed km/h"
            )

            val intent = Intent(this, MockLocationService::class.java).apply {
                putExtra("lat", lat)
                putExtra("lon", lon)
                putExtra("speed", speedMps)
            }

            startService(intent)
        }

        // ---------------- STOP ----------------
        stopBtn.setOnClickListener {
            Log.i("BenchGpsMocker", "STOP")
            stopService(Intent(this, MockLocationService::class.java))
        }

        // ---------------- HA (Hard Acceleration) ----------------
        haBtn.setOnClickListener {
            Log.i("BenchGpsMocker", "HA triggered")
            simulateSpeedChange(0, 60)
        }

        // ---------------- HB (Hard Brake) ----------------
        hbBtn.setOnClickListener {
            Log.i("BenchGpsMocker", "HB triggered")
            simulateSpeedChange(currentSpeed.toInt(), 0)
        }

        // ---------------- HC (Sharp Turn) ----------------
        hcBtn.setOnClickListener {
            Log.i("BenchGpsMocker", "HC triggered (sharp turn)")
            simulateSpeedChange(currentSpeed.toInt(), (currentSpeed + 20).toInt())
        }
    }

    // ---------------- SPEED SIMULATION ----------------
    private fun simulateSpeedChange(from: Int, to: Int) {
        val handler = Handler(Looper.getMainLooper())
        var speed = from

        val step = if (to > from) 5 else -5

        handler.post(object : Runnable {
            override fun run() {
                speed += step

                if ((step > 0 && speed >= to) || (step < 0 && speed <= to)) {
                    speed = to
                }

                speedSeekBar.progress = speed
                currentSpeed = speed.toFloat()
                tvSpeedValue.text = "Speed: $speed km/h"

                if (speed != to) {
                    handler.postDelayed(this, 100)
                }
            }
        })
    }
}
