package ru.mggtk.practice20_curiosity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Button
import android.widget.TextView
class MainActivity : AppCompatActivity() {
    private lateinit var sensorManager: SensorManager
    private lateinit var gyroscopeSensor: Sensor
    private lateinit var gyroscopeListener: SensorEventListener

    private var isThreadRunning = false
    private lateinit var handlerThread: HandlerThread
    private lateinit var handler: Handler

    private lateinit var gyroscopeDataTextView: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gyroscopeDataTextView = findViewById(R.id.gyroscopeData)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)!!

        gyroscopeListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val gyroData = "X: ${event.values[0]}, Y: ${event.values[1]}, Z: ${event.values[2]}"
                runOnUiThread {
                    gyroscopeDataTextView.text = gyroData
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    fun startGyroscopeThread(view: View) {
        if (!isThreadRunning) {
            isThreadRunning = true
            startButton.isEnabled = false
            stopButton.isEnabled = true

            sensorManager.registerListener(gyroscopeListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stopGyroscopeThread(view: View?) {
        if (isThreadRunning) {
            isThreadRunning = false
            startButton.isEnabled = true
            stopButton.isEnabled = false

            sensorManager.unregisterListener(gyroscopeListener)
        }
    }

    override fun onPause() {
        super.onPause()
        stopGyroscopeThread(null)
    }
}