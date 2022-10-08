package midi.core

import platform.posix.abs

val Int.quarter: Int get() = this * 24
val Int.beat: Int get() = this.quarter
val Int.whole: Int get() = this.quarter * 4
val Int.half: Int get() = this.quarter * 2
val Int.eighth: Int get() = this.quarter / 2
val Int.sixteenth: Int get() = this.eighth / 2
val Int.thirtysecond: Int get() = this.sixteenth / 2
val Int.half_triplet: Int get() = this.whole / 3
val Int.quarter_triplet: Int get() = this.half / 3
val Int.eighth_triplet: Int get() = this.quarter / 3
val Int.sixteenth_triplet: Int get() = this.eighth / 3
val Int.thirtysecond_triplet: Int get() = this.sixteenth / 3

fun justify(vararg components: Pair<Any?, Int>) = components
    .map { (component, size) ->
        val text = component.toString()
        when{
            size > text.length -> (text.length until size).joinToString("") { " " } + text
            size < 0 && abs(size) > text.length -> text + (text.length until abs(size)).joinToString("") { " " }
            else -> text
        }
    }
    .joinToString(" ")
