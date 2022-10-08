package sequencer

import controller.Button
import controller.MidiController
import controller.buttons.ButtonCommand.*
import controller.events.*
import midi.activity.MidiActivity
import utils.PropertyUpdater

class SongControl(val controller: MidiController) : MidiActivity("SongControl") {

    private val PLAY = controller.buttons[Play][0]
    private val STOP = controller.buttons[Stop][0]
    private val RECORD = controller.buttons[Record][0]
    private val mappedButtons = listOf(PLAY, STOP, RECORD)

    private val updater = PropertyUpdater<Button, Int>(2) { state = it }
    private val values = updater[0]
    private val overlay = updater[1]

    init {
        onClock.add(updater)

        onChange.add {
            values[PLAY] = when {
                midiClock.playing -> PLAY.ACTIVE
                else -> PLAY.INACTIVE
            }
            values[STOP] = STOP.INACTIVE
            values[RECORD] = when {
                midiClock.playing -> RECORD.STANDBY
                else -> RECORD.INACTIVE
            }
        }

        onEvent.add { event ->
            if (event is ButtonEvent && event.button in (mappedButtons)) {
                val button = event.button
                when (event) {
                    is ButtonPressed -> overlay[button] = button.HIGHLIGHT
                    is ButtonReleased -> overlay[button] = null
                }
                if(event is ButtonPressed) {
                    when {
                        button == PLAY -> midiClock.startPlay()
                        button == STOP -> midiClock.stopPlay()
                    }
                }
                changed = true
            }
        }
    }
}
