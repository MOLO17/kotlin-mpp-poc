package com.molo17.damianogiusti.ui

import com.molo17.damianogiusti.*

private const val SERVICE_BUTTON_PRESSED = "FEE0"
private const val CHAR_BUTTON_PRESSED = "00000010"
private const val SERVICE_HEART_RATE = "180D"
private const val CHAR_HEART_RATE = "2A37"

/**
 * Created by Damiano Giusti on 01/02/19.
 */
class DevicesListPresenter(
    private val bluetoothAdapter: BluetoothAdapter
) : BluetoothAdapterListener {

    private var view: DevicesListView? = null

    private var characteristic: BleCharacteristic? = null

    fun attachView(v: DevicesListView) {
        view = v
        bluetoothAdapter.listener = this
        bluetoothAdapter.findBondedDevices { devices ->
            val uiDevices = devices.map(::mapToUiDevice)
            view?.showDevices(uiDevices)

            val device = devices.find { it.name.startsWith("MI") }
            if (device == null) {
                view?.showMessage("MI Band not bonded to your ${platformName()} device.")
            } else {
                view?.showMessage("Connecting device ${device.id}")
                bluetoothAdapter.connect(device)
            }
        }
    }

    fun detachView() {
        characteristic?.let { char ->
            bluetoothAdapter.setNotificationDisabled(char)
            bluetoothAdapter.disconnect()
            bluetoothAdapter.listener = null
        }
        characteristic = null
        view?.showDevices(emptyList())
        view = null
    }

    override fun onStateChange(state: BleState) {
        when (state) {
            is BleState.Connected -> {
                view?.showMessage("Connected device ${state.device.name}")
                bluetoothAdapter.discoverServices()
            }
            is BleState.Disconnected -> {
                view?.showMessage("Device disconnected ${state.device.name}")
            }
            is BleState.ServicesDiscovered -> {
                val service = state.services.first { it.id.contains(SERVICE_BUTTON_PRESSED, ignoreCase = true) }
                view?.showMessage("Service discovered ${service.id}")
                bluetoothAdapter.discoverCharacteristics(service)
            }
            is BleState.CharacteristicsDiscovered -> {
                val char = state.chars.first { it.id.contains(CHAR_BUTTON_PRESSED, ignoreCase = true) }
                view?.showMessage("Char discovered ${char.id}")
                bluetoothAdapter.setNotificationEnabled(char)
            }
            is BleState.CharacteristicChanged -> onButtonPressed(state.char)
        }.let { /* exhaustive */ }
    }

    private fun onButtonPressed(characteristic: BleCharacteristic) {
        view?.showMessage("Button pressed!")
    }
}

private fun mapToUiDevice(device: BluetoothDevice) = UiDevice(
    id = device.id,
    displayableContent = "ID: ${device.id}\nName: ${device.name}"
)