package sequencer

import controller.*
import controller.buttons.ButtonCommand.Note
import midi.api.StartPlay
import midi.api.StopPlay

class PatternView(config: Config, controller: MidiController, sequencer: TrackSequencer) : MappedActivities(
    "PatternView",
    controller.pads.map { pad ->
        val trackNumber = pad.col / 2
        val clipNumber = pad.row * 2 + pad.col % 2
        val clip = sequencer.grid[trackNumber][clipNumber]
        val noteButton = controller.buttons[Note][0]
        val colors = config.trackColors[trackNumber]
        Mapping(pad, noteButton) to ClipEditor(controller, clip, colors)
    },
    exclusive = true
) {
    private val base = Layer(this, "PatternView Base")

    init {
        onEvent.add { if(it is StartPlay || it is StopPlay) changed = true }
        onChange.add {
            println("Updating ${this@PatternView}")
            controller.pads.forEach { pad ->
                val trackNumber = pad.col / 2
                val clipNumber = pad.row * 2 + pad.col % 2
                val clip = sequencer.grid[trackNumber][clipNumber]
                pad.color[base] = when {
                    clip.isEmpty() -> config.trackColors[trackNumber].empty
                    clip.active && midiClock.playing -> config.trackColors[trackNumber].active
                    else -> config.trackColors[trackNumber].clip
                }
            }
        }
    }
}


