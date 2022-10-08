package controller

import midi.activity.MidiActivity

abstract class MidiController(val name: String = "unnamed"){
    abstract val buttons : Buttons
    abstract val pads : Pads
    abstract val mapper: MidiActivity
    abstract val updater: MidiActivity
}

