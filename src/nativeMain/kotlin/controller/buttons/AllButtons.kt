package controller.buttons

import controller.Button
import controller.MidiControl
import controller.events.*
import midi.api.ProtocolEvent

class GenericButton(
    index: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "GenericButton"
) : Button(
    index,
    mapPress to { GenericPressed(this as GenericButton, it.defer) },
    mapRelease to { GenericReleased(this as GenericButton, it.defer) },
    name
)

class SelectButton(
    index: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "SelectButton"
) : Button(
    index,
    mapPress to { SelectPressed(this as SelectButton, it.defer) },
    mapRelease to { SelectReleased(this as SelectButton, it.defer) },
    name
)

class ModeButton(
    index: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "ModeButton"
) : Button(
    index,
    mapPress to { ModePressed(this as ModeButton, it.defer) },
    mapRelease to { ModeReleased(this as ModeButton, it.defer) },
    name
)

class PatternUpButton(
    index: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "PatternUpButton"
) : Button(
    index,
    mapPress to { PatternUpPressed(this as PatternUpButton, it.defer) },
    mapRelease to { PatternUpReleased(this as PatternUpButton, it.defer) },
    name
)

class PatternDownButton(
    index: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "PatternDownButton"
) : Button(
    index,
    mapPress to { PatternDownPressed(this as PatternDownButton, it.defer) },
    mapRelease to { PatternDownReleased(this as PatternDownButton, it.defer) },
    name
)

class BrowserButton(
    index: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "BrowserButton"
) : Button(
    index,
    mapPress to { BrowserPressed(this as BrowserButton, it.defer) },
    mapRelease to { BrowserReleased(this as BrowserButton, it.defer) },
    name
)

class GridLeftButton(
    index: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "GridLeftButton"
) : Button(
    index,
    mapPress to { GridLeftPressed(this as GridLeftButton, it.defer) },
    mapRelease to { GridLeftReleased(this as GridLeftButton, it.defer) },
    name
)

class GridRightButton(
    index: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "GridRightButton"
) : Button(
    index,
    mapPress to { GridRightPressed(this as GridRightButton, it.defer) },
    mapRelease to { GridRightReleased(this as GridRightButton, it.defer) },
    name
)

class MuteButton(
    index: Int,
    number: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "MuteButton $number"
) : Button(
    index,
    mapPress to { MutePressed(this as MuteButton, it.defer) },
    mapRelease to { MuteReleased(this as MuteButton, it.defer) },
    name
)

class VolumeButton(
    index: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "VolumeButton"
) : Button(
    index,
    mapPress to { VolumePressed(this as VolumeButton, it.defer) },
    mapRelease to { VolumeReleased(this as VolumeButton, it.defer) },
    name
)

class PanButton(
    index: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "PanButton"
) : Button(
    index,
    mapPress to { PanPressed(this as PanButton, it.defer) },
    mapRelease to { PanReleased(this as PanButton, it.defer) },
    name
)

class FilterButton(
    index: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "FilterButton"
) : Button(
    index,
    mapPress to { FilterPressed(this as FilterButton, it.defer) },
    mapRelease to { FilterReleased(this as FilterButton, it.defer) },
    name
)

class ResonanceButton(
    index: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "ResonanceButton"
) : Button(
    index,
    mapPress to { ResonancePressed(this as ResonanceButton, it.defer) },
    mapRelease to { ResonanceReleased(this as ResonanceButton, it.defer) },
    name
)

class StepButton(
    index: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "StepButton"
) : Button(
    index,
    mapPress to { StepPressed(this as StepButton, it.defer) },
    mapRelease to { StepReleased(this as StepButton, it.defer) },
    name
)

class NoteButton(
    index: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "NoteButton"
) : Button(
    index,
    mapPress to { NotePressed(this as NoteButton, it.defer) },
    mapRelease to { NoteReleased(this as NoteButton, it.defer) },
    name
)

class DrumButton(
    index: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "DrumButton"
) : Button(
    index,
    mapPress to { DrumPressed(this as DrumButton, it.defer) },
    mapRelease to { DrumReleased(this as DrumButton, it.defer) },
    name
)

class PerformButton(
    index: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "PerformButton"
) : Button(
    index,
    mapPress to { PerformPressed(this as PerformButton, it.defer) },
    mapRelease to { PerformReleased(this as PerformButton, it.defer) },
    name
)

class ShiftButton(
    index: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "ShiftButton"
) : Button(
    index,
    mapPress to { ShiftPressed(this as ShiftButton, it.defer) },
    mapRelease to { ShiftReleased(this as ShiftButton, it.defer) },
    name
)

class AltButton(
    index: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "AltButton"
) : Button(
    index,
    mapPress to { AltPressed(this as AltButton, it.defer) },
    mapRelease to { AltReleased(this as AltButton, it.defer) },
    name
)

class PatternButton(
    index: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "PatternButton"
) : Button(
    index,
    mapPress to { PatternPressed(this as PatternButton, it.defer) },
    mapRelease to { PatternReleased(this as PatternButton, it.defer) },
    name
)

class PlayButton(
    index: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "PlayButton"
) : Button(
    index,
    mapPress to { PlayPressed(this as PlayButton, it.defer) },
    mapRelease to { PlayReleased(this as PlayButton, it.defer) },
    name
)

class StopButton(
    index: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "StopButton"
) : Button(
    index,
    mapPress to { StopPressed(this as StopButton, it.defer) },
    mapRelease to { StopReleased(this as StopButton, it.defer) },
    name
)

class RecordButton(
    index: Int,
    mapPress: ProtocolEvent,
    mapRelease: ProtocolEvent,
    name: String = "RecordButton"
) : Button(
    index,
    mapPress to { RecordPressed(this as RecordButton, it.defer) },
    mapRelease to { RecordReleased(this as RecordButton, it.defer) },
    name
)

