package jack

import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.alloc
import kotlinx.cinterop.asStableRef
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import kotlinx.cinterop.staticCFunction
import midi.api.ClockTick
import midi.api.MidiClient
import midi.api.MidiClock
import midi.api.MidiEvent
import midi.api.MidiIns
import midi.api.MidiOuts

val jackMidiClient : MidiClient by lazy { JackClient() }

internal class JackClient : MidiClient {
    val CLIENT_NAME = "cykuit-pro"
    val handle: CPointer<jack_client_t>

    internal val jackMidiIns : JackMidiIns
    internal val jackMidiOuts : JackMidiOuts
    internal val jackMidiClock : JackMidiClock
    internal var jackContext : JackContext

    override val midiIns : MidiIns get() = jackMidiIns
    override val midiOuts : MidiOuts get() = jackMidiOuts
    override val midiClock : MidiClock get() = jackMidiClock

    private val positionStruct = nativeHeap.alloc<jack_position_t>()

    private var currentTransport: jack_transport_state_t = 256u
    private var frames = 0L
    private var start = 0L
    private var lastTicks = 0

    init {
        handle = requireNotNull(jack_client_open(CLIENT_NAME, JackNullOption, null)) { "Jack server not running?" }

        jackMidiIns = JackMidiIns(this)
        jackMidiOuts = JackMidiOuts(this)
        jackMidiClock = JackMidiClock(this)
        jackContext = JackContext(this)

        val callback = staticCFunction(
            fun(nframes: jack_nframes_t, ptr: COpaquePointer?): Int {
                return ptr!!.asStableRef<JackClient>().get().process(nframes)
            }
        )
        jack_set_process_callback(handle, callback, StableRef.create(this).asCPointer())
        jack_transport_start(handle)

        if (jack_activate(handle) != 0) {
            throw IllegalStateException("Cannot activate client")
        }
    }

    override fun destroy() {
        nativeHeap.free(positionStruct.rawPtr)
        jackMidiIns.destroy()
        jackMidiOuts.destroy()
    }

    private fun process(nframes: jack_nframes_t): Int {
        frames += nframes.toLong()
        if (start == 0L) {
            start = jack_frames_to_time(handle, frames.toUInt()).toLong()
        }
        val time = jack_frames_to_time(handle, frames.toUInt()).toLong() - start
        val clockInterval = 60_000_000 / (midiClock.bpm * 24)
        val ticks = (time / clockInterval).toInt()
        val transport = jack_transport_query(handle, positionStruct.ptr)

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
            jackMidiOuts.backing.forEach{ it.lastTicks = ticks }
        }

        jackMidiIns.backing.forEach { it.dispatchEvents(ticks, nframes) }

        if (emitClock) {
            jackMidiClock.emitClock(ClockTick())
        }

        jackMidiOuts.backing.forEach{ it.dispatchEvents(ticks, nframes) }

        return 0
    }

    internal fun emitGlobalEvent(event: MidiEvent?) = jackMidiIns.backing.forEach { it.emitEvent(event) }

    override fun toString() = "JackClient"
}

