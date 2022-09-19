package jack

import midi.api.MidiIn
import midi.api.MidiIns
import utils.toKStrings

internal class JackMidiIns private constructor(
    internal val jack: JackClient,
    internal val backing: MutableList<JackMidiIn>
) : MidiIns, List<MidiIn> by backing {

    constructor(client: JackClient) : this(client, mutableListOf())

    init {
        println("Available to connect as MidiIn: $available")
    }

    internal fun destroy() {
        backing.forEach { it.destroy() }
    }

    override val available: List<String>
        get() = requireNotNull(jack_get_ports(jack.handle, null, JACK_DEFAULT_MIDI_TYPE, JackPortIsOutput.toULong()))
            .toKStrings()

    override fun connect(name: String, predicate: (String) -> Boolean): MidiIn {
        get(name)?.let { throw IllegalStateException("MidiIn '$name' already connected!") }

        val srcName = available.filterNot { it.startsWith("${jack.CLIENT_NAME}:") }.firstOrNull(predicate)
            ?: throw NoSuchElementException("Available midi-ins contains no port matching the predicate for '$name'.")
        val destName = "${jack.CLIENT_NAME}:$name"

        val inPort = jack_port_register(jack.handle, name, JACK_DEFAULT_MIDI_TYPE, JackPortIsInput.toULong(), 0)
        requireNotNull(inPort)

        jack_connect(jack.handle, srcName, destName)

        val midiIn = JackMidiIn(jack, name, inPort)
        backing.add(midiIn)
        return midiIn
    }
}
