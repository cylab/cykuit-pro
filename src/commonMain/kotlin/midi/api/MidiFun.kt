package midi.api

fun interface MidiFun {
    fun MidiContext.process(event: MidiEvent)
}

