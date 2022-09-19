@file:OptIn(ExperimentalUnsignedTypes::class)

package controller.fire

import controller.*
import controller.buttons.*
import midi.activity.MidiActivity
import midi.api.*
import midi.api.MidiNote.*
import midi.core.controlChange

class FireController : MidiController("Akai Fire Pro") {

    override val buttons = Buttons(25) {
        when (it) {
            0 -> SelectButton(it, NoteOn(Cs0), NoteOff(Cs0))
            1 -> ModeButton(it, NoteOn(D0), NoteOff(D0))
            2 -> PatternUpButton(it, NoteOn(G0), NoteOff(G0))
            3 -> PatternDownButton(it, NoteOn(Gs0), NoteOff(Gs0))
            4 -> BrowserButton(it, NoteOn(A0), NoteOff(A0))
            5 -> GridLeftButton(it, NoteOn(As0), NoteOff(As0))
            6 -> GridRightButton(it, NoteOn(B0), NoteOff(B0))
            7 -> MuteButton(it, 0, NoteOn(C1), NoteOff(C1))
            8 -> MuteButton(it, 1, NoteOn(Cs1), NoteOff(Cs1))
            9 -> MuteButton(it, 2, NoteOn(D1), NoteOff(D1))
            10 -> MuteButton(it, 3, NoteOn(Ds1), NoteOff(Ds1))
            11 -> VolumeButton(it, NoteOn(`E-1`), NoteOff(E1))
            12 -> PanButton(it, NoteOn(`F-1`), NoteOff(F1))
            13 -> FilterButton(it, NoteOn(`Fs-1`), NoteOff(Fs1))
            14 -> ResonanceButton(it, NoteOn(`G-1`), NoteOff(G1))
            15 -> StepButton(it, NoteOn(Gs1), NoteOff(Gs1))
            16 -> NoteButton(it, NoteOn(A1), NoteOff(A1))
            17 -> DrumButton(it, NoteOn(As1), NoteOff(As1))
            18 -> PerformButton(it, NoteOn(B1), NoteOff(B1))
            19 -> ShiftButton(it, NoteOn(C2), NoteOff(C2))
            20 -> AltButton(it, NoteOn(Cs2), NoteOff(Cs2))
            21 -> PatternButton(it, NoteOn(D2), NoteOff(D2))
            22 -> PlayButton(it, NoteOn(Ds2), NoteOff(Ds2))
            23 -> StopButton(it, NoteOn(E2), NoteOff(E2))
            24 -> RecordButton(it, NoteOn(F2), NoteOff(F2))
            else -> GenericButton(it, NoteOn(Fs2), NoteOff(Fs2))
        }
    }

    override val pads = Pads(16, 4, NoteOn(Fs2), NoteOff(Fs2))

    override val mapper = object : MidiActivity("Akai Fire Pro Mapper") {

        override fun onEvent(midi: MidiContext, event: MidiEvent) {
            pads.forEach { it.emitMapped(midi, event) }
            buttons.forEach { it.emitMapped(midi,event) }
        }
    }

    override val updater = object : MidiActivity("Akai Fire Pro Updater") {

        override fun onStartup(midi: MidiContext) {
            pads.forEach { it.color = 0xfA_0C_38 }
            with(pads) {
                get(6, 0).color = 0x06_02_00
                get(8, 0).color = 0x06_02_00
                get(5, 1).color = 0x60_20_00
                get(6, 1).color = 0x08_0C_00
                get(7, 1).color = 0x60_20_00
                get(8, 1).color = 0x08_0C_00
                get(9, 1).color = 0x60_20_00
                get(6, 2).color = 0x60_20_00
                get(7, 2).color = 0x60_20_00
                get(8, 2).color = 0x60_20_00
                get(7, 3).color = 0x08_04_00
            }
        }

        override fun onClock(midi: MidiContext, event: MidiEvent) = with(midi) {
            buttons.dirtyIndices
                .forEach { controlChange(buttons[it].mapPress.first.data1, buttons[it].value) }

            pads.dirtyIndices
                .map { (it shl 24) + (pads[it].color and 0xff_ff_ff) }
                .let { firePadColors(it) }
        }
    }
}
