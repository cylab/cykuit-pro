package sequencer

import controller.Layer.Companion.BACKGROUND
import controller.MidiController
import midi.activity.MidiActivity

class HomeView(controller: MidiController) : MidiActivity("HomeView") {
    init {
        onActivate.add {
            with(controller.pads) {
                forEach { it.color[BACKGROUND] = 0x00_0C_38 }
                get(6, 0).color[BACKGROUND] = 0x06_02_00
                get(8, 0).color[BACKGROUND] = 0x06_02_00
                get(5, 1).color[BACKGROUND] = 0x60_20_00
                get(6, 1).color[BACKGROUND] = 0x08_0C_00
                get(7, 1).color[BACKGROUND] = 0x60_20_00
                get(8, 1).color[BACKGROUND] = 0x08_0C_00
                get(9, 1).color[BACKGROUND] = 0x60_20_00
                get(6, 2).color[BACKGROUND] = 0x60_20_00
                get(7, 2).color[BACKGROUND] = 0x60_20_00
                get(8, 2).color[BACKGROUND] = 0x60_20_00
                get(7, 3).color[BACKGROUND] = 0x08_04_00
            }
        }
    }
}


