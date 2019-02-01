package com.molo17.damianogiusti

import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context

actual class BluetoothAdapter(
    private val context: Context,
    actual var whenReady: ((BluetoothAdapter) -> Unit)?
) : ScanCallback() {

    private val foundDevices = mutableMapOf<String, BluetoothDevice>()

    private val bluetoothManager: BluetoothManager
        get() = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

    private val bluetoothAdapter: android.bluetooth.BluetoothAdapter
        get() = bluetoothManager.adapter

    private var callback: ((List<BluetoothDevice>) -> Unit)? = null

    init {
        whenReady?.invoke(this)
    }

    override fun onScanResult(callbackType: Int, result: ScanResult?) {
        val device = result?.device
        if (device != null) {
            foundDevices[device.address] = BluetoothDevice(device.address, device.name ?: "")
            callback?.invoke(foundDevices.values.toList())
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Actual declarations
    ///////////////////////////////////////////////////////////////////////////



    actual fun discoverDevices(callback: (List<BluetoothDevice>) -> Unit) {
        this.callback = callback

        bluetoothAdapter.bluetoothLeScanner.startScan(this)
    }

    actual fun stopScan() {
        bluetoothAdapter.bluetoothLeScanner.stopScan(this)
        callback = null
    }
}