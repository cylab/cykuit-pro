package javamidi

import midi.api.*
import midi.api.MidiEvent
import midi.core.MidiGroup
import javax.sound.midi.*

internal class JavaMidiIn(
    private val client: JavaMidiClient,
    override val name: String,
    private val inDevice: MidiDevice
) : MidiIn, MidiGroup(), Receiver {

    init {
        inDevice.transmitter.receiver = this
        if(!inDevice.isOpen) {
            inDevice.open()
        }
    }

    override fun add(vararg functs: MidiFun) = addAll(functs)

    override fun close() = destroy()

    internal fun destroy() {
        inDevice.transmitter.close()
        inDevice.close()
    }

    override fun send(message: MidiMessage?, timeStamp: Long) {
        emitEvent(message.toMidiEvent())
    }

    internal fun emitEvent(event: MidiEvent?) {
        if (event != null) {
            client.javaMidiContext.processInContext(event)
        }
    }


    private fun MidiMessage?.toMidiEvent(): MidiEvent? {
        val data0 = data(0)
        val type = data0 and 0b1111_0000
        val channel = data0 and 0b0000_1111
        // TODO: detect sysex!
        return when (EventType.fromHead(type)) {
            EventType.NOTE_ON -> NoteOn(
                note = MidiNote.noteByNumber[data(1)],
                velocity = data(2),
                channel = channel
            )
            EventType.NOTE_OFF -> NoteOff(
                note = MidiNote.noteByNumber[data(1)],
                velocity = data(1),
                channel = channel
            )
            EventType.PITCH_BEND -> PitchBend(
                bend = (data(2) and 0b0111_1111) shl 7 + (data(1) and 0b0111_1111),
                channel = channel
            )
            EventType.AFTERTOUCH -> Aftertouch(
                pressure = data(1),
                channel = channel
            )
            EventType.POLY_AFTERTOUCH -> PolyAftertouch(
                note = MidiNote.noteByNumber[data(1)],
                pressure = data(2),
                channel = channel
            )
            EventType.CONTROL_CHANGE -> ControlChange(
                control = data(1),
                value = data(2),
                channel = channel
            )
            EventType.PROGRAM_CHANGE -> ProgramChange(
                program = data(1),
                channel = channel
            )
            EventType.SYSRT_START -> StartPlay().also { client.javaMidiClock.emitClock(it) }
            EventType.SYSRT_CONTINUE -> ContinuePlay().also { client.javaMidiClock.emitClock(it) }
            EventType.SYSRT_STOP -> StopPlay().also { client.javaMidiClock.emitClock(it) }
            EventType.SYSRT_RESET -> ResetPlay().also { client.javaMidiClock.emitClock(it) }
            EventType.SYSRT_SENSING -> Sensing()
            EventType.SYSRT_CLOCK -> {
                // TODO: learn bpm from ticks for usage in clock and playhead
                null
            }
            else -> null
        }
    }

    private fun MidiMessage?.data(i: Int) = this?.message?.get(i)?.toInt() ?: 0
}
