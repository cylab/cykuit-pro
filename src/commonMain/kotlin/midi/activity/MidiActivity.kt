package midi.activity

import midi.api.*
import midi.core.*

abstract class MidiActivity(
    var name: String = "unnamed",
    initialActive: Boolean = true
) : MidiFun {
    var initialized = false; private set
    var active = initialActive; private set
    var changed = true // ; protected set // TODO make this a function?

    private var shouldActivate = false
    private var shouldDeactivate = false

    protected val onProcess = MidiGroup()
    protected val onEvent = MidiGroup()
    protected val onClock = MidiGroup()

    val onStartup = MidiGroup()
    val onActivate = MidiGroup()
    val onDeactivate = MidiGroup()
    val onChange = MidiGroup()
    val onEmit = MidiGroup()

    private val eventPipe = MidiPipe(
        { event ->
            val midi = this
            if (shouldDeactivate) {
//                println("$name shouldDeactivate=$shouldDeactivate -> deactivate(midi)")
                deactivate(midi)
            }
            onProcess.forEach { it.process(midi, event) }
            if (active || shouldActivate) {
                if (!initialized || shouldActivate) {
//                    println("$name shouldActivate=$shouldActivate -> activate(midi)")
                    activate(midi)
                }
                if (changed) {
                    onChange(midi)
                    onChange.forEach { it.process(midi, NOOPEvent) }
                    changed = false
                }
                when (event) {
                    is SysrtEvent -> {
                        onClock(midi, event)
                        onClock.forEach { it.process(midi, event) }
                        emit(event) // propagate clock events, always!
                    }
                    else -> {
                        onEvent(midi, event)
                        onEvent.forEach { it.process(midi, event) }
                    }
                }
            }
        },
        MidiPeek { event -> if (active) onEmit.forEach { it.process(this, event) } }
    )

    final override fun MidiContext.processInContext(event: MidiEvent) = eventPipe.process(this, event)

    // TODO: remove this, if the eventPipe above proves to work like intended (peeking for emitted events)
    fun MidiContext._process(event: MidiEvent) {
        val midi = this
        onProcess.forEach { it.process(midi, event) }
        if (shouldDeactivate) {
//            println("$name shouldDeactivate=$shouldDeactivate -> deactivate(midi)")
            deactivate(midi)
        }
        if (active || shouldActivate) {
            if (!initialized || shouldActivate) {
                println("$name shouldActivate=$shouldActivate -> activate(midi)")
                activate(midi)
            }
            if (changed) {
                onChange(midi)
                onChange.forEach { it.process(midi, NOOPEvent) }
                changed = false
            }
            when (event) {
                is SysrtEvent -> {
                    onClock(midi, event)
                    onClock.forEach { it.process(midi, event) }
                    emit(event) // propagate clock events, always!
                }
                else -> {
                    onEvent(midi, event)
                    onEvent.forEach { it.process(midi, event) }
                }
            }
        }
    }

    // TODO remove the requirement of having an MidiContext in the activate and deactivate methods completely???
    fun activate() = apply {
        shouldActivate = true
        println("$name activate() -> setting shouldActivate=$shouldActivate")
    }

    fun deactivate() = apply {
        shouldActivate = false
        shouldDeactivate = true
        println("$name deactivate() -> setting shouldActivate=$shouldActivate, shouldDeactivate=$shouldDeactivate")
    }

    fun activate(midi: MidiContext) = apply {
        active = true
        shouldActivate = false
        if (!initialized) {
            initialized = true
            onStartup(midi)
            onStartup.forEach { it.process(midi, NOOPEvent) }
        }
        onActivate(midi)
        onActivate.forEach { it.process(midi, NOOPEvent) }
        changed = true
    }

    fun deactivate(midi: MidiContext) = apply {
        active = false
        shouldDeactivate = false
        onDeactivate.forEach { it.process(midi, NOOPEvent) }
        onDeactivate(midi)
    }

    protected open fun onStartup(midi: MidiContext) {}
    protected open fun onActivate(midi: MidiContext) {}
    protected open fun onDeactivate(midi: MidiContext) {}
    protected open fun onClock(midi: MidiContext, event: MidiEvent) {}
    protected open fun onEvent(midi: MidiContext, event: MidiEvent) {}
    protected open fun onChange(midi: MidiContext) {}

    override fun toString(): String {
        return "${this::class.simpleName}(name='$name', initialized=$initialized, active=$active)"
    }
}
