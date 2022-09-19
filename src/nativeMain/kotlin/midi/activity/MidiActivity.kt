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
    var changed = true; protected set

    protected val onActivate = MidiGroup()
    protected val onDeactivate = MidiGroup()
    protected val onEvent = MidiGroup()
    protected val onClock = MidiGroup()
    protected val onChange = MidiGroup()

    final override fun MidiContext.process(event: MidiEvent) {
        val midi = this
        if(active){
            if(!initialized) {
                activate(midi)
            }
            if(changed) {
                onChange.forEach { it.process(midi, NOOPEvent) }
                onChange(midi)
                changed = false
            }
            when(event){
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
        changed = true
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
