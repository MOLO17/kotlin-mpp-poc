package com.molo17.damianogiusti

import platform.CoreBluetooth.*
import platform.Foundation.NSError
import platform.Foundation.NSNumber
import platform.darwin.NSObject

actual class BluetoothAdapter {

    actual var listener: BluetoothAdapterListener? = null

    private var isReady = false
    private var whenReady: ((BluetoothAdapter) -> Unit)? = null
    private var connectedDevice: BluetoothDevice? = null
    private var discoveredServices: List<BleService>? = null

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
            connectedDevice = BluetoothDevice(didConnectPeripheral).also {
                listener?.onStateChange(BleState.Connected(it))
            }
        }

        override fun peripheral(peripheral: CBPeripheral, didDiscoverServices: NSError?) {
            val device = getDeviceOrThrow()
            discoveredServices = peripheral.cbServices.map { BleService(it.UUID.UUIDString, device) }.also {
                listener?.onStateChange(BleState.ServicesDiscovered(device, it))
            }
        }

        override fun peripheral(
            peripheral: CBPeripheral,
            didDiscoverCharacteristicsForService: CBService,
            error: NSError?
        ) {
            val device = getDeviceOrThrow()
            val serviceId = didDiscoverCharacteristicsForService.UUID.UUIDString
            val service = checkNotNull(discoveredServices).first { it.id == serviceId }
            val chars = didDiscoverCharacteristicsForService.cbChars.map { char -> BleCharacteristic(char, service) }
            listener?.onStateChange(BleState.CharacteristicsDiscovered(device, chars))
        }

        override fun peripheral(
            peripheral: CBPeripheral,
            didUpdateValueForCharacteristic: CBCharacteristic,
            error: NSError?
        ) {
            val serviceId = didUpdateValueForCharacteristic.UUID.UUIDString
            val service = checkNotNull(discoveredServices).first { it.id == serviceId }
            listener?.onStateChange(
                BleState.CharacteristicChanged(
                    getDeviceOrThrow(),
                    BleCharacteristic(didUpdateValueForCharacteristic, service)
                )
            )
        }
    }

    private val manager = CBCentralManager()
    private var onDeviceReceived: ((BluetoothDevice) -> Unit)? = null

    init {
        manager.delegate = delegateImpl
    }

    private fun getDeviceOrThrow(): BluetoothDevice {
        val device = connectedDevice
        check(device != null) { "Device is not connected!" }
        return device
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

    actual fun findBondedDevices(callback: (List<BluetoothDevice>) -> Unit) {
        manager.retrieveConnectedPeripheralsWithServices(listOf("180A"))
            .mapNotNull { it as? CBPeripheral }
            .map { BluetoothDevice(it) }
            .also(callback)
    }

    actual fun connect(device: BluetoothDevice) {
        manager.connectPeripheral(device.peripheral, null)
    }

    actual fun disconnect() {
        manager.cancelPeripheralConnection(getDeviceOrThrow().peripheral)
    }

    actual fun discoverServices() {
        getDeviceOrThrow().peripheral.discoverServices(null)
    }

    actual fun discoverCharacteristics(service: BleService) {
        val s = service.device.peripheral.cbServices.first { it.UUID.UUIDString == service.id }
        service.device.peripheral.discoverCharacteristics(null, s)
    }

    actual fun setNotificationEnabled(char: BleCharacteristic) {
        val cbService = char.service.device.peripheral.cbServices.first { it.UUID.UUIDString == char.service.id }
        val c = cbService.cbChars.first { it.UUID.UUIDString == char.id }
        char.service.device.peripheral.setNotifyValue(true, c)
    }

    actual fun setNotificationDisabled(char: BleCharacteristic) {
        val cbService = char.service.device.peripheral.cbServices.first { it.UUID.UUIDString == char.service.id }
        val c = cbService.cbChars.first { it.UUID.UUIDString == char.id }
        char.service.device.peripheral.setNotifyValue(true, c)
    }
}