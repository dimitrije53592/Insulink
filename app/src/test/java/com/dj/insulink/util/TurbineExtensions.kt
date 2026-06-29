package com.dj.insulink.util

import app.cash.turbine.ReceiveTurbine

/**
 * Awaits items until one satisfies [predicate], returning it. Useful for `stateIn` flows whose
 * initial value and computed value may arrive as separate (or conflated) emissions.
 */
suspend fun <T> ReceiveTurbine<T>.awaitUntil(predicate: (T) -> Boolean): T {
    while (true) {
        val item = awaitItem()
        if (predicate(item)) return item
    }
}
