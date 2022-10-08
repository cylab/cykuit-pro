package controller

import controller.buttons.ButtonCommand
import midi.api.*

class Buttons(buttons: List<Button>) : List<Button> by buttons {
    constructor(vararg buttons: Button) : this(buttons.asList())
    constructor(
        amount: Int,
        mapPressBase: ProtocolEvent = NoteOn(MidiNote.C0),
        mapReleaseBase: ProtocolEvent = NoteOn(MidiNote.C0),
        init: (Int) -> Button = { number ->
            Button(
                ButtonCommand.Generic,
                mapPressBase.copy(mapPressBase.data1 + number),
                mapReleaseBase.copy(mapReleaseBase.data1 + number),
                number = number,
                name = "${ButtonCommand.Generic.name} button $number"
            )
        }
    ) : this(List(amount, init))

    operator fun get(command: ButtonCommand) = filter { it.command == command }

    val dirtyButtons
        get() = filter { it.dirty }.onEach { it.dirty = false }
}
