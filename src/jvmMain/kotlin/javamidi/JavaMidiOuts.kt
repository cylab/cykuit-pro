package javamidi

import midi.api.MidiOut
import midi.api.MidiOuts
import javax.sound.midi.MidiSystem

internal class JavaMidiOuts private constructor(
    internal val client: JavaMidiClient,
    internal val backing: MutableList<JavaMidiOut>
) : MidiOuts, List<MidiOut> by backing {

    constructor(client: JavaMidiClient) : this(client, mutableListOf())

    init {
        println("Available to connect as MidiOut: $available")
    }

    internal fun destroy() {
        backing.forEach { it.destroy() }
    }

    override val available: List<String>
        get() = MidiSystem.getMidiDeviceInfo()
            .map { MidiSystem.getMidiDevice(it) }
            .filter { it.maxReceivers != 0 }
            .map { it.deviceInfo.name }

    override fun connect(name: String, predicate: (String) -> Boolean): MidiOut {
        get(name)?.let { throw IllegalStateException("MidiOut '$name' already connected!") }

        val destName = available.firstOrNull(predicate)
            ?: throw NoSuchElementException("Available midi-outs contains no port matching the predicate for '$name'.")

        val outDevice = MidiSystem.getMidiDeviceInfo()
            .map { MidiSystem.getMidiDevice(it) }
            .filter { it.maxReceivers != 0 }
            .first { it.deviceInfo.name == destName }

        val midiOut = JavaMidiOut(name, outDevice)
        backing.add(midiOut)
        return midiOut
    }
}
