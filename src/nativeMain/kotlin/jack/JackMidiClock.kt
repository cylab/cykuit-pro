package jack

import midi.api.ContinuePlay
import midi.api.EventType
import midi.api.MidiClock
import midi.api.MidiEvent
import midi.api.ResetPlay
import midi.api.StartPlay
import midi.api.StopPlay
import midi.core.MidiGroup

internal class JackMidiClock(private val jack: JackClient) : MidiClock, MidiGroup() {
    override var bpm: Int = 120
    override var playing : Boolean = false
    override var ticks : Int = 0
    override var position : Int = 0

    override fun startPlay() = emitClock(StartPlay())
    override fun stopPlay() = emitClock(StopPlay())
    override fun continuePlay() = emitClock(ContinuePlay())
    override fun resetPlay() = emitClock(ResetPlay())

    internal fun emitClock(event: MidiEvent?) {
        if(event == null)
            return

        when(event.type) {
            EventType.SYSRT_START -> {
                position = 0
                playing = true
                jack.emitGlobalEvent(event)
            }
            EventType.SYSRT_STOP -> {
                playing = false
                jack.emitGlobalEvent(event)
            }
            EventType.SYSRT_CONTINUE -> {
                playing = true
                jack.emitGlobalEvent(event)
            }
            EventType.SYSRT_RESET -> {
                position = 0
                playing = false
                jack.emitGlobalEvent(event)
            }
            EventType.SYSRT_CLOCK -> {
                ticks++
                if(playing) {
                    position++
                }
                jack.emitGlobalEvent(event)
            }
            else -> {}
        }
    }

}
