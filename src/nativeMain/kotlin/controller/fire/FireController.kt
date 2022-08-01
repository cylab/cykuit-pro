package controller.fire

import midi.activity.MidiActivity
import midi.api.MidiContext
import midi.api.MidiEvent
import midi.core.controlChange
import midi.core.sixteenth
import midi.core.sysEx

class FireController : MidiActivity("Akai Fire Pro") {
    override fun onClock(midi: MidiContext, event: MidiEvent) = with(midi) {
        if (midiClock.ticks % 1.sixteenth == 0)
            return
        controlChange(0x1b, 3)

        firePadColors(*IntArray(64) { (it shl 24) + 0x0A_0C_18 })
        firePadColors(
            0x06_06_02_00, 0x08_06_02_00,
            0x15_60_20_00, 0x16_08_0C_00, 0x17_60_20_00, 0x18_08_0C_00, 0x19_60_20_00,
            0x26_60_20_00, 0x27_60_20_00, 0x28_60_20_00,
            0x37_08_04_00
        )
    }
}

@ExperimentalUnsignedTypes
fun MidiContext.firePadColors(vararg colorData: Int) = sysEx(0x47_7F_43, 3 + 4 * colorData.size) { outIndex ->
    val colorByteIndex = (outIndex - 3) % 4
    val colorIndex = (outIndex - 3) / 4
    val padColor = colorData[colorIndex]
    when (outIndex) {
        0 -> 0x65u // write pad command
        1 -> (colorData.size * 4 shr 7).toUByte() // payload length high 7bits
        2 -> (colorData.size * 4 and 0x7F).toUByte() // payload length low 7bits
        else -> when (colorByteIndex) {
            0 -> (padColor shr 24 and 0x7F).toUByte()   // 7bit pad index
            1 -> (padColor shr 17 and 0x7F).toUByte()   // 7bit r
            2 -> (padColor shr 9 and 0x7F).toUByte()    // 7bit g
            else -> (padColor shr 1 and 0x7F).toUByte() // 7bit b
        }
    }
}
