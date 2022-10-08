package midi.core

import midi.api.MidiContext
import midi.api.MidiEvent
import midi.api.MidiFun

open class MidiGroup private constructor(
    val midiFuns: List<MidiFun>
) : MidiFun, MutableList<MidiFun> by ArrayList(midiFuns) {

    constructor() : this(listOf<MidiFun>())
    constructor(vararg functs: MidiFun) : this(functs.toList())
    constructor(functs: Collection<MidiFun>) : this(functs.toList())

    private val contexts = mutableListOf<BufferedMidiContextWrapper>()

    // workaround to call the "extension-function" from a subclass: https://youtrack.jetbrains.com/issue/KT-11488
    protected fun process(parentContext: MidiContext, event: MidiEvent) {
        // have a dedicated context per group member to individually control context attributes like the channel
        when {
            size > contexts.size -> (contexts.size until size).forEach { contexts.add(BufferedMidiContextWrapper(null)) }
            size < contexts.size -> (size until contexts.size).forEach { contexts.removeLast() }
        }
        forEachIndexed { i, fn ->
            val midi = contexts[i]
            midi.target = parentContext
            with(fn) { midi.process(event) }
        }
    }

    override fun MidiContext.process(event: MidiEvent) = process(this, event)

    open fun add(vararg functs: MidiFun) = addAll(functs)
    inline fun <reified T> addFor(crossinline funct: MidiContext.(T) -> Unit) = add { if (it is T) this.funct(it) }
}
