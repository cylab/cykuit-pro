@file:OptIn(ExperimentalUnsignedTypes::class)

package jack

import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.set
import midi.api.EventType.AFTERTOUCH
import midi.api.EventType.CONTROL_CHANGE
import midi.api.EventType.NOTE_OFF
import midi.api.EventType.NOTE_ON
import midi.api.EventType.PITCH_BEND
import midi.api.EventType.POLY_AFTERTOUCH
import midi.api.EventType.PROGRAM_CHANGE
import midi.api.EventType.SYSEX
import midi.api.EventType.SYSRT_CLOCK
import midi.api.EventType.SYSRT_CONTINUE
import midi.api.EventType.SYSRT_RESET
import midi.api.EventType.SYSRT_SENSING
import midi.api.EventType.SYSRT_START
import midi.api.EventType.SYSRT_STOP
import midi.api.MidiContext
import midi.api.MidiEvent
import midi.api.MidiOut
import midi.api.ProtocolEvent
import midi.api.SysEx

internal class JackMidiOut(
    override val name: String,
    private val port: CPointer<jack_port_t>
) : MidiOut {
    var lastTicks = 0
    private val outEvents = mutableListOf<Pair<Int, MidiEvent>>()

    override fun MidiContext.process(event: MidiEvent) {
        outEvents.add(lastTicks to event)
    }

    internal fun dispatchEvents(ticks: Int, nframes: jack_nframes_t) {
        val outputBuffer = jack_port_get_buffer(port, nframes)
        jack_midi_clear_buffer(outputBuffer);

        val it = outEvents.iterator()
        while (it.hasNext()) {
            val (schedule, event) = it.next()
            if (ticks >= schedule) {
                it.remove()
                when (event) {
                    is ProtocolEvent -> dispatchEvent(outputBuffer, event)
                }
            }
        }
    }

    private fun dispatchEvent(outputBuffer: COpaquePointer?, event: ProtocolEvent): Unit = when (event.type) {
        NOTE_ON, NOTE_OFF, PITCH_BEND, POLY_AFTERTOUCH, CONTROL_CHANGE ->
            sendEvent(outputBuffer, event.data0, event.data1, event.data2)
        PROGRAM_CHANGE, AFTERTOUCH ->
            sendEvent(outputBuffer, event.data0, event.data1)
        SYSRT_CLOCK, SYSRT_START, SYSRT_CONTINUE, SYSRT_STOP, SYSRT_SENSING, SYSRT_RESET ->
            sendEvent(outputBuffer, event.data0)
        SYSEX ->
            sendSysex(outputBuffer, event as SysEx)
        else -> {}
    }

    private fun sendEvent(outputBuffer: COpaquePointer?, vararg data: Int) {
        jack_midi_event_reserve(outputBuffer, 0, data.size.toULong())!!
            .let { buffer -> data.indices.forEach { buffer[it] = data[it].toUByte() } }
    }

    private fun sendSysex(outputBuffer: COpaquePointer?, event: SysEx) = when {
        // 3 byte manufacturer ID
        event.id > 0x7F -> {
            val data_size = (5 + event.sysexData.size).toULong()
            jack_midi_event_reserve(outputBuffer, 0, data_size)!!.let {
                it[0] = event.data0.toUByte()
                it[1] = ((event.id shr 16) and 0x7F).toUByte()
                it[2] = ((event.id shr 8) and 0x7F).toUByte()
                it[3] = (event.id and 0x7F).toUByte()
                event.sysexData.forEachIndexed { i, byte -> it[i + 4] = byte }
                it[event.sysexData.size + 4] = 0xF7.toUByte()
            }
        }
        // 1 byte manufacturer ID
        else -> {
            val data_size = (3 + event.sysexData.size).toULong()
            jack_midi_event_reserve(outputBuffer, 0, data_size)!!.let {
                it[0] = event.data0.toUByte()
                it[1] = (event.id and 0x7F).toUByte()
                event.sysexData.forEachIndexed { i, byte -> it[i + 2] = byte }
                it[event.sysexData.size + 2] = 0xF7.toUByte()
            }
        }
    }

    internal fun destroy() {
    }
}
