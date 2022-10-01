package controller

import midi.api.*

class Pads(
    val cols: Int,
    val rows: Int,
    mapPressBase: ProtocolEvent = NoteOn(MidiNote.C0),
    mapReleaseBase: ProtocolEvent = NoteOn(MidiNote.C0),
    init: (Int) -> Pad = { index ->
        val col = index % cols
        val row = index / cols
        Pad(
            index, col, row,
            mapPressBase.copy(mapPressBase.data1 + index),
            mapReleaseBase.copy(mapReleaseBase.data1 + index),
            "Pad ($col,$row)"
        )
    }
) : List<Pad> by List(cols * rows, init) {

    fun location(index: Int): Pair<Int, Int> = when (index in 0 until size) {
        true -> index % cols to index / cols
        else -> throw IndexOutOfBoundsException("index: ${index}, size: $size")
    }

    fun get(col: Int, row: Int) = when ((col in 0 until cols) && (row in 0 until rows)) {
        true -> get(col + row * cols)
        else -> throw IndexOutOfBoundsException("col: $col, row: $row, index: ${col + row * cols}, size: $size")
    }

    fun dirtyPadsAfterUpdate(clock: MidiClock) = filter { it.dirtyAfterUpdate(clock) }

    val lastCol = cols - 1
    val lastRow = rows - 1
}
