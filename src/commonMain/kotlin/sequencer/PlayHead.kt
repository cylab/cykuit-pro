package sequencer

import controller.Layer
import controller.MidiController
import midi.activity.MidiActivity

class PlayHead(
    private val controller: MidiController
) : MidiActivity("PlayHead") {
    var position: Int = 0
        set(value) {
            if (field != value) {
                lastPosition = field
                field = value
            }
        }

    private var lastPosition = 0
    private val indicator = Layer(this, "Position indicator")

    init {
        onClock.add {
            if (position != lastPosition) {
                controller.pads.getOrNull(lastPosition)?.let { it.color.remove(indicator) }
                controller.pads.getOrNull(position)?.let { it.color[indicator] = 0xffffff }
            }
        }
        onActivate.add {
            controller.pads.getOrNull(lastPosition)?.let { it.color.remove(indicator) }
            controller.pads.getOrNull(position)?.let { it.color[indicator] = 0xffffff }
        }
        onDeactivate.add {
            controller.pads.getOrNull(lastPosition)?.let { it.color.remove(indicator) }
            controller.pads.getOrNull(position)?.let { it.color.remove(indicator) }
        }
    }
}
