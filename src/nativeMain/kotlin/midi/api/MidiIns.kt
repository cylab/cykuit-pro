package midi.api

interface MidiIns : List<MidiIn> {
    val available: List<String>
    fun connect(name: String, predicate: (String) -> Boolean = { it.contains(name) }) : MidiIn
    operator fun get(name: String) = firstOrNull() { it.name == name }
}
