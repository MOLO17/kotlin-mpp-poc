package com.molo17.damianogiusti

expect class BluetoothAdapter {

    var whenReady: ((BluetoothAdapter) -> Unit)?

    fun discoverDevices(callback: (List<BluetoothDevice>) -> Unit)

    fun stopScan()
}