package controller.events

import controller.Button
import controller.buttons.*
import midi.api.NOOPEvent.channel
import midi.api.NOOPEvent.defer
import midi.api.NOOPEvent.type
import midi.core.justify

sealed class ButtonEvent(val button: Button) : ControlEvent {
    override val defer: Int = 0
    override fun toString() = justify(
        this::class.simpleName to -15,
        "button:" to 10, button.name to -22,
        "number:" to 10, button.number to 5,
        "defer:" to 10, defer to 5
    )
}

class ButtonPressed(button: Button) : ButtonEvent(button)

class ButtonReleased(button: Button) : ButtonEvent(button)
