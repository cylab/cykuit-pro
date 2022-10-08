package utils

import kotlinx.cinterop.*
import midi.api.*
import kotlin.math.abs

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


inline fun fade(interval: Int, first: Value<Int>, second: Value<Int>) : Value<Int?> = DynamicValue {
    val start = first()
    val end = second()
    val f_start = abs(interval - (ticks % interval) *2)
    val f_end = abs(-f_start + interval)
    val r_mask = 0xff0000
    val g_mask = 0x00ff00
    val b_mask = 0x0000ff
    when {
        start <= 255 && end <= 255 -> (start * f_start + end * f_end) / interval
        else -> {
            val r = (((start and r_mask) * f_start + (end and r_mask) * f_end) / interval) and r_mask
            val g = (((start and g_mask) * f_start + (end and g_mask) * f_end) / interval) and g_mask
            val b = (((start and b_mask) * f_start + (end and b_mask) * f_end) / interval) and b_mask
            r+g+b
        }
    }

}
