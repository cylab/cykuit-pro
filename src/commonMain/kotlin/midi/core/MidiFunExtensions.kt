package midi.core

import midi.api.MidiEvent
import midi.api.MidiContext
import midi.api.MidiFun

fun MidiFun.process(midi: MidiContext, event: MidiEvent) = midi.process(event)
