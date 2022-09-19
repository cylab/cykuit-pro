package controller.events

import controller.Pad

abstract class PadEvent : ControlEvent {
    abstract val pad: Pad
    abstract val velocity: Int

    override fun toString() =
        "${this::class.simpleName}\tpad:${pad.name}\tnumber:${pad.index}}\tvelocity:${velocity}\tdefer:$defer"
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
