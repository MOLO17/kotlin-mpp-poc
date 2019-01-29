package com.molo17.damianogiusti

import android.os.Build

actual fun platformName(): String {
    return "Android ${Build.VERSION.RELEASE}"
}

actual class BluetoothAdapter actual constructor(whenReady: (BluetoothAdapter) -> Unit) {
    actual fun discoverDevices(callback: (List<BluetoothDevice>) -> Unit) {
    }

    actual fun stopScan() {
    }

}
