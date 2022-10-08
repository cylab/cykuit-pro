package midi.activity

import midi.api.MidiContext
import midi.core.MidiGroup
import midi.core.process

open class SwitchableMidiActivity(
    name: String = "unnamed",
    active: Boolean = true
) : MidiActivity(name, active) {

    protected var target: MidiActivity? = null; private set

    init {
        onProcess.add { target?.process(this, it) }
    }

    fun activate(midi: MidiContext, subActivity: MidiActivity) = subActivity.also {
        when (subActivity) {
            this -> throw IllegalArgumentException("Can't use parent as sub activity!")
            target -> subActivity.activate(midi)
            else -> {
                target?.deactivate(midi)
                target = subActivity
                subActivity.activate(midi)
                println("Switched to ${subActivity}")
            }
        }
    }
}

