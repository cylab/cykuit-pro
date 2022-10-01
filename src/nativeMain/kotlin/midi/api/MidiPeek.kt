package midi.api

import midi.core.MidiFunImpl

class MidiPeek(fn: MidiContext.(MidiEvent) -> Unit) : MidiFunImpl({ fn(it); emit(it) })
inline fun <reified T> MidiPeek(crossinline fn: MidiContext.(T) -> Unit) = MidiPeek { if(it is T) fn(it); emit(it) }
