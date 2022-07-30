package midi.api

import jack.JackClient

interface MidiClient {
    val midiIn: MidiIn
    val midiOut: MidiOut
    val midiClock: MidiClock

    fun destroy()

    companion object {
        val default : JackClient by lazy {
            JackClient()
        }
    }
}

