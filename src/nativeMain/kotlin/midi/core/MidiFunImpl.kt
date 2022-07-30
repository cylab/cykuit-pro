package midi.core

import midi.api.MidiEvent
import midi.api.MidiContext
import midi.api.MidiFun

open class MidiFunImpl(val fn: MidiFun) : MidiFun {
    override fun MidiContext.process(event: MidiEvent) = with(fn) { process(event) }
}
