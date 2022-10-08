package midi.core

import midi.api.*

class BufferedMidiContextWrapper(var target: MidiContext?) : MidiContext {
    private val eventBuffer = mutableListOf<Pair<Int, MidiEvent>>()

    override val midiClock: MidiClock
        get() = target?.midiClock ?: throw IllegalStateException("Can't use a clock with uninitialized target")

    private var _channel: Int? = null
    override var channel: Int
        get() = _channel ?: target?.channel ?: 0
        set(value) { _channel = value }

    override fun emit(event: MidiEvent, defer: Int) {
        val ticks = midiClock.ticks
        // buffer events with defer at the timestamp need to eventually be emitted
        when (defer){
            0 -> target?.emit(event, 0)
            else -> eventBuffer.add(ticks + defer to event)
        }
        // emit all deferred events, once their timestamp is reached
        val it = eventBuffer.iterator()
        while (it.hasNext()) {
            val (schedule, event) = it.next()
            if (ticks >= schedule) {
                it.remove()
                target?.emit(event, 0)
            }
        }
    }
}
