package midi.core

import midi.api.MidiEvent
import midi.api.MidiContext
import midi.api.MidiFun

abstract class MidiFunByGroup() : MidiFun {
    protected abstract val group : MidiGroup
    override fun MidiContext.process(event: MidiEvent) = with(group) { process(event) }
}
