package controller.events

import controller.Button
import controller.MidiControl
import midi.core.justify

sealed class ButtonEvent(val button: Button) : ControlEvent {
    override val control: MidiControl; get() = button

    override fun toString() = justify(
        this::class.simpleName to -15,
        "button:" to 10, button.name to -22,
        "number:" to 10, button.number to 5,
    )
}

class ButtonPressed(button: Button) : ButtonEvent(button), ControlPressed

class ButtonReleased(button: Button) : ButtonEvent(button), ControlReleased
