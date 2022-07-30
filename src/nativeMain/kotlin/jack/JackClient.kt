package jack

import kotlinx.cinterop.*
import midi.*
import midi.api.EventType.*
import midi.api.MidiNote.Companion.noteByNumber
import midi.api.*
import midi.core.MidiFunImpl
import midi.core.MidiGroup
import utils.toKStrings

class JackClient : MidiClient {
    private val jackMidiIn = JackMidiIn()
    private val jackMidiOut = JackMidiOut()
    private val jackMidiClock = JackMidiClock()

    override val midiIn : MidiIn get() = jackMidiIn
    override val midiOut : MidiOut get() = jackMidiOut
    override val midiClock : MidiClock get() = jackMidiClock

    private var jackContext = JackContext()
    private val client: CPointer<jack_client_t>
    private val inputPort: CPointer<jack_port_t>
    private val outputPort: CPointer<jack_port_t>
    private val position = nativeHeap.alloc<jack_position_t>()
    private val event = nativeHeap.alloc<jack_midi_event_t>()

    init {
        client = requireNotNull(jack_client_open("cykuit-pro", JackNullOption, null)) { "Jack server not running?" }
        inputPort = requireNotNull(
            jack_port_register(client, "midi_in", JACK_DEFAULT_MIDI_TYPE, JackPortIsInput.toULong(), 0)
        )
        outputPort = requireNotNull(
            jack_port_register(client, "midi_out", JACK_DEFAULT_MIDI_TYPE, JackPortIsOutput.toULong(), 0)
        )

        val midiInPorts =
            requireNotNull(jack_get_ports(client, null, JACK_DEFAULT_MIDI_TYPE, JackPortIsInput.toULong()))
                .toKStrings()
        val midiOutPorts =
            requireNotNull(jack_get_ports(client, null, JACK_DEFAULT_MIDI_TYPE, JackPortIsOutput.toULong()))
                .toKStrings()

        println("Midi-In Ports: $midiInPorts")
        println("Midi-Out Ports: $midiOutPorts")

        val cyIn = midiInPorts.first { it.contains("cykuit-pro") }
        val cyOut = midiOutPorts.first { it.contains("cykuit-pro") }

        val apc40In = midiInPorts.first { it.contains("APC40") }
        val apc40Out = midiOutPorts.first { it.contains("APC40") }

        println("connecting $apc40Out -> $cyIn")
        jack_connect(client, apc40Out, cyIn)
        println("connecting $cyOut -> $apc40In")
        jack_connect(client, cyOut, apc40In)

        val callback = staticCFunction(
            fun(nframes: jack_nframes_t, ptr: COpaquePointer?): Int {
                return ptr!!.asStableRef<JackClient>().get().process(nframes)
            }
        )
        jack_set_process_callback(client, callback, StableRef.create(this).asCPointer())
        jack_transport_start(client)

        if (jack_activate(client) != 0) {
            throw IllegalStateException("Cannot activate client")
        }
    }

    var currentTransport: jack_transport_state_t = 256u
    var frames = 0L
    var start = 0L
    var lastTicks = 0
    private fun process(nframes: jack_nframes_t): Int {
        frames += nframes.toLong()
        if (start == 0L) {
            start = jack_frames_to_time(client, frames.toUInt()).toLong()
        }
        val time = jack_frames_to_time(client, frames.toUInt()).toLong() - start
        val clockInterval = 60_000_000 / (midiClock.bpm * 24)
        val ticks = (time / clockInterval).toInt()
        val transport = jack_transport_query(client, position.ptr)
        val inputBuffer = jack_port_get_buffer(inputPort, nframes)

        if (currentTransport != transport) {
            currentTransport = transport
            when (currentTransport) {
                JackTransportStopped -> println("Stopped")
                JackTransportRolling -> println("Rolling")
                JackTransportLooping -> println("Looping")
                JackTransportStarting -> println("Starting")
                else -> println("Unknown")
            }
        }
        val emitClock = ticks != lastTicks
        if (emitClock) {
            lastTicks = ticks
        }

        for (i in 0 until jack_midi_get_event_count(inputBuffer).toInt()) {
            jack_midi_event_get(event.ptr, inputBuffer, i.toUInt())
            emitEvent(event.toMidiEvent())
        }

        if (emitClock) {
            emitClock(ClockTick())
        }

        jackMidiOut.submit(ticks, nframes)

        return 0
    }

