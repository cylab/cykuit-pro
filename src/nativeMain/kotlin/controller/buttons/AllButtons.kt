package controller.buttons

import controller.Button
import controller.events.*
import midi.api.ProtocolEvent

enum class AllButtons {
    Generic,
    Select,
    Mode,
    PatternUp,
    PatternDown,
    Browser,
    GridLeft,
    GridRight,
    Mute,
    Volume,
    Pan,
    Filter,
    Resonance,
    Step,
    Note,
    Drum,
    Perform,
    Shift,
    Alt,
    Pattern,
    Play,
    Stop,
    Record,
}

class GenericButton(
    number: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "GenericButton"
) : Button(
    number,
    mapPress to { GenericPressed(this as GenericButton, it.defer) },
    mapRelease to { GenericReleased(this as GenericButton, it.defer) },
    name
)

class SelectButton(
    number: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "SelectButton"
) : Button(
    number,
    mapPress to { SelectPressed(this as SelectButton, it.defer) },
    mapRelease to { SelectReleased(this as SelectButton, it.defer) },
    name
)

class ModeButton(
    number: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "ModeButton"
) : Button(
    number,
    mapPress to { ModePressed(this as ModeButton, it.defer) },
    mapRelease to { ModeReleased(this as ModeButton, it.defer) },
    name
)

class PatternUpButton(
    number: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "PatternUpButton"
) : Button(
    number,
    mapPress to { PatternUpPressed(this as PatternUpButton, it.defer) },
    mapRelease to { PatternUpReleased(this as PatternUpButton, it.defer) },
    name
)

class PatternDownButton(
    number: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "PatternDownButton"
) : Button(
    number,
    mapPress to { PatternDownPressed(this as PatternDownButton, it.defer) },
    mapRelease to { PatternDownReleased(this as PatternDownButton, it.defer) },
    name
)

class BrowserButton(
    number: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "BrowserButton"
) : Button(
    number,
    mapPress to { BrowserPressed(this as BrowserButton, it.defer) },
    mapRelease to { BrowserReleased(this as BrowserButton, it.defer) },
    name
)

class GridLeftButton(
    number: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "GridLeftButton"
) : Button(
    number,
    mapPress to { GridLeftPressed(this as GridLeftButton, it.defer) },
    mapRelease to { GridLeftReleased(this as GridLeftButton, it.defer) },
    name
)

class GridRightButton(
    number: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "GridRightButton"
) : Button(
    number,
    mapPress to { GridRightPressed(this as GridRightButton, it.defer) },
    mapRelease to { GridRightReleased(this as GridRightButton, it.defer) },
    name
)

class MuteButton(
    number: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "MuteButton"
) : Button(
    number,
    mapPress to { MutePressed(this as MuteButton, it.defer) },
    mapRelease to { MuteReleased(this as MuteButton, it.defer) },
    name
)

class VolumeButton(
    number: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "VolumeButton"
) : Button(
    number,
    mapPress to { VolumePressed(this as VolumeButton, it.defer) },
    mapRelease to { VolumeReleased(this as VolumeButton, it.defer) },
    name
)

class PanButton(
    number: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "PanButton"
) : Button(
    number,
    mapPress to { PanPressed(this as PanButton, it.defer) },
    mapRelease to { PanReleased(this as PanButton, it.defer) },
    name
)

class FilterButton(
    number: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "FilterButton"
) : Button(
    number,
    mapPress to { FilterPressed(this as FilterButton, it.defer) },
    mapRelease to { FilterReleased(this as FilterButton, it.defer) },
    name
)

class ResonanceButton(
    number: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "ResonanceButton"
) : Button(
    number,
    mapPress to { ResonancePressed(this as ResonanceButton, it.defer) },
    mapRelease to { ResonanceReleased(this as ResonanceButton, it.defer) },
    name
)

class StepButton(
    number: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "StepButton"
) : Button(
    number,
    mapPress to { StepPressed(this as StepButton, it.defer) },
    mapRelease to { StepReleased(this as StepButton, it.defer) },
    name
)

class NoteButton(
    number: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "NoteButton"
) : Button(
    number,
    mapPress to { NotePressed(this as NoteButton, it.defer) },
    mapRelease to { NoteReleased(this as NoteButton, it.defer) },
    name
)

class DrumButton(
    number: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "DrumButton"
) : Button(
    number,
    mapPress to { DrumPressed(this as DrumButton, it.defer) },
    mapRelease to { DrumReleased(this as DrumButton, it.defer) },
    name
)

class PerformButton(
    number: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "PerformButton"
) : Button(
    number,
    mapPress to { PerformPressed(this as PerformButton, it.defer) },
    mapRelease to { PerformReleased(this as PerformButton, it.defer) },
    name
)

class ShiftButton(
    number: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "ShiftButton"
) : Button(
    number,
    mapPress to { ShiftPressed(this as ShiftButton, it.defer) },
    mapRelease to { ShiftReleased(this as ShiftButton, it.defer) },
    name
)

class AltButton(
    number: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "AltButton"
) : Button(
    number,
    mapPress to { AltPressed(this as AltButton, it.defer) },
    mapRelease to { AltReleased(this as AltButton, it.defer) },
    name
)

class PatternButton(
    number: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "PatternButton"
) : Button(
    number,
    mapPress to { PatternPressed(this as PatternButton, it.defer) },
    mapRelease to { PatternReleased(this as PatternButton, it.defer) },
    name
)

class PlayButton(
    number: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "PlayButton"
) : Button(
    number,
    mapPress to { PlayPressed(this as PlayButton, it.defer) },
    mapRelease to { PlayReleased(this as PlayButton, it.defer) },
    name
)

class StopButton(
    number: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "StopButton"
) : Button(
    number,
    mapPress to { StopPressed(this as StopButton, it.defer) },
    mapRelease to { StopReleased(this as StopButton, it.defer) },
    name
)

class RecordButton(
    number: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "RecordButton"
) : Button(
    number,
    mapPress to { RecordPressed(this as RecordButton, it.defer) },
    mapRelease to { RecordReleased(this as RecordButton, it.defer) },
    name
)

