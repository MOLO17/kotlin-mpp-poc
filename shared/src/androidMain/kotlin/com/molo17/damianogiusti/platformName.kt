package com.molo17.damianogiusti

import android.os.Build
import android.bluetooth.BluetoothAdapter as AndroidBluetoothAdapter

actual fun platformName(): String {
    return "Android ${Build.VERSION.RELEASE}"
}

