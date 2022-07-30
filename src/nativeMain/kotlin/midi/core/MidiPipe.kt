package midi.core

import midi.api.MidiContext
import midi.api.MidiEvent
import midi.api.MidiFun

class MidiPipe private constructor(midiFuns: List<MidiFun>) : MidiFun, MutableList<MidiFun> by ArrayList(midiFuns) {
    constructor() : this(listOf<MidiFun>())
    constructor(fn: MidiFun) : this(listOf(fn))
    constructor(vararg functs: MidiFun) : this(functs.toList())
    constructor(functs: Collection<MidiFun>) : this(functs.toList())

    private val eventBuffer1 = mutableListOf<MidiEvent>()
    private val eventBuffer2 = mutableListOf<MidiEvent>()
    private val midi = PipeContext(eventBuffer2)

    // workaround to call the "extension-function" from a subclass: https://youtrack.jetbrains.com/issue/KT-11488
    protected fun process(parentContext: MidiContext, event: MidiEvent) {
        var currentIn = eventBuffer1
        midi.out = eventBuffer2
        midi.delegate.target = parentContext
        currentIn.clear()
        currentIn.add(event)
        forEach { midiFun ->
            currentIn.forEach {
                midiFun.process(midi, it)
            }
            currentIn.clear()
            val swap = midi.out
            midi.out = currentIn
            currentIn = swap
        }
        currentIn.forEach {
            midi.delegate.emit(it)
        }
        currentIn.clear()
    }

    override fun MidiContext.process(event: MidiEvent) = process(this, event)

    private class PipeContext(
        var out: MutableList<MidiEvent>,
        val delegate: MidiContextStateWrapper = MidiContextStateWrapper(null)
    ) : MidiContext by delegate {
        override fun emit(event: MidiEvent) {
            out.add(event)
        }
    }

    fun addAll(vararg next: MidiFun) = addAll(next)
}
