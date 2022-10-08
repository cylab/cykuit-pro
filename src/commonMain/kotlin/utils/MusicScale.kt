package utils

open class MusicalScale(val name: String, private vararg val intervals: Int) {

    val notesPerOctave = intervals.size

    /** returns the distance of the given step from the root note of a scale in semitones **/
    operator fun get(step: Int) = when {
        step < 0 -> intervals[(notesPerOctave - step) % notesPerOctave] - 12 + 12 * (step / notesPerOctave)
        else -> intervals[step % notesPerOctave] + 12 * (step / notesPerOctave)
    }

    companion object {
        val ACOUSTIC = MusicalScale("Acoustic", 0, 2, 4, 6, 7, 9, 10)
        val ALTERED = MusicalScale("Altered scale", 0, 1, 3, 4, 6, 8, 10)
        val NATURAL_MINOR = MusicalScale("Natural Minor", 0, 2, 3, 5, 7, 8, 10)
        val MAJOR = MusicalScale("Major", 0, 2, 4, 5, 7, 9, 11)
        val MINOR = MusicalScale("Minor", 0, 2, 3, 5, 7, 8, 10)
    }
}
