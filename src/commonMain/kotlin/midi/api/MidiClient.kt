package midi.api

interface MidiClient {
    val midiIns: MidiIns
    val midiOuts: MidiOuts
    val midiClock: MidiClock

    fun destroy()
}

