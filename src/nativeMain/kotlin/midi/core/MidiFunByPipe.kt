package midi.core

import midi.api.MidiEvent
import midi.api.MidiContext
import midi.api.MidiFun

abstract class MidiFunByPipe() : MidiFun {
    protected abstract val pipe : MidiPipe
    override fun MidiContext.process(event: MidiEvent) = with(pipe) { process(event) }
}
