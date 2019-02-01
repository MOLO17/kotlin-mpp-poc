package com.molo17.damianogiusti.ui

import com.molo17.damianogiusti.BluetoothAdapter
import com.molo17.damianogiusti.BluetoothDevice

/**
 * Created by Damiano Giusti on 01/02/19.
 */
class DevicesListPresenter(
    private val bluetoothAdapter: BluetoothAdapter
) {
    private var view: DevicesListView? = null
    private var discoveredDevices = mutableMapOf<String, BluetoothDevice>()

    fun attachView(v: DevicesListView) {
        view = v

        bluetoothAdapter.discoverDevices { device ->
            discoveredDevices[device.id] = device
            val uiDevices = discoveredDevices.values.map(::mapToUiDevice)
            view?.showDevices(uiDevices)
        }
    }

    fun detachView() {
        bluetoothAdapter.stopScan()
        view = null
    }
}

private fun mapToUiDevice(device: BluetoothDevice) = UiDevice(
    id = device.id,
    displayableContent = "ID: ${device.id}\nName: ${device.name}"
)