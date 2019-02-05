package com.molo17.damianogiusti

import platform.CoreBluetooth.*
import platform.Foundation.NSError
import platform.Foundation.NSNumber
import platform.darwin.NSObject

actual class BluetoothAdapter {

    private var isReady = false
    private var whenReady: ((BluetoothAdapter) -> Unit)? = null

    private val delegateImpl = object : NSObject(), CBCentralManagerDelegateProtocol, CBPeripheralDelegateProtocol {
        override fun centralManagerDidUpdateState(central: CBCentralManager) {
            when (central.state) {
                CBCentralManagerStatePoweredOn -> whenReady?.invoke(this@BluetoothAdapter)
            }
        }

        override fun centralManager(
            central: CBCentralManager,
            didDiscoverPeripheral: CBPeripheral,
            advertisementData: Map<Any?, *>,
            RSSI: NSNumber
        ) {
            val device = BluetoothDevice(didDiscoverPeripheral)
            onDeviceReceived?.invoke(device)
        }

        override fun centralManager(central: CBCentralManager, didConnectPeripheral: CBPeripheral) {
            onDeviceConnected?.invoke(BluetoothDevice(didConnectPeripheral))
        }

        override fun peripheral(peripheral: CBPeripheral, didDiscoverServices: NSError?) {
            onServicesDiscovered?.invoke(BluetoothDevice(peripheral))
        }

        override fun peripheral(
            peripheral: CBPeripheral,
            didDiscoverCharacteristicsForService: CBService,
            error: NSError?
        ) {
            onCharsDiscovered?.invoke(BluetoothDevice(peripheral))
        }

        override fun peripheral(
            peripheral: CBPeripheral,
            didUpdateValueForCharacteristic: CBCharacteristic,
            error: NSError?
        ) {

        }
    }

    private val manager = CBCentralManager()
    private var onDeviceReceived: ((BluetoothDevice) -> Unit)? = null
    private var onDeviceConnected: ((BluetoothDevice) -> Unit)? = null
    private var onServicesDiscovered: ((BluetoothDevice) -> Unit)? = null
    private var onCharsDiscovered: ((BluetoothDevice) -> Unit)? = null
    private var onCharUpdated: ((BluetoothDevice, CBCharacteristic) -> Unit)? = null

    init {
        manager.delegate = delegateImpl
    }

    ///////////////////////////////////////////////////////////////////////////
    // Actual declarations
    ///////////////////////////////////////////////////////////////////////////

    actual fun discoverDevices(callback: (BluetoothDevice) -> Unit) {
        if (isReady) {
            manager.scanForPeripheralsWithServices(null, null)
            onDeviceReceived = callback
        } else {
            whenReady = {
                whenReady = null
                manager.scanForPeripheralsWithServices(null, null)
                onDeviceReceived = callback
            }
        }
    }

    actual fun stopScan() {
        manager.stopScan()
        onDeviceReceived = null
    }

    actual fun connect(
        device: BluetoothDevice,
        callback: (BluetoothDevice) -> Unit
    ) {
        onDeviceConnected = { d ->
            onDeviceConnected = null
            callback(d)
        }
        manager.connectPeripheral(device.peripheral, null)
    }

    actual fun discoverServices(
        device: BluetoothDevice,
        uuids: List<String>,
        callback: (List<BleService>) -> Unit
    ) {
        onServicesDiscovered = { d ->
            onServicesDiscovered = null
            val services = d.peripheral.cbServices.map { BleService(it.UUID.UUIDString, d) }
            callback(services)
        }
        device.peripheral.discoverServices(uuids)
    }

    actual fun discoverCharacteristics(
        service: BleService,
        uuids: List<String>,
        callback: (List<BleCharacteristic>) -> Unit
    ) {
        onCharsDiscovered = { d ->
            onCharsDiscovered = null
            val cbService = d.peripheral.cbServices.first { it.UUID.UUIDString == service.id }
            val chars = cbService.cbChars.map { char -> BleCharacteristic(char, service) }
            callback(chars)
        }
    }

    actual fun setNotificationEnabled(
        char: BleCharacteristic,
        callback: (BleCharacteristic) -> Unit
    ) {
        onCharUpdated = { _, c ->
            callback(BleCharacteristic(c, char.service))
        }
        val cbService = char.service.device.peripheral.cbServices.first { it.UUID.UUIDString == char.service.id }
        val c = cbService.cbChars.first { it.UUID.UUIDString == char.id }
        char.service.device.peripheral.setNotifyValue(true, c)
    }

    actual fun serNotificationDisabled(char: BleCharacteristic) {
        onCharUpdated = null
        val cbService = char.service.device.peripheral.cbServices.first { it.UUID.UUIDString == char.service.id }
        val c = cbService.cbChars.first { it.UUID.UUIDString == char.id }
        char.service.device.peripheral.setNotifyValue(true, c)
    }
}