package sequencer

import midi.core.half
import midi.core.quarter
import utils.*

class TrackColors(
    val off: Value<Int?>,
    val empty: Value<Int?>,
    val clip: Value<Int?>,
    val root: Value<Int?>,
    val note: Value<Int?>,
    val step: Value<Int?>,
    val active: Value<Int?>,
    val pending: Value<Int?>,
)

class Config {
    val trackColors = listOf(
        0x0A0028, 0x5000F0, 0x850028, 0x5000F0, 0x957acc,
        0x000830, 0x0040F0, 0x800830, 0x0040F0, 0x7a90cc,
        0x041810, 0x38F0C0, 0x8f1810, 0x38F0C0, 0x7accb7,
        0x002004, 0x00E028, 0x802004, 0x00E028, 0x7acc89,
        0x181800, 0xE0E000, 0xa01800, 0xE0E000, 0xcccc7a,
        0x401300, 0xF04800, 0xe01300, 0xF04800, 0xcc937a,
        0x401511, 0xF05040, 0xe01511, 0xF05040, 0xcc827a,
        0x200010, 0xF00078, 0xb00010, 0xF00078, 0xcc7aa3,
    ).chunked(5).map { (empty, clip, root, note, step) ->
        TrackColors(
            off = { 0 },
            empty = { empty },
            clip = { clip },
            root = { root },
            note = { note },
            step = { step },
            active = fade(1.half, clip, empty),
            pending = blink(1.half, clip, 0x8080ff),
        )
    }
//    val trackColors = listOf(
//        0x0A0028, 0x5000F0, 0x150040, 0x5000F0, 0x957acc,
//        0x000830, 0x0040F0, 0x001140, 0x0040F0, 0x7a90cc,
//        0x041810, 0x38F0C0, 0x0f4033, 0x38F0C0, 0x7accb7,
//        0x002004, 0x00E028, 0x00400b, 0x00E028, 0x7acc89,
//        0x181800, 0xE0E000, 0x404000, 0xE0E000, 0xcccc7a,
//        0x401300, 0xF04800, 0x401300, 0xF04800, 0xcc937a,
//        0x401511, 0xF05040, 0x401511, 0xF05040, 0xcc827a,
//        0x200010, 0xF00078, 0x400020, 0xF00078, 0xcc7aa3,
//    ).chunked(5).map { (empty, clip, root, note, step) -> TrackColors(empty, clip, root, note, step) }
}
