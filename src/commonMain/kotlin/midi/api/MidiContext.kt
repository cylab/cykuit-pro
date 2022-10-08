package midi.api

interface MidiContext {
    val midiClock: MidiClock
    var channel: Int

    fun emit(event: MidiEvent, defer: Int = 0)
}
