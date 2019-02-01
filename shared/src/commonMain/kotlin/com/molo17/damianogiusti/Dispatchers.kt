package com.molo17.damianogiusti

import kotlinx.coroutines.CoroutineDispatcher

/** A platform-specified dispatcher which computes in the main UI thread. */
expect object MainDispatcher: CoroutineDispatcher

/** A platform-specified dispatcher which computes in a background thread. */
expect object BackgroundDispatcher: CoroutineDispatcher