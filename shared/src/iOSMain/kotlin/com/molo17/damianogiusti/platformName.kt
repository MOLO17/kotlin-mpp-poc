package com.molo17.damianogiusti

import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBCentralManagerStatePoweredOn
import platform.CoreBluetooth.CBPeripheral
import platform.Foundation.NSNumber
import platform.UIKit.UIDevice
import platform.darwin.NSObject

actual fun platformName(): String {
    return UIDevice.currentDevice.run { "$systemName $systemVersion" }
}

actual class BluetoothAdapter(
    actual var whenReady: ((BluetoothAdapter) -> Unit)?
) {

    private val devices = mutableListOf<BluetoothDevice>()

    private val delegateImpl = object : NSObject(), CBCentralManagerDelegateProtocol {
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
            devices.add(device)
            callback?.invoke(devices)
        }
    }

    private val manager = CBCentralManager()
    private var callback: ((List<BluetoothDevice>) -> Unit)? = null

    init {
        manager.delegate = delegateImpl
    }

    ///////////////////////////////////////////////////////////////////////////
    // Privagte
    ///////////////////////////////////////////////////////////////////////////

    actual fun discoverDevices(callback: (List<BluetoothDevice>) -> Unit) {
        manager.scanForPeripheralsWithServices(null, null)
        this.callback = callback
    }

    actual fun stopScan() {
        manager.stopScan()
        callback = null
    }
}