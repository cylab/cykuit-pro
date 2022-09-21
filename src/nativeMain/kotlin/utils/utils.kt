package utils

import kotlinx.cinterop.*
import midi.api.MidiContext

fun CPointer<CPointerVar<ByteVar>>.toKStrings() = generateSequence(0) { it+1 }
    .map { this[it] }
    .takeWhile { it != null }
//    .map { ptr -> ptr?.toKString().also { jack_free(ptr) } }
    .mapNotNull { ptr -> ptr?.toKString() }
    .toList()

inline fun <reified R> Iterable<*>.instanceOf(number: Int = 0) = filterIsInstance<R>()[number]


inline fun <T> blink(interval: Int, supplier: () -> Pair<T?, T?>): MidiContext.() -> T? {
    val values = supplier()
    return {
        when {
            midiClock.ticks % interval <= (interval / 2) -> values.first
            else -> values.second
        }
    }
}
