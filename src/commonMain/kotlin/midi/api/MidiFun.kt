package midi.api

fun interface MidiFun {
    fun MidiContext.processInContext(event: MidiEvent)
}

