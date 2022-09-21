package controller.events

import controller.Button
import controller.buttons.*
import midi.api.NOOPEvent.channel
import midi.api.NOOPEvent.type
import midi.core.justify

abstract class ButtonEvent<T: Button> : ControlEvent {
    abstract val button: T

    override fun toString() = justify(
        this::class.simpleName to -15,
        "button:" to 10, button.name to -22,
        "number:" to 10, button.number to 5,
        "defer:" to 10, defer to 5
    )
}

abstract class ButtonPressed<T: Button> : ButtonEvent<T>()

abstract class ButtonReleased<T: Button> : ButtonEvent<T>()

class GenericPressed(
    override val button: GenericButton,
    override val defer: Int = 0
) : ButtonPressed<GenericButton>()

class GenericReleased(
    override val button: GenericButton,
    override val defer: Int = 0
) : ButtonReleased<GenericButton>()


class SelectPressed(
    override val button: SelectButton,
    override val defer: Int = 0
) : ButtonPressed<SelectButton>()

class SelectReleased(
    override val button: SelectButton,
    override val defer: Int = 0
) : ButtonReleased<SelectButton>()

class ModePressed(
    override val button: ModeButton,
    override val defer: Int = 0
) : ButtonPressed<ModeButton>()

class ModeReleased(
    override val button: ModeButton,
    override val defer: Int = 0
) : ButtonReleased<ModeButton>()

class PatternUpPressed(
    override val button: PatternUpButton,
    override val defer: Int = 0
) : ButtonPressed<PatternUpButton>()

class PatternUpReleased(
    override val button: PatternUpButton,
    override val defer: Int = 0
) : ButtonReleased<PatternUpButton>()

class PatternDownPressed(
    override val button: PatternDownButton,
    override val defer: Int = 0
) : ButtonPressed<PatternDownButton>()

class PatternDownReleased(
    override val button: PatternDownButton,
    override val defer: Int = 0
) : ButtonReleased<PatternDownButton>()

class BrowserPressed(
    override val button: BrowserButton,
    override val defer: Int = 0
) : ButtonPressed<BrowserButton>()

class BrowserReleased(
    override val button: BrowserButton,
    override val defer: Int = 0
) : ButtonReleased<BrowserButton>()

class GridLeftPressed(
    override val button: GridLeftButton,
    override val defer: Int = 0
) : ButtonPressed<GridLeftButton>()

class GridLeftReleased(
    override val button: GridLeftButton,
    override val defer: Int = 0
) : ButtonReleased<GridLeftButton>()

class GridRightPressed(
    override val button: GridRightButton,
    override val defer: Int = 0
) : ButtonPressed<GridRightButton>()

class GridRightReleased(
    override val button: GridRightButton,
    override val defer: Int = 0
) : ButtonReleased<GridRightButton>()

class MutePressed(
    override val button: MuteButton,
    override val defer: Int = 0
) : ButtonPressed<MuteButton>()

class MuteReleased(
    override val button: MuteButton,
    override val defer: Int = 0
) : ButtonReleased<MuteButton>()

class VolumePressed(
    override val button: VolumeButton,
    override val defer: Int = 0
) : ButtonPressed<VolumeButton>()

class VolumeReleased(
    override val button: VolumeButton,
    override val defer: Int = 0
) : ButtonReleased<VolumeButton>()

class PanPressed(
    override val button: PanButton,
    override val defer: Int = 0
) : ButtonPressed<PanButton>()

class PanReleased(
    override val button: PanButton,
    override val defer: Int = 0
) : ButtonReleased<PanButton>()

class FilterPressed(
    override val button: FilterButton,
    override val defer: Int = 0
) : ButtonPressed<FilterButton>()

class FilterReleased(
    override val button: FilterButton,
    override val defer: Int = 0
) : ButtonReleased<FilterButton>()

class ResonancePressed(
    override val button: ResonanceButton,
    override val defer: Int = 0
) : ButtonPressed<ResonanceButton>()

class ResonanceReleased(
    override val button: ResonanceButton,
    override val defer: Int = 0
) : ButtonReleased<ResonanceButton>()

class StepPressed(
    override val button: StepButton,
    override val defer: Int = 0
) : ButtonPressed<StepButton>()

class StepReleased(
    override val button: StepButton,
    override val defer: Int = 0
) : ButtonReleased<StepButton>()

class NotePressed(
    override val button: NoteButton,
    override val defer: Int = 0
) : ButtonPressed<NoteButton>()

class NoteReleased(
    override val button: NoteButton,
    override val defer: Int = 0
) : ButtonReleased<NoteButton>()

class DrumPressed(
    override val button: DrumButton,
    override val defer: Int = 0
) : ButtonPressed<DrumButton>()

class DrumReleased(
    override val button: DrumButton,
    override val defer: Int = 0
) : ButtonReleased<DrumButton>()

class PerformPressed(
    override val button: PerformButton,
    override val defer: Int = 0
) : ButtonPressed<PerformButton>()

class PerformReleased(
    override val button: PerformButton,
    override val defer: Int = 0
) : ButtonReleased<PerformButton>()

class ShiftPressed(
    override val button: ShiftButton,
    override val defer: Int = 0
) : ButtonPressed<ShiftButton>()

class ShiftReleased(
    override val button: ShiftButton,
    override val defer: Int = 0
) : ButtonReleased<ShiftButton>()

class AltPressed(
    override val button: AltButton,
    override val defer: Int = 0
) : ButtonPressed<AltButton>()

class AltReleased(
    override val button: AltButton,
    override val defer: Int = 0
) : ButtonReleased<AltButton>()

class PatternPressed(
    override val button: PatternButton,
    override val defer: Int = 0
) : ButtonPressed<PatternButton>()

class PatternReleased(
    override val button: PatternButton,
    override val defer: Int = 0
) : ButtonReleased<PatternButton>()

class PlayPressed(
    override val button: PlayButton,
    override val defer: Int = 0
) : ButtonPressed<PlayButton>()

class PlayReleased(
    override val button: PlayButton,
    override val defer: Int = 0
) : ButtonReleased<PlayButton>()

class StopPressed(
    override val button: StopButton,
    override val defer: Int = 0
) : ButtonPressed<StopButton>()

class StopReleased(
    override val button: StopButton,
    override val defer: Int = 0
) : ButtonReleased<StopButton>()

class RecordPressed(
    override val button: RecordButton,
    override val defer: Int = 0
) : ButtonPressed<RecordButton>()

class RecordReleased(
    override val button: RecordButton,
    override val defer: Int = 0
) : ButtonReleased<RecordButton>()
