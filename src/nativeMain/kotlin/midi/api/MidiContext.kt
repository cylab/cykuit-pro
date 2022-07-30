package midi.api

interface MidiContext {
    val midiClock: MidiClock
    var channel: Int

    fun emit(events: Collection<MidiEvent>) = events.forEach { emit(it) }
    fun emit(vararg events: MidiEvent) = events.forEach { emit(it) }
    fun emit(event: MidiEvent)
}
