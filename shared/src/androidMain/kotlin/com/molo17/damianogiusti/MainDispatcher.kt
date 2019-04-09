package com.molo17.damianogiusti

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlin.coroutines.CoroutineContext

actual object MainDispatcher : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        // In the JVM multi-threading is supported so the implementation can rely on the Main dispatcher.
        Dispatchers.Main.dispatch(context, block)
    }
}