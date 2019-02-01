package com.molo17.damianogiusti

expect class BluetoothAdapter {

    fun discoverDevices(callback: (BluetoothDevice) -> Unit)

    fun stopScan()
}