package com.molo17.damianogiusti

import platform.UIKit.UIDevice

actual fun platformName(): String {
    return UIDevice.currentDevice.run { "$systemName $systemVersion" }
}

