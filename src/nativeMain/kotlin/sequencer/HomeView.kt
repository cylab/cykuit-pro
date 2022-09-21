package sequencer

import controller.MidiController
import controller.Pad
import controller.events.PadPressed
import controller.events.PadReleased
import midi.activity.MidiActivity
import midi.api.*
import midi.api.MidiNote.E1
import midi.core.noteOff
import midi.core.noteOn
import utils.*
import utils.MusicalScale.Companion.MINOR

class HomeView(controller: MidiController) : MidiActivity("HomeView") {
    init {
        onActivate.add {
            with(controller.pads) {
                forEach { it.color = 0xfA_0C_38 }
                get(6, 0).color = 0x06_02_00
                get(8, 0).color = 0x06_02_00
                get(5, 1).color = 0x60_20_00
                get(6, 1).color = 0x08_0C_00
                get(7, 1).color = 0x60_20_00
                get(8, 1).color = 0x08_0C_00
                get(9, 1).color = 0x60_20_00
                get(6, 2).color = 0x60_20_00
                get(7, 2).color = 0x60_20_00
                get(8, 2).color = 0x60_20_00
                get(7, 3).color = 0x08_04_00
            }
        }
    }
}


