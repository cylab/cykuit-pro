package sequencer

import controller.Button
import controller.MidiController
import controller.buttons.*
import controller.events.*
import midi.activity.MidiActivity
import midi.api.MidiContext
import midi.core.half
import midi.core.sixteenth
import utils.*

class SongControl(val controller: MidiController) : MidiActivity("SongControl") {

    private val PATTERN = controller.buttons.instanceOf<PatternButton>()
    private val PLAY = controller.buttons.instanceOf<PlayButton>()
    private val STOP = controller.buttons.instanceOf<StopButton>()
    private val RECORD = controller.buttons.instanceOf<RecordButton>()
    private val mappedButtons = listOf(PATTERN, PLAY, STOP, RECORD)

    private val overlays = PropertyOverlays<Button, Int>(2) { value = it }
    private val values = overlays[0]
    private val cursor = overlays[1]

    init {
        onClock.add(overlays)

        onChange.add {
            values[PATTERN] = 2
            when {
                midiClock.playing -> values[PLAY] = 3
                else -> values[PLAY] = 2
            }
            values[STOP] = 1
            when {
                midiClock.playing -> values[RECORD] = 2
                else -> values[RECORD] = 2
            }
        }

        onEvent.add {
            if (it is ButtonEvent<*> && it.button in (mappedButtons)) {
                when (it) {
                    is ButtonPressed<*> -> cursor[it.button] = blink(1.half) { 1 to 4 }
                    is ButtonReleased<*> -> cursor[it.button] = null
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
