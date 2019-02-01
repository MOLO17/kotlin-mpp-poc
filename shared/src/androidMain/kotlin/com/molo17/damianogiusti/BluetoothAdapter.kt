package com.molo17.damianogiusti

import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context

actual class BluetoothAdapter(
    private val context: Context
) : ScanCallback() {

    private val bluetoothManager: BluetoothManager
        get() = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

    private val bluetoothAdapter: android.bluetooth.BluetoothAdapter
        get() = bluetoothManager.adapter

    private var callback: ((BluetoothDevice) -> Unit)? = null

    override fun onScanResult(callbackType: Int, result: ScanResult?) {
        val device = result?.device
        if (device != null) {
            callback?.invoke(BluetoothDevice(device.address, device.name ?: ""))
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Actual declarations
    ///////////////////////////////////////////////////////////////////////////



    actual fun discoverDevices(callback: (BluetoothDevice) -> Unit) {
        this.callback = callback

        bluetoothAdapter.bluetoothLeScanner.startScan(this)
    }

    actual fun stopScan() {
        bluetoothAdapter.bluetoothLeScanner.stopScan(this)
        callback = null
    }
}