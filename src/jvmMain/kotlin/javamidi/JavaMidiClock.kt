package javamidi

import midi.api.ContinuePlay
import midi.api.EventType
import midi.api.MidiClock
import midi.api.ProtocolEvent
import midi.api.ResetPlay
import midi.api.StartPlay
import midi.api.StopPlay
import midi.core.MidiGroup

internal class JavaMidiClock(private val client: JavaMidiClient) : MidiClock, MidiGroup() {
    override var bpm: Int = 120
    override var playing : Boolean = false
    override var ticks : Int = 0
    override var position : Int = 0

    override fun startPlay() = emitClock(StartPlay())
    override fun stopPlay() = emitClock(StopPlay())
    override fun continuePlay() = emitClock(ContinuePlay())
    override fun resetPlay() = emitClock(ResetPlay())

    internal fun emitClock(event: ProtocolEvent?) {
        if(event == null)
            return

        when(event.type) {
            EventType.SYSRT_START -> {
                position = 0
                playing = true
                client.emitGlobalEvent(event)
            }
            EventType.SYSRT_STOP -> {
                playing = false
                client.emitGlobalEvent(event)
            }
            EventType.SYSRT_CONTINUE -> {
                playing = true
                client.emitGlobalEvent(event)
            }
            EventType.SYSRT_RESET -> {
                position = 0
                playing = false
                client.emitGlobalEvent(event)
            }
            EventType.SYSRT_CLOCK -> {
                ticks++
                if(playing) {
                    position++
                }
                client.emitGlobalEvent(event)
            }
            else -> {}
        }
    }

}