    private fun jack_midi_event_t?.toMidiEvent(): MidiEvent? {
        val data0 = data(0)
        val type = data0 and 0b1111_0000
        val channel = data0 and 0b0000_1111
        // TODO: detect sysex!
        return when (type) {
            NOTE_ON.code -> NoteOn(
                channel,
                note = noteByNumber[data(1)],
                velocity = data(2)
            )
            NOTE_OFF.code -> NoteOff(
                channel,
                note = noteByNumber[data(1)],
                velocity = data(1)
            )
            PITCH_BEND.code -> PitchBend(
                channel,
                bend = (data(2) and 0b0111_1111) shl 7 + (data(1) and 0b0111_1111)
            )
            AFTERTOUCH.code -> Aftertouch(
                channel,
                pressure = data(1)
            )
            POLY_AFTERTOUCH.code -> PolyAftertouch(
                channel,
                note = noteByNumber[data(1)],
                pressure = data(2)
            )
            CONTROL_CHANGE.code -> ControlChange(
                channel,
                control = data(1),
                value = data(2)
            )
            PROGRAM_CHANGE.code -> ProgramChange(
                channel,
                program = data(1)
            )
            SYSRT_START.code -> StartPlay().also { emitClock(it) }
            SYSRT_CONTINUE.code -> ContinuePlay().also { emitClock(it) }
            SYSRT_STOP.code -> StopPlay().also { emitClock(it) }
            SYSRT_RESET.code -> ResetPlay().also { emitClock(it) }
            SYSRT_SENSING.code -> Sensing()
            SYSRT_CLOCK.code -> {
                // TODO: learn bpm from ticks for usage in clock and playhead
                null
            }
            else -> null
        }
    }

    private fun jack_midi_event_t?.data(i: Int) = this?.buffer?.get(i)?.toInt() ?: 0


    private fun emitClock(event: MidiEvent?) {
        if(event == null)
            return

        when(event.type) {
            SYSRT_START -> {
                jackMidiClock.position = 0
                jackMidiClock.playing = true
                emitEvent(event)
            }
            SYSRT_STOP -> {
                jackMidiClock.playing = false
                emitEvent(event)
            }
            SYSRT_CONTINUE -> {
                jackMidiClock.playing = true
                emitEvent(event)
            }
            SYSRT_RESET -> {
                jackMidiClock.position = 0
                jackMidiClock.playing = false
                emitEvent(event)
            }
            SYSRT_CLOCK -> {
                jackMidiClock.ticks++
                if(jackMidiClock.playing) {
                    jackMidiClock.position++
                }
                emitEvent(event)
            }
            else -> {}
        }
    }

    private fun emitEvent(event: MidiEvent?) {
        if (event != null) {
            with(jackMidiIn) { jackContext.process(event) }
        }
    }

    override fun destroy() {
        nativeHeap.free(position.rawPtr)
        nativeHeap.free(event.rawPtr)
    }


    private inner class JackMidiIn : MidiIn, MidiGroup()

    private inner class JackMidiOut : MidiOut {
        private val outEvents = mutableListOf<Pair<Int, MidiEvent>>()

        override fun MidiContext.process(event: MidiEvent) {
            outEvents.add(lastTicks + event.defer to event)
        }

        fun submit(ticks: Int, nframes: jack_nframes_t) {
            val outputBuffer = jack_port_get_buffer(outputPort, nframes)
            jack_midi_clear_buffer(outputBuffer);

            outEvents.iterator().let {
                while (it.hasNext()) {
                    val (schedule, event) = it.next()
                    if (ticks >= schedule) {
                        it.remove()
                        when (event.type) {
                            NOTE_ON, NOTE_OFF, PITCH_BEND, POLY_AFTERTOUCH, CONTROL_CHANGE ->
                                jack_midi_event_reserve(outputBuffer, 0, 3)!!.let {
                                    it[0] = event.data0.toUByte()
                                    it[1] = event.data1.toUByte()
                                    it[2] = event.data2.toUByte()
                                }
                            PROGRAM_CHANGE, AFTERTOUCH ->
                                jack_midi_event_reserve(outputBuffer, 0, 2)!!.let {
                                    it[0] = event.data0.toUByte()
                                    it[1] = event.data1.toUByte()
                                }
                            SYSRT_CLOCK, SYSRT_START, SYSRT_CONTINUE, SYSRT_STOP, SYSRT_SENSING, SYSRT_RESET ->
                                jack_midi_event_reserve(outputBuffer, 0, 1)!!.let {
                                    it[0] = event.data0.toUByte()
                                }
                            else -> {
                            }
                        }
                    }
                }
            }
        }
    }

    private inner class JackMidiClock : MidiClock, MidiGroup() {
        override var bpm: Int = 120
        override var playing : Boolean = false
        override var ticks : Int = 0
        override var position : Int = 0

        override fun startPlay() = emitClock(StartPlay())
        override fun stopPlay() = emitClock(StopPlay())
        override fun continuePlay() = emitClock(ContinuePlay())
        override fun resetPlay() = emitClock(ResetPlay())
    }

    private inner class JackContext : MidiContext {
        override val midiClock = this@JackClient.midiClock
        override var channel = 0

        override fun emit(event: MidiEvent) {
            /** needs no implementation **/
        }

        override fun toString(): String {
            return "JackContext"
        }
    }

    override fun toString(): String {
        return "JackClient"
    }
}
