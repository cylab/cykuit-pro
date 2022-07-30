package midi.activity

import midi.api.*
import midi.core.MidiGroup
import midi.core.process

abstract class MidiActivity(
    var name: String = "unnamed",
    active : Boolean = true
) : MidiFun {
    var initialized = false; private set
    var active = active; private set

    protected val onActivate = MidiGroup()
    protected val onDeactivate = MidiGroup()
    protected val onEvent = MidiGroup()
    protected val onClock = MidiGroup()

    final override fun MidiContext.process(event: MidiEvent) {
        val midi = this
        if(active){
            if(!initialized) {
                activate(midi)
            }
            when(event){
                is SysrtEvent -> {
                    onClock(midi, event)
                    onClock.forEach { it.process(midi, event) }
                }
                else -> {
                    onEvent(midi, event)
                    onEvent.forEach { it.process(midi, event) }
                }
            }
        }
    }

    fun activate(midi: MidiContext) = apply {
        active = true
        if (!initialized) {
            initialized = true
            onStartup(midi)
        }
        onActivate.forEach { it.process(midi, NOOPEvent) }
        onActivate(midi)
    }

    fun deactivate(midi: MidiContext) = apply {
        active = false
        onDeactivate.forEach { it.process(midi, NOOPEvent) }
        onDeactivate(midi)
    }

    protected open fun onStartup(midi: MidiContext) {}
    protected open fun onActivate(midi: MidiContext) {}
    protected open fun onDeactivate(midi: MidiContext) {}
    protected open fun onClock(midi: MidiContext, event: MidiEvent) {}
    protected open fun onEvent(midi: MidiContext, event: MidiEvent) {}

    override fun toString(): String {
        return "${this::class.simpleName}(name='$name', initialized=$initialized, active=$active)"
    }
}
