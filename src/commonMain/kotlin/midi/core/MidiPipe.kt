package midi.core

import midi.api.*

open class MidiPipe private constructor(midiFuns: List<MidiFun>) : MidiFun, MutableList<MidiFun> by ArrayList(midiFuns) {
    constructor() : this(listOf<MidiFun>())
    constructor(fn: MidiFun) : this(listOf(fn))
    constructor(vararg functs: MidiFun) : this(functs.toList())
    constructor(functs: Collection<MidiFun>) : this(functs.toList())

    private val eventBuffer1 = mutableListOf<MidiEvent>()
    private val eventBuffer2 = mutableListOf<MidiEvent>()
    private val pipeContext = PipeContext(eventBuffer2)
    private val contexts = mutableListOf<BufferedMidiContextWrapper>()

    // workaround to call the "extension-function" from a subclass: https://youtrack.jetbrains.com/issue/KT-11488
    protected fun process(parentContext: MidiContext, event: MidiEvent) {
        when {
            size > contexts.size -> (contexts.size until size).forEach { contexts.add(BufferedMidiContextWrapper(pipeContext)) }
            size < contexts.size -> (size until contexts.size).forEach { contexts.removeLast() }
        }

        var currentIn = eventBuffer1
        pipeContext.out = eventBuffer2
        pipeContext.delegate.target = parentContext
        currentIn.clear()
        currentIn.add(event)
        forEachIndexed { i, midiFun ->
            currentIn.forEach {
                midiFun.process(contexts[i], it)
            }
            currentIn.clear()
            val swap = pipeContext.out
            pipeContext.out = currentIn
            currentIn = swap
        }
        currentIn.forEach {
            pipeContext.delegate.emit(it)
        }
        currentIn.clear()
    }

    override fun MidiContext.process(event: MidiEvent) = process(this, event)

    private class PipeContext(
        var out: MutableList<MidiEvent>,
        val delegate: BufferedMidiContextWrapper = BufferedMidiContextWrapper(null)
    ) : MidiContext by delegate {
        override fun emit(event: MidiEvent, defer: Int) {
            out.add(event)
        }
    }

    fun addAll(vararg next: MidiFun) = addAll(next)
}

open class _MidiPipe private constructor(midiFuns: List<MidiFun>) : MidiFun, MutableList<MidiFun> by ArrayList(midiFuns) {
    constructor() : this(listOf<MidiFun>())
    constructor(fn: MidiFun) : this(listOf(fn))
    constructor(vararg functs: MidiFun) : this(functs.toList())
    constructor(functs: Collection<MidiFun>) : this(functs.toList())

    private val eventBuffer1 = mutableListOf<MidiEvent>()
    private val eventBuffer2 = mutableListOf<MidiEvent>()
    private val pipeContext = PipeContext(eventBuffer2)

    private val contexts = mutableListOf<BufferedMidiContextWrapper>()

    override fun MidiContext.process(event: MidiEvent) = process(this, event)

    // workaround to call the "extension-function" from a subclass: https://youtrack.jetbrains.com/issue/KT-11488
    protected fun process(parentContext: MidiContext, event: MidiEvent) {
        when {
            size > contexts.size -> (contexts.size until size).forEach { contexts.add(BufferedMidiContextWrapper(pipeContext)) }
            size < contexts.size -> (size until contexts.size).forEach { contexts.removeLast() }
        }

        var currentIn = eventBuffer1
        pipeContext.out = eventBuffer2
        pipeContext.delegate.target = parentContext
        currentIn.clear()
        currentIn.add(event)
        forEachIndexed { i, midiFun ->
            currentIn.forEach {
                midiFun.process(contexts[i], event)
            }
            currentIn.clear()
            val swap = pipeContext.out
            pipeContext.out = currentIn
            currentIn = swap
        }
        currentIn.forEach { event -> pipeContext.delegate.emit(event, 0) }
        currentIn.clear()
    }

    private class PipeContext(
        var out: MutableList<MidiEvent>,
        val delegate: BufferedMidiContextWrapper = BufferedMidiContextWrapper(null)
    ) : MidiContext by delegate {
        override fun emit(event: MidiEvent, defer: Int) {
            if(event !is SysrtEvent) {
                println("Adding $event to $out")
            }
            out.add(event)
        }
    }

    fun addAll(vararg functs: MidiFun) = addAll(functs)
}
