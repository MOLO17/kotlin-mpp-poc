package com.molo17.damianogiusti

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlin.coroutines.CoroutineContext

actual object MainDispatcher : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        // Dispatch in the iOS main queue.
        dispatch_async(dispatch_get_main_queue(), block::run)
    }
}