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
    internal var gattCallback: KtGattCallback? = null
}

internal class KtGattCallback(
    var onConnectionStateChange: ((gatt: BluetoothGatt, status: Int, newState: Int) -> Unit)? = null,
    var onServicesDiscovered: ((gatt: BluetoothGatt, status: Int) -> Unit)? = null,
    var onCharacteristicChanged: ((gatt: BluetoothGatt, char: BluetoothGattCharacteristic) -> Unit)? = null
) : BluetoothGattCallback() {

    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        onConnectionStateChange?.invoke(gatt, status, newState)
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        onServicesDiscovered?.invoke(gatt, status)
    }

    override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
        onCharacteristicChanged?.invoke(gatt, characteristic)
    }
}