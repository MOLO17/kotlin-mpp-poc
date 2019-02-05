package com.molo17.damianogiusti

import io.ktor.client.engine.ios.toByteArray
import platform.CoreBluetooth.*

actual data class BluetoothDevice(
    actual val id: String,
    actual val name: String,
    internal val peripheral: CBPeripheral
)

internal fun BluetoothDevice(peripheral: CBPeripheral) = BluetoothDevice(
    id = peripheral.identifier.UUIDString,
    name = peripheral.name.orEmpty(),
    peripheral = peripheral
)

internal val CBPeripheral.cbServices get() = services().orEmpty().map { it as CBMutableService }
internal val CBService.cbChars get() = characteristics().orEmpty().map { it as CBMutableCharacteristic }

internal fun BleCharacteristic(char: CBCharacteristic, service: BleService): BleCharacteristic {
    val id = char.UUID.UUIDString
    val value = char.value?.toByteArray() ?: ByteArray(0)
    return BleCharacteristic(id, value, service)
}