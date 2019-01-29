package com.molo17.damianogiusti

data class BluetoothDevice(
    val id: String,
    val name: String
)

expect class BluetoothAdapter(whenReady: (BluetoothAdapter) -> Unit) {
    fun discoverDevices(callback: (List<BluetoothDevice>) -> Unit)

    fun stopScan()
}

expect fun platformName(): String

fun getMessage() = "\"Parliamo di Koltin (soprattutto di Koltin per ${platformName()})" +
        " ma senza dimenticare altri sviluppi\" cit."
