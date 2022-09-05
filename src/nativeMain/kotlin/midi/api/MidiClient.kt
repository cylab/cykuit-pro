package midi.api

import jack.JackClient

interface MidiClient {
    val midiIns: MidiIns
    val midiOuts: MidiOuts
    val midiClock: MidiClock

    fun destroy()

    companion object {
        val default : MidiClient by lazy { JackClient() }
    }
}

