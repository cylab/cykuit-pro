package jack

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.alloc
import kotlinx.cinterop.get
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import midi.api.Aftertouch
import midi.api.ContinuePlay
import midi.api.ControlChange
import midi.api.EventType
import midi.api.MidiEvent
import midi.api.MidiFun
import midi.api.MidiIn
import midi.api.MidiNote
import midi.api.NoteOff
import midi.api.NoteOn
import midi.api.PitchBend
import midi.api.PolyAftertouch
import midi.api.ProgramChange
import midi.api.ResetPlay
import midi.api.Sensing
import midi.api.StartPlay
import midi.api.StopPlay
import midi.core.MidiGroup

internal class JackMidiIn(
    private val jack: JackClient,
    override val name: String,
    private val port: CPointer<jack_port_t>
) : MidiIn, MidiGroup() {
    private val eventStruct = nativeHeap.alloc<jack_midi_event_t>()

    override fun add(vararg functs: MidiFun) = addAll(functs)

    internal fun destroy() {
        nativeHeap.free(eventStruct.rawPtr)
    }

    internal fun dispatchEvents(ticks: Int, nframes: jack_nframes_t) {
        val inputBuffer = jack_port_get_buffer(port, nframes)
        for (i in 0 until jack_midi_get_event_count(inputBuffer).toInt()) {
            jack_midi_event_get(eventStruct.ptr, inputBuffer, i.toUInt())
            val event = eventStruct.toMidiEvent()
            emitEvent(event)
        }
    }

    internal fun emitEvent(event: MidiEvent?) {
        if (event != null) {
            jack.jackContext.process(event)
        }
    }

    private fun jack_midi_event_t?.toMidiEvent(): MidiEvent? {
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
            EventType.SYSRT_START -> StartPlay().also { jack.jackMidiClock.emitClock(it) }
            EventType.SYSRT_CONTINUE -> ContinuePlay().also { jack.jackMidiClock.emitClock(it) }
            EventType.SYSRT_STOP -> StopPlay().also { jack.jackMidiClock.emitClock(it) }
            EventType.SYSRT_RESET -> ResetPlay().also { jack.jackMidiClock.emitClock(it) }
            EventType.SYSRT_SENSING -> Sensing()
            EventType.SYSRT_CLOCK -> {
                // TODO: learn bpm from ticks for usage in clock and playhead
                null
            }
            else -> null
        }
    }

    private fun jack_midi_event_t?.data(i: Int) = this?.buffer?.get(i)?.toInt() ?: 0
}
