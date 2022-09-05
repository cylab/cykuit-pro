package controller.fire

import midi.api.MidiContext
import midi.core.sysEx

fun MidiContext.firePadColors(vararg colorData: Int) = firePadColors(colorData.size) { colorData[it] }

fun MidiContext.firePadColors(colorData: List<Int>) = firePadColors(colorData.size) { colorData[it] }

private fun MidiContext.firePadColors(size: Int, colorSupplier: (Int) -> Int) {
    if (size == 0) {
        return
    }
    sysEx(0x47_7F_43, 3 + 4 * size) { outIndex ->
        val colorByteIndex = (outIndex - 3) % 4
        val colorIndex = (outIndex - 3) / 4
        val padColor = colorSupplier(colorIndex)
        when (outIndex) {
            0 -> 0x65u // write pad command
            1 -> (size * 4 shr 7).toUByte() // payload length high 7bits
            2 -> (size * 4 and 0x7F).toUByte() // payload length low 7bits
            else -> when (colorByteIndex) {
                0 -> (padColor shr 24 and 0x7F).toUByte()   // 7bit pad index
                1 -> (padColor shr 17 and 0x7F).toUByte()   // 7bit r
                2 -> (padColor shr 9 and 0x7F).toUByte()    // 7bit g
                else -> (padColor shr 1 and 0x7F).toUByte() // 7bit b
            }
        }
    }
}
