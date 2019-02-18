package com.molo17.damianogiusti

import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBCentralManagerStatePoweredOn
import platform.CoreBluetooth.CBPeripheral
import platform.Foundation.NSNumber
import platform.darwin.NSObject

actual class BluetoothAdapter {

    private var isReady = false
    private var whenReady: ((BluetoothAdapter) -> Unit)? = null

    private val delegateImpl = object : NSObject(),
        CBCentralManagerDelegateProtocol {
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
            val device = BluetoothDevice(
                id = didDiscoverPeripheral.identifier.UUIDString,
                name = didDiscoverPeripheral.name.orEmpty()
            )
            onDeviceReceived?.invoke(device)
        }
    }

    private val manager = CBCentralManager()
    private var onDeviceReceived: ((BluetoothDevice) -> Unit)? = null

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
                manager.scanForPeripheralsWithServices(null, null)
                onDeviceReceived = callback
                whenReady = null
            }
        }
    }

    actual fun stopScan() {
        manager.stopScan()
        onDeviceReceived = null
    }
}