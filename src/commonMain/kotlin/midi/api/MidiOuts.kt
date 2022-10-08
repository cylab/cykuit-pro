package midi.api

interface MidiOuts : List<MidiOut> {
    val available: List<String>
    fun connect(name: String, predicate: (String) -> Boolean = { it.contains(name) }) : MidiOut
    operator fun get(name: String) = firstOrNull() { it.name == name }
}
