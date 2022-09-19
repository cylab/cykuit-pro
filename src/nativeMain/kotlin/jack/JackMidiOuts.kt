package jack

import midi.api.MidiOut
import midi.api.MidiOuts
import utils.toKStrings

internal class JackMidiOuts private constructor(
    internal val jack: JackClient,
    internal val backing: MutableList<JackMidiOut>
) : MidiOuts, List<MidiOut> by backing {

    constructor(client: JackClient) : this(client, mutableListOf())

    init {
        println("Available to connect as MidiOut: $available")
    }

    internal fun destroy() {
        backing.forEach { it.destroy() }
    }

    override val available: List<String>
        get() = requireNotNull(jack_get_ports(jack.handle, null, JACK_DEFAULT_MIDI_TYPE, JackPortIsInput.toULong()))
            .toKStrings()

    override fun connect(name: String, predicate: (String) -> Boolean): MidiOut {
        get(name)?.let { throw IllegalStateException("MidiOut '$name' already connected!") }

        val srcName = "${jack.CLIENT_NAME}:$name"
        val destName = available.filterNot { it.startsWith("${jack.CLIENT_NAME}:") }.firstOrNull(predicate)
            ?: throw NoSuchElementException("Available midi-outs contains no port matching the predicate for $name.")

        val outPort = jack_port_register(jack.handle, name, JACK_DEFAULT_MIDI_TYPE, JackPortIsOutput.toULong(), 0)
        requireNotNull(outPort)

        jack_connect(jack.handle, srcName, destName)

        val midiOut = JackMidiOut(name, outPort)
        backing.add(midiOut)
        return midiOut
    }
}
