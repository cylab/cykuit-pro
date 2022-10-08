package midi.core

import midi.api.MidiEvent
import midi.api.MidiContext

open class MidiFilter(predicate: MidiContext.(MidiEvent) -> Boolean) : MidiFunImpl({
    if (predicate(it)) {
        emit(it)
    }
})
