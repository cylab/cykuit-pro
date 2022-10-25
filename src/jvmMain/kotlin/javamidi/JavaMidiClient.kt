package javamidi

import midi.api.*

val javaMidiClient : MidiClient by lazy { JavaMidiClient() }

internal class JavaMidiClient : MidiClient {
    val CLIENT_NAME = "Java Midi Client"

    internal val javaMidiIns : JavaMidiIns
    internal val javaMidiOuts : JavaMidiOuts
    internal val javaMidiClock : JavaMidiClock
    internal var javaMidiContext : JavaMidiContext

    override val midiIns : MidiIns get() = javaMidiIns
    override val midiOuts : MidiOuts get() = javaMidiOuts
    override val midiClock : MidiClock get() = javaMidiClock

    init {
        javaMidiIns = JavaMidiIns(this)
        javaMidiOuts = JavaMidiOuts(this)
        javaMidiClock = JavaMidiClock(this)
        javaMidiContext = JavaMidiContext(this)
    }

    override fun destroy() {
        javaMidiIns.destroy()
        javaMidiOuts.destroy()
    }

//    private fun process(nframes: jack_nframes_t): Int {
//        frames += nframes.toLong()
//        if (start == 0L) {
//            start = jack_frames_to_time(handle, frames.toUInt()).toLong()
//        }
//        val time = jack_frames_to_time(handle, frames.toUInt()).toLong() - start
//        val clockInterval = 60_000_000 / (midiClock.bpm * 24)
//        val ticks = (time / clockInterval).toInt()
//        val transport = jack_transport_query(handle, positionStruct.ptr)
//
//        if (currentTransport != transport) {
//            currentTransport = transport
//            when (currentTransport) {
//                JackTransportStopped -> println("Stopped")
//                JackTransportRolling -> println("Rolling")
//                JackTransportLooping -> println("Looping")
//                JackTransportStarting -> println("Starting")
//                else -> println("Unknown")
//            }
//        }
//        val emitClock = ticks != lastTicks
//        if (emitClock) {
//            lastTicks = ticks
//            jackMidiOuts.backing.forEach{ it.lastTicks = ticks }
//        }
//
//        jackMidiIns.backing.forEach { it.dispatchEvents(ticks, nframes) }
//
//        if (emitClock) {
//            jackMidiClock.emitClock(ClockTick())
//        }
//
//        jackMidiOuts.backing.forEach{ it.dispatchEvents(ticks, nframes) }
//
//        return 0
//    }

    internal fun emitGlobalEvent(event: MidiEvent?) = javaMidiIns.backing.forEach { it.emitEvent(event) }

    override fun toString() = "JackClient"}
