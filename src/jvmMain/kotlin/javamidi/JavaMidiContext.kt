package javamidi

import midi.api.MidiContext
import midi.api.MidiEvent

internal class JavaMidiContext(private val client: JavaMidiClient) : MidiContext {
    override val midiClock = client.midiClock
    override var channel = 0

    override fun emit(event: MidiEvent, defer: Int) {
        /** needs no implementation **/
    }

    override fun toString(): String {
        return "JackContext"
    }
}
