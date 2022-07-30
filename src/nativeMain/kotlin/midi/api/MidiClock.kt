package midi.api

interface MidiClock {
    var bpm: Int
    val playing : Boolean
    val ticks : Int
    val position : Int

    fun startPlay()
    fun stopPlay()
    fun continuePlay()
    fun resetPlay()
}
