package midi.core

import midi.api.MidiClock
import midi.api.MidiContext
import midi.api.MidiEvent

class MidiContextStateWrapper(var target: MidiContext?) : MidiContext {
    override val midiClock: MidiClock
        get() = target?.midiClock ?: throw IllegalStateException("Can't use a clock with uninitialized target")

    private var _channel: Int? = null
    override var channel: Int
        get() = _channel ?: target?.channel ?: 0
        set(value) { _channel = value }

    override fun emit(event: MidiEvent) {
        target?.emit(event)
    }
}
