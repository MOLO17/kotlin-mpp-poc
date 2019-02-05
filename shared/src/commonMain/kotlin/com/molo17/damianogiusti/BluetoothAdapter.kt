package com.molo17.damianogiusti

expect class BluetoothAdapter {

    fun discoverDevices(callback: (BluetoothDevice) -> Unit)

    fun stopScan()

    fun connect(device: BluetoothDevice, callback: (BluetoothDevice) -> Unit)

    fun discoverServices(device: BluetoothDevice, uuids: List<String>, callback: (List<BleService>) -> Unit)

    fun discoverCharacteristics(service: BleService, uuids: List<String>, callback: (List<BleCharacteristic>) -> Unit)

    fun setNotificationEnabled(char: BleCharacteristic, callback: (BleCharacteristic) -> Unit)

    fun serNotificationDisabled(char: BleCharacteristic)
}