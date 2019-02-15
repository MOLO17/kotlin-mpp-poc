package com.molo17.damianogiusti

import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.util.*
import android.bluetooth.BluetoothDevice as AndroidBleDevice

actual class BluetoothAdapter(
    private val context: Context
) : BluetoothGattCallback() {

    private val mainThreadHandler = Handler(Looper.getMainLooper())

    private val bluetoothManager: BluetoothManager
        get() = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

    private val bluetoothAdapter: android.bluetooth.BluetoothAdapter
        get() = bluetoothManager.adapter

    private var handler: ((BluetoothDevice) -> Unit)? = null

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            val device = result?.device
            if (device != null) {
                handler?.invoke(BluetoothDevice(device.address, device.name ?: "", device))
            }
        }
    }

    private var connectedDevice: BluetoothDevice? = null
    private var discoveredServices: List<BleService>? = null

    ///////////////////////////////////////////////////////////////////////////
    // Actual declarations
    ///////////////////////////////////////////////////////////////////////////

    actual var listener: BluetoothAdapterListener? = null

    actual fun discoverDevices(callback: (BluetoothDevice) -> Unit) {
        handler = callback
        bluetoothAdapter.bluetoothLeScanner?.startScan(scanCallback)
    }

    actual fun stopScan() {
        bluetoothAdapter.bluetoothLeScanner?.stopScan(scanCallback)
        handler = null
    }

    actual fun findBondedDevices(callback: (List<BluetoothDevice>) -> Unit) {
        bluetoothManager.getConnectedDevices(BluetoothProfile.GATT).filterNotNull()
            .map { BluetoothDevice(it.address, it.name, it) }
            .also(callback)
    }

    actual fun connect(device: BluetoothDevice) {
        val gattCallback = KtGattCallback(this, onConnectionStateChange = { _, _, newState ->
            mainThreadHandler.post {
                when (newState) {
                    BluetoothGatt.STATE_CONNECTED -> {
                        connectedDevice = device
                        listener?.onStateChange(BleState.Connected(device))
                    }
                    BluetoothGatt.STATE_DISCONNECTED -> {
                        listener?.onStateChange(BleState.Disconnected(getDeviceOrThrow()))
                        connectedDevice = null
                    }
                }
            }
        })
        device.gatt = device.androidDevice.connectGatt(context, false, gattCallback)
    }

    actual fun disconnect() {
        connectedDevice?.gatt?.disconnect()
        connectedDevice = null
        discoveredServices = null
    }

    actual fun discoverServices() {
        val device = getDeviceOrThrow()
        device.gatt?.discoverServices()
    }

    actual fun discoverCharacteristics(service: BleService) {
        val device = getDeviceOrThrow()
        val bleService = service.device.gatt?.run {
            getService(UUID.fromString(service.id)) ?: error("Service not found ${service.id}")
        } ?: error("Device not connected ${service.device.id}")
        val chars = bleService.characteristics.map { BleCharacteristic(it, service) }
        listener?.onStateChange(BleState.CharacteristicsDiscovered(device, chars))
    }

    actual fun setNotificationEnabled(char: BleCharacteristic) {
        val gatt = getGatt(char.service.device)
        val bleChar = getChar(gatt, char)
        check(gatt.setCharacteristicNotification(bleChar, true)) { "Characteristic not written!" }
    }

    actual fun setNotificationDisabled(char: BleCharacteristic) {
        val gatt = getGatt(char.service.device)
        val bleChar = getChar(gatt, char)
        check(gatt.setCharacteristicNotification(bleChar, false)) { "Characteristic not written!" }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Gatt Callback
    ///////////////////////////////////////////////////////////////////////////

    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        mainThreadHandler.post {
            val device = getDeviceOrThrow()
            discoveredServices = gatt.services.map { BleService(it.uuid.toString(), device) }.also {
                listener?.onStateChange(BleState.ServicesDiscovered(device, it))
            }
        }
    }

    override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
        mainThreadHandler.post {
            val device = getDeviceOrThrow()
            val services = checkNotNull(discoveredServices)
            val service = services.first { it.id == characteristic.service.uuid.toString() }
            val char = BleCharacteristic(characteristic, service)
            listener?.onStateChange(BleState.CharacteristicChanged(device, char))
        }

    }

    private fun getGatt(device: BluetoothDevice): BluetoothGatt {
        return device.gatt ?: error("Device not connected ${device.id}")
    }

    private fun getChar(gatt: BluetoothGatt, char: BleCharacteristic): BluetoothGattCharacteristic {
        val service = gatt.getService(UUID.fromString(char.service.id)) ?: error("Service not found ${char.service.id}")
        return service.getCharacteristic(UUID.fromString(char.id)) ?: error("Char not found ${char.id}")
    }

    private fun getDeviceOrThrow(): BluetoothDevice = checkNotNull(connectedDevice) { "Device is not connected!" }
}

fun log(msg: String) = Log.d("BLE_STATE", msg)