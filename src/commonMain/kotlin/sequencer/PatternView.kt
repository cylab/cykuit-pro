package sequencer

import controller.*
import controller.buttons.ButtonCommand.Note
import midi.activity.MidiActivity
import midi.api.*

class PatternView(config: Config, controller: MidiController, sequencer: TrackSequencer) : MappedActivities(
    "PatternView",
    activities = controller.pads
        .flatMap { pad ->
            val trackNumber = pad.col / 2
            val clipNumber = pad.row * 2 + pad.col % 2
            val track = sequencer.grid[trackNumber]
            val clip = track[clipNumber]
            val noteButton = controller.buttons[Note][0]
            val colors = config.trackColors[trackNumber]
            listOf(
                Mapping(pad, noteButton) to ClipEditor(controller, clip, colors),
                Mapping(pad) to MidiFun { track.schedule(clip) }
            )
        }
        .toMap(),
    exclusive = true
) {
    private val base = Layer(this, "PatternView Base")

    init {
        sequencer.onChange.add { changed = true }
        activities.values
            .filterIsInstance<MidiActivity>()
            .forEach { activity -> activity.onActivate.add { changed = true } }
        onClock.add { if (it is StartPlay || it is StopPlay) changed = true }
        onChange.add {
            controller.pads.forEach { pad ->
                val trackNumber = pad.col / 2
                val clipNumber = pad.row * 2 + pad.col % 2
                val track = sequencer.grid[trackNumber]
                val clip = track[clipNumber]
                pad.color[base] = when {
                    clip.isEmpty() -> config.trackColors[trackNumber].empty
                    clip.active && midiClock.playing -> config.trackColors[trackNumber].active
                    clip.active -> config.trackColors[trackNumber].pending
                    track.scheduled == clip -> config.trackColors[trackNumber].pending
                    else -> config.trackColors[trackNumber].clip
                }
            }
        }
    }
}


