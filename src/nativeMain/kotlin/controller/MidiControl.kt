package controller
import controller.MidiControlType.*

enum class MidiControlType {
    PUSH_BUTTON, TOGGLE_BUTTON, FADER, KNOB, ROTARY
}

sealed class MidiControl(
    val controlId: UByte,
    val name: String = "unnamed",
    val type: MidiControlType = PUSH_BUTTON
)

class PushButton(controlId: UByte, name: String = "unnamed") : MidiControl(controlId, name, PUSH_BUTTON)
class ToggleButton(controlId: UByte, name: String = "unnamed") : MidiControl(controlId, name, TOGGLE_BUTTON)
class Fader(controlId: UByte, name: String = "unnamed") : MidiControl(controlId, name, FADER)
class Knob(controlId: UByte, name: String = "unnamed") : MidiControl(controlId, name, KNOB)
class Rotary(controlId: UByte, name: String = "unnamed") : MidiControl(controlId, name, ROTARY)
