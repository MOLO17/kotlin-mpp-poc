package com.molo17.damianogiusti

import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.os.Build
import android.bluetooth.BluetoothAdapter as AndroidBluetoothAdapter

actual fun platformName(): String {
    return "Android ${Build.VERSION.RELEASE}"
}

actual class BluetoothAdapter actual constructor(whenReady: (BluetoothAdapter) -> Unit): ScanCallback() {

    private val bluetoothAdapter by lazy { AndroidBluetoothAdapter.getDefaultAdapter() }

    private var callback: ((List<BluetoothDevice>) -> Unit)? = null

    actual fun discoverDevices(callback: (List<BluetoothDevice>) -> Unit) {
        this.callback = callback

        if (bluetoothAdapter.isDiscovering) {
            bluetoothAdapter.cancelDiscovery()
        }

        bluetoothAdapter.bluetoothLeScanner.startScan(this)
    }

    actual fun stopScan() {
        bluetoothAdapter.bluetoothLeScanner.stopScan(this)
        callback = null
    }

    override fun onBatchScanResults(results: MutableList<ScanResult>?) {
        results?.map { it.device }?.map { BluetoothDevice(it.address, it.name) }?.let { callback?.invoke(it) }
    }

}
