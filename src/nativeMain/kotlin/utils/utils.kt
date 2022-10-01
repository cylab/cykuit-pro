package utils

import kotlinx.cinterop.*
import midi.api.*

fun CPointer<CPointerVar<ByteVar>>.toKStrings() = generateSequence(0) { it + 1 }
    .map { this[it] }
    .takeWhile { it != null }
//    .map { ptr -> ptr?.toKString().also { jack_free(ptr) } }
    .mapNotNull { ptr -> ptr?.toKString() }
    .toList()


fun interface Value<T> {
    operator fun invoke(): T
}

class DynamicValue<T>(private val generator: MidiClock.() -> T) : Value<T> {
    override fun invoke(): T = supply(StoppedClock)
    fun supply(clock: MidiClock): T = clock.generator()
}

inline fun <T> blink(interval: Int, first: T, second: T) = DynamicValue {
    when {
        ticks % interval <= (interval / 2) -> first
        else -> second
    }
}

inline fun <T> blink(interval: Int, first: Value<T>, second: Value<T>) = blink(interval, first(), second())
