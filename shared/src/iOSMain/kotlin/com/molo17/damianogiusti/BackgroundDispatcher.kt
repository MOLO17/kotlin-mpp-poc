package com.molo17.damianogiusti

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import kotlin.coroutines.CoroutineContext

actual object BackgroundDispatcher : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        // Currently coroutines for Kotlin/Native do not support multi-threading computation.
        // On the iOS platform the dispatching is implemented in the main thread.
        MainDispatcher.dispatch(context, block)
    }
}