package controller.events

import controller.MidiControl
import midi.api.MidiEvent

interface ControlEvent : MidiEvent {
    val control: MidiControl
}
interface ControlPressed : ControlEvent
interface ControlReleased : ControlEvent
