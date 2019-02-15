package com.molo17.damianogiusti

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic

actual data class BluetoothDevice(
    actual val id: String,
    actual val name: String,
    internal val androidDevice: BluetoothDevice
) {
    internal var gatt: BluetoothGatt? = null
}

internal fun BleCharacteristic(char: BluetoothGattCharacteristic, service: BleService): BleCharacteristic {
    return BleCharacteristic(char.uuid.toString(), char.value, service)
}

internal class KtGattCallback(
    private val proxy: BluetoothGattCallback,
    var onConnectionStateChange: ((gatt: BluetoothGatt, status: Int, newState: Int) -> Unit)? = null
) : BluetoothGattCallback() {

    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        onConnectionStateChange?.invoke(gatt, status, newState)
        proxy.onConnectionStateChange(gatt, status, newState)
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        proxy.onServicesDiscovered(gatt, status)
    }

    override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
        proxy.onCharacteristicChanged(gatt, characteristic)
    }

    override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
        proxy.onCharacteristicRead(gatt, characteristic, status)
    }

    override fun onCharacteristicWrite(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int
    ) {
        proxy.onCharacteristicWrite(gatt, characteristic, status)
    }
}