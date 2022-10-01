package sequencer

import controller.Layer
import controller.MidiController

class PatternView(config: Config, controller: MidiController, sequencer: TrackSequencer) : MappedActivities(
    "PatternView",
    controller.pads.map { pad ->
        val trackNumber = pad.col / 2
        val clipNumber = pad.row * 2 + pad.col % 2
        val clip = sequencer.grid[trackNumber][clipNumber]
        pad to ClipEditor(controller, clip, colors = config.trackColors[trackNumber])
    },
    exclusive = true
) {
    private val base = Layer(this, "PatternView Base")

    init {
        onChange.add {
            println("Updating ${this@PatternView}")
            controller.pads.forEach { pad ->
                val trackNumber = pad.col / 2
                val clipNumber = pad.row * 2 + pad.col % 2
                val clip = sequencer.grid[trackNumber][clipNumber]
                pad.color[base] = when {
                    clip.isEmpty() -> config.trackColors[trackNumber].empty
                    else -> config.trackColors[trackNumber].clip
                }
            }
        }
    }
}


