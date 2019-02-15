package com.molo17.damianogiusti

expect class BluetoothAdapter {

    var listener: BluetoothAdapterListener?

    fun discoverDevices(callback: (BluetoothDevice) -> Unit)

    fun stopScan()

    fun findBondedDevices(callback: (List<BluetoothDevice>) -> Unit)

    fun connect(device: BluetoothDevice)

    fun disconnect()

    fun discoverServices()

    fun discoverCharacteristics(service: BleService)

    fun setNotificationEnabled(char: BleCharacteristic)

    fun setNotificationDisabled(char: BleCharacteristic)
}

sealed class BleState {
    data class Connected(val device: BluetoothDevice): BleState()
    data class Disconnected(val device: BluetoothDevice): BleState()
    data class ServicesDiscovered(val device: BluetoothDevice, val services: List<BleService>): BleState()
    data class CharacteristicsDiscovered(val device: BluetoothDevice, val chars: List<BleCharacteristic>): BleState()
    data class CharacteristicChanged(val device: BluetoothDevice, val char: BleCharacteristic): BleState()
}

interface BluetoothAdapterListener {
    fun onStateChange(state: BleState)
}