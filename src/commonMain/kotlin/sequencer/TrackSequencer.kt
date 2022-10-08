package sequencer

import midi.activity.*
import midi.api.MidiContext
import midi.api.MidiEvent
import midi.api.MidiNote
import midi.core.note
import midi.core.sixteenth

class TrackSequencer : MidiActivity("TrackSequencer") {
    val grid = Grid(this)

    init {
        onChange.add {
            onProcess.clear()
            onProcess.addAll(grid)
        }
    }
}

class Grid private constructor(
    private val sequencer: TrackSequencer,
    private val tracks: MutableList<Track>
) : List<Track> by tracks {
    constructor(sequencer: TrackSequencer) : this(sequencer, mutableListOf<Track>())

    override operator fun get(index: Int): Track {
        if (index >= tracks.size) {
            (tracks.size..index).forEach { tracks.add(Track()) }
            sequencer.changed = true
        }
        return tracks[index]
    }

    operator fun set(index: Int, track: Track) {
        if (index >= tracks.size) {
            (tracks.size..index).forEach { tracks.add(Track()) }
            sequencer.changed = true
        }
        tracks[index] = track
    }
}

class Track private constructor(
    private val clips: MutableList<Clip>
) : ExclusiveActivities(activities = clips), List<Clip> by clips {
    constructor() : this(mutableListOf<Clip>())

    override operator fun get(index: Int): Clip {
        if (index >= clips.size) {
            (clips.size..index).forEach { clips.add(Clip(index)) }
            changed = true
        }
        return clips[index]
    }

    operator fun set(index: Int, clip: Clip) {
        if (index >= clips.size) {
            (clips.size..index).forEach { clips.add(Clip(index)) }
            changed = true
        }
        clips[index] = clip
    }
}

class Clip(
    val number: Int,
    val steps: MutableList<Step> = MutableList(16) { Step() },
    var position: Int = 0,
    var stepLength: Int = 1.sixteenth
) : MidiActivity(name="Clip $number") {
    init {
        onClock.add {
            if (!midiClock.playing || midiClock.position % stepLength != 0 || position >= steps.size) {
                return@add
            }
            position = midiClock.position / stepLength % steps.size
            for (note in steps[position].notes) {
                if (note != null) {
                    note(note.value, note.length, note.velocity)
                }
            }
//            position++
//            if(position >= steps.size) {
//                position = 0
//            }
        }
    }

    fun isEmpty() = steps.all { it.isEmpty() }
}


class Step() {
    val notes: Array<Note?> = arrayOfNulls(6)
    val effects: Array<Int?> = arrayOfNulls(8)

    fun isEmpty() = notes.all { it == null } && effects.all { it == null }
}

class Note(
    val value: MidiNote,
    val velocity: Int,
    val length: Int
) {
    override fun equals(other: Any?) = this === other || (other is Note && other.value == value)
    override fun hashCode() = value.hashCode()
}
