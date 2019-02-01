package com.molo17.damianogiusti.kotlinnativepoc

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.molo17.damianogiusti.BluetoothAdapter
import com.molo17.damianogiusti.ui.DevicesListPresenter
import com.molo17.damianogiusti.ui.DevicesListView
import com.molo17.damianogiusti.ui.UiDevice

class MainActivity : AppCompatActivity(), DevicesListView {

    private val presenter by lazy { DevicesListPresenter(BluetoothAdapter(applicationContext)) }

    private val textView by lazy { findViewById<TextView>(R.id.textView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        if (hasLocationPermission()) {
            start()
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (hasLocationPermission()) {
            start()
        }
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }

    private fun start() {
        presenter.attachView(this)
    }

    override fun showDevices(devices: List<UiDevice>) {
        textView.text = devices.joinToString("\n\n") { it.displayableContent }
    }

    private fun hasLocationPermission() =
        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

}
