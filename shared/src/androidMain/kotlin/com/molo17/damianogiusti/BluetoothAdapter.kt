package com.molo17.damianogiusti

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import java.util.*
import android.bluetooth.BluetoothDevice as AndroidBleDevice

actual class BluetoothAdapter(
    private val context: Context
) : ScanCallback() {

    private val bluetoothManager: BluetoothManager
        get() = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

    private val bluetoothAdapter: android.bluetooth.BluetoothAdapter
        get() = bluetoothManager.adapter

    private var handler: ((BluetoothDevice) -> Unit)? = null

    override fun onScanResult(callbackType: Int, result: ScanResult?) {
        val device = result?.device
        if (device != null) {
            handler?.invoke(BluetoothDevice(device.address, device.name ?: "", device))
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Actual declarations
    ///////////////////////////////////////////////////////////////////////////

    actual fun discoverDevices(callback: (BluetoothDevice) -> Unit) {
        handler = callback

        bluetoothAdapter.bluetoothLeScanner.startScan(this)
    }

    actual fun stopScan() {
        bluetoothAdapter.bluetoothLeScanner.stopScan(this)
        handler = null
    }

    actual fun connect(
        device: BluetoothDevice,
        callback: (BluetoothDevice) -> Unit
    ) {
        val gattCallback = KtGattCallback(
            onConnectionStateChange = { gatt, status, newState ->
                when (newState) {
                    BluetoothGatt.STATE_CONNECTED -> callback(device)
                    // BluetoothGatt.STATE_DISCONNECTED -> devices.remove(androidDevice.id)
                }
            }
        )
        device.gattCallback = gattCallback
        device.gatt = device.androidDevice.connectGatt(context, true, gattCallback)
    }

    actual fun discoverServices(
        device: BluetoothDevice,
        uuids: List<String>,
        callback: (List<BleService>) -> Unit
    ) {
        device.gattCallback?.onServicesDiscovered = { gatt, status ->
            val services = gatt.services.map { BleService(it.uuid.toString(), device) }
            callback(services)
        }
        device.gatt?.discoverServices()
    }

    actual fun discoverCharacteristics(
        service: BleService,
        uuids: List<String>,
        callback: (List<BleCharacteristic>) -> Unit
    ) {
        val bleService = service.device.gatt?.run {
            getService(UUID.fromString(service.id)) ?: error("Service not found ${service.id}")
        } ?: error("Device not connected ${service.device.id}")
        val chars = bleService.characteristics
            .filter { it.uuid.toString() in uuids }
            .map { BleCharacteristic(it.uuid.toString(), it.value, service) }
        callback(chars)
    }

    actual fun setNotificationEnabled(char: BleCharacteristic, callback: (BleCharacteristic) -> Unit) {
        val gatt = getGatt(char.service.device)
        val bleChar = getChar(gatt, char)
        char.service.device.gattCallback?.onCharacteristicChanged = { _, c ->
            callback(BleCharacteristic(c.uuid.toString(), c.value, char.service))
        }
        gatt.setCharacteristicNotification(bleChar, true)
    }

    actual fun serNotificationDisabled(char: BleCharacteristic) {
        val gatt = getGatt(char.service.device)
        val bleChar = getChar(gatt, char)
        char.service.device.gattCallback?.onCharacteristicChanged = null
        gatt.setCharacteristicNotification(bleChar, true)
    }

    private fun getGatt(device: BluetoothDevice): BluetoothGatt {
        return device.gatt ?: error("Device not connected ${device.id}")
    }

    private fun getChar(gatt: BluetoothGatt, char: BleCharacteristic): BluetoothGattCharacteristic {
        val service = gatt.getService(UUID.fromString(char.service.id)) ?: error("Service not found ${char.service.id}")
        return service.getCharacteristic(UUID.fromString(char.id)) ?: error("Char not found ${char.id}")
    }

    private fun getService(gatt: BluetoothGatt, service: BleService): BluetoothGattService {
        return gatt.getService(UUID.fromString(service.id)) ?: error("Service not found ${service.id}")
    }
}