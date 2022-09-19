package midi.api

interface MidiIn : MutableList<MidiFun> {
    val name: String
    fun add(vararg next: MidiFun) = addAll(next)
}
