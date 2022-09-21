@file:OptIn(ExperimentalUnsignedTypes::class)

package controller.fire

import controller.*
import controller.buttons.*
import midi.activity.MidiActivity
import midi.api.*
import midi.api.MidiNote.*
import midi.core.controlChange

class FireController : MidiController("Akai Fire Pro") {

    override val buttons = Buttons(
        SelectButton(0, NoteOn(Cs0), NoteOff(Cs0)),
        ModeButton(0, NoteOn(D0), NoteOff(D0)),
        PatternUpButton(0, NoteOn(G0), NoteOff(G0)),
        PatternDownButton(0, NoteOn(Gs0), NoteOff(Gs0)),
        BrowserButton(0, NoteOn(A0), NoteOff(A0)),
        GridLeftButton(0, NoteOn(As0), NoteOff(As0)),
        GridRightButton(0, NoteOn(B0), NoteOff(B0)),
        MuteButton(0, NoteOn(C1), NoteOff(C1)),
        MuteButton(1, NoteOn(Cs1), NoteOff(Cs1)),
        MuteButton(2, NoteOn(D1), NoteOff(D1)),
        MuteButton(3, NoteOn(Ds1), NoteOff(Ds1)),
        VolumeButton(0, NoteOn(`E-1`), NoteOff(E1)),
        PanButton(0, NoteOn(`F-1`), NoteOff(F1)),
        FilterButton(0, NoteOn(`Fs-1`), NoteOff(Fs1)),
        ResonanceButton(0, NoteOn(`G-1`), NoteOff(G1)),
        StepButton(0, NoteOn(Gs1), NoteOff(Gs1)),
        NoteButton(0, NoteOn(A1), NoteOff(A1)),
        DrumButton(0, NoteOn(As1), NoteOff(As1)),
        PerformButton(0, NoteOn(B1), NoteOff(B1)),
        ShiftButton(0, NoteOn(C2), NoteOff(C2)),
        AltButton(0, NoteOn(Cs2), NoteOff(Cs2)),
        PatternButton(0, NoteOn(D2), NoteOff(D2)),
        PlayButton(0, NoteOn(Ds2), NoteOff(Ds2)),
        StopButton(0, NoteOn(E2), NoteOff(E2)),
        RecordButton(0, NoteOn(F2), NoteOff(F2)),
    )

    override val pads = Pads(16, 4, NoteOn(Fs2), NoteOff(Fs2))

    override val mapper = object : MidiActivity("Akai Fire Pro Mapper") {

        override fun onEvent(midi: MidiContext, event: MidiEvent) {
            pads.forEach { it.emitMapped(midi, event) }
            buttons.forEach { it.emitMapped(midi, event) }
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
            buttons.dirtyControls.forEach { controlChange(it.mapPress.first.data1, it.value) }

            pads.dirtyPads
                .map { (it.number shl 24) + (it.color and 0xff_ff_ff) }
                .let { firePadColors(it) }
        }
    }
}
