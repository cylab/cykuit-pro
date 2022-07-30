package midi.activity

import midi.api.MidiContext
import midi.api.MidiEvent
import midi.api.MidiFun
import midi.core.process

class SwitchableMidiActivity(
    name: String = "unnamed",
    active: Boolean = true
) : MidiActivity(name, active) {

    private var target: MidiActivity? = null

    fun switchTo(midi: MidiContext, subActivity: MidiActivity) = subActivity.also {
        when (subActivity) {
            this -> throw IllegalArgumentException("Can't use parent as sub activity!")
            target -> {}
            else -> {
                println("Switching to ${subActivity.name}")
                target?.deactivate(midi)
                target = subActivity
                subActivity.activate(midi)
            }
        }
    }

    override fun onEvent(midi: MidiContext, event: MidiEvent) {
        target?.process(midi, event)
    }
}

