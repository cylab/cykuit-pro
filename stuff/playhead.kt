
class PlayheadAnimation(midiOut: MidiOut) : MidiFun by MidiPipe(
    PlayFilter(1.sixteenth),
    {
        val prevPos = (midiClock.position / 1.sixteenth - 1) % 16
        val currentPos = (midiClock.position / 1.sixteenth) % 16
        channel = 0
        noteOn(C0 + currentPos, 128 / 16 * currentPos + 7)
        noteOff(C0 + prevPos)
    },
    midiOut
)

class PlayheadAnimationWithState(midiOut: MidiOut) : MidiFunByPipe() {
    private val someState: Int = 0
    override val pipe = MidiPipe(
        PlayFilter(1.sixteenth),
        {
            val prevPos = (midiClock.position / 1.sixteenth - 1) % 16
            val currentPos = (midiClock.position / 1.sixteenth) % 16
            channel = 0
            noteOn(C0 + currentPos, 128 / 16 * currentPos + 7)
            noteOff(C0 + prevPos)
        },
        midiOut
    )
}

class PlayheadAnimationWithStateNoPipe() : MidiFun {
    private val someState: Int = 0

    override fun MidiContext.process(event: MidiEvent) {
        if (!(event is ClockTick && midiClock.playing && midiClock.position % 1.sixteenth == 0)) return

        val prevPos = (midiClock.position / 1.sixteenth - 1) % 16
        val currentPos = (midiClock.position / 1.sixteenth) % 16
        channel = 0
        noteOn(C0 + currentPos, 128 / 16 * currentPos + 7)
        noteOff(C0 + prevPos)
    }
}


class PlayFilter(interval: Int) :
    MidiFilter({ it is ClockTick && midiClock.playing && midiClock.position % interval == 0 })
