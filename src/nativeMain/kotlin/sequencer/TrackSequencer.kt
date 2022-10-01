package sequencer

import midi.activity.MidiActivity
import midi.api.MidiContext
import midi.api.MidiEvent
import midi.api.MidiNote
import midi.core.note
import midi.core.sixteenth

class TrackSequencer : MidiActivity() {
    val grid = Grid()
}

class Grid {
    private val tracks = mutableListOf<Track>()

    operator fun get(index: Int): Track {
        if (index >= tracks.size) {
            (tracks.size..index).forEach { tracks.add(Track()) }
        }
        return tracks[index]
    }

    operator fun set(index: Int, track: Track) {
        if (index >= tracks.size) {
            (tracks.size..index).forEach { tracks.add(Track()) }
        }
        tracks[index] = track
    }
}

class Track : MidiActivity() {
    private val clips = mutableListOf<Clip>()

    operator fun get(index: Int): Clip {
        if (index >= clips.size) {
            (clips.size..index).forEach { clips.add(Clip()) }
        }
        return clips[index]
    }

    operator fun set(index: Int, clip: Clip) {
        if (index >= clips.size) {
            (clips.size..index).forEach { clips.add(Clip()) }
        }
        clips[index] = clip
    }
}

class Clip(
    val steps: MutableList<Step> = MutableList(16) { Step() },
    var position: Int = 0,
    var stepLength: Int = 1.sixteenth
) : MidiActivity() {
    override fun onEvent(midi: MidiContext, event: MidiEvent) {
        if (midi.midiClock.position % stepLength != 0 || position >= steps.size) {
            return
        }
        for (note in steps[position].notes) {
            if (note != null) {
                midi.note(note.value, note.length, note.velocity)
            }
        }
        position++
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
