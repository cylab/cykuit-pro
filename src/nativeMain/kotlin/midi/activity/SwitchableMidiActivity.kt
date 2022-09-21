package midi.activity

import midi.api.MidiContext
import midi.api.MidiEvent
import midi.api.MidiFun
import midi.core.process

open class SwitchableMidiActivity(
    name: String = "unnamed",
    active: Boolean = true
) : MidiActivity(name, active) {

    var target: MidiActivity? = null; private set

    init {
        onProcess.add { target?.process(this, it) }
    }

    fun switchTo(midi: MidiContext, subActivity: MidiActivity) = subActivity.also {
        when (subActivity) {
            this -> throw IllegalArgumentException("Can't use parent as sub activity!")
            target -> {}
            else -> {
                println("Switching to ${subActivity}")
                target?.deactivate(midi)
                target = subActivity
                subActivity.activate(midi)
            }
        }
    }
}

