package javamidi

import midi.api.MidiIn
import midi.api.MidiIns
import javax.sound.midi.MidiSystem

internal class JavaMidiIns private constructor(
    internal val client: JavaMidiClient,
    internal val backing: MutableList<JavaMidiIn>
) : MidiIns, List<MidiIn> by backing {

    constructor(client: JavaMidiClient) : this(client, mutableListOf())

    init {
        println("Available to connect as MidiIn: $available")
    }

    internal fun destroy() {
        backing.forEach { it.destroy() }
    }

    override val available: List<String>
        get() = MidiSystem.getMidiDeviceInfo()
            .map { MidiSystem.getMidiDevice(it) }
            .filter { it.maxTransmitters != 0 }
            .map { it.deviceInfo.name }

    override fun connect(name: String, predicate: (String) -> Boolean): MidiIn {
        get(name)?.let { throw IllegalStateException("MidiIn '$name' already connected!") }

        val srcName = available.firstOrNull(predicate)
            ?: throw NoSuchElementException("Available midi-ins contains no port matching the predicate for '$name'.")

        val inDevice = MidiSystem.getMidiDeviceInfo()
            .map { MidiSystem.getMidiDevice(it) }
            .filter { it.maxTransmitters != 0 }
            .first { it.deviceInfo.name == srcName }

        val midiIn = JavaMidiIn(client, name, inDevice)
        backing.add(midiIn)
        return midiIn
    }
}
