package sequencer

import controller.MidiController
import controller.buttons.*
import controller.events.*
import midi.activity.MidiActivity
import utils.PropertyListOverlays

class SongControl(val controller: MidiController) : MidiActivity("SongControl") {

    private val PATTERN = controller.buttons.indexOf<PatternButton>()
    private val PLAY = controller.buttons.indexOf<PlayButton>()
    private val STOP = controller.buttons.indexOf<StopButton>()
    private val RECORD = controller.buttons.indexOf<RecordButton>()
    private val mappedButtons = listOf(PATTERN, PLAY, STOP, RECORD)

    private val overlays = PropertyListOverlays(2, controller.buttons, { value }, { value = it })
    private val values = overlays[0]
    private val cursor = overlays[1]

    init {
        onClock.add(overlays)

        onChange.add {
            values[PATTERN] = 2
            values[PLAY] = when {
                midiClock.playing -> 3
                else -> 2
            }
            values[STOP] = 1
            values[RECORD] = when {
                midiClock.playing -> 2
                else -> 2
            }
        }

        onEvent.add {
            if (it is ButtonEvent<*> && it.button.index in (mappedButtons)) {
                when (it) {
                    is ButtonPressed<*> -> cursor[it.button.index] = 4
                    is ButtonReleased<*> -> cursor[it.button.index] = null
                }
                when (it) {
                    is PlayPressed -> midiClock.startPlay()
                    is StopPressed -> midiClock.stopPlay()
                }
                changed = true
            }
        }
    }
}
