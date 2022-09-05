package jack

import midi.api.MidiContext
import midi.api.MidiEvent

internal class JackContext(private val jack: JackClient) : MidiContext {
    override val midiClock = jack.midiClock
    override var channel = 0

    override fun emit(event: MidiEvent) {
        /** needs no implementation **/
    }

    override fun toString(): String {
        return "JackContext"
    }
}
