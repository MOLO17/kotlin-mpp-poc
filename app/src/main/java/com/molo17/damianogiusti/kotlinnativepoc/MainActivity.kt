package com.molo17.damianogiusti.kotlinnativepoc

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.molo17.damianogiusti.BluetoothAdapter

class MainActivity : AppCompatActivity() {

    private val textView by lazy { findViewById<TextView>(R.id.textView) }

    private lateinit var bluetoothAdapter: BluetoothAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        bluetoothAdapter = BluetoothAdapter {
            it.discoverDevices { devices ->
                textView.text = devices.joinToString("\n\n") { (id, name) -> "$id\n$name" }
            }
        }
    }

    override fun onStop() {
        super.onStop()

        bluetoothAdapter.stopScan()
    }
}
