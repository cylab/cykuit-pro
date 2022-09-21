package controller.events

import controller.Pad
import midi.core.justify

abstract class PadEvent : ControlEvent {
    abstract val pad: Pad
    abstract val velocity: Int

    override fun toString() = justify(
        this::class.simpleName to -15,
        "pad:" to 10, "${pad.name}" to -22,
        "number:" to 10, pad.number to 5,
        "defer:" to 10, defer to 5
    )
}

class PadPressed(
    override val pad: Pad,
    override val velocity: Int,
    override val defer: Int = 0
) : PadEvent()

class PadReleased(
    override val pad: Pad,
    override val velocity: Int,
    override val defer: Int = 0
) : PadEvent()
