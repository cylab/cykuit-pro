package midi.core

import midi.api.MidiEvent
import midi.api.MidiContext
import midi.api.MidiFun

open class MidiFunImpl(val fn: MidiFun) : MidiFun {
    override fun MidiContext.processInContext(event: MidiEvent) = with(fn) { processInContext(event) }
}
