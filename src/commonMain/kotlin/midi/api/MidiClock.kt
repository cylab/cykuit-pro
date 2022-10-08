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

object StoppedClock : MidiClock {
    override var bpm: Int = 0
    override val playing: Boolean = false
    override val ticks: Int = 0
    override val position: Int = 0

    override fun startPlay() {}
    override fun stopPlay() {}
    override fun continuePlay() {}
    override fun resetPlay() {}
}
