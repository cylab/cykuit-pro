@file:OptIn(ExperimentalUnsignedTypes::class)

package controller.fire

import controller.*
import controller.Layer.Companion.BACKGROUND
import controller.buttons.ButtonCommand.*
import midi.activity.MidiActivity
import midi.api.*
import midi.api.MidiNote.*
import midi.core.controlChange
import midi.core.half
import utils.blink

class FireController : MidiController("Akai Fire Pro") {

    override val buttons = Buttons(
        Button(Generic, NoteOn(`Cs-2`), NoteOff(`Cs-2`), inactive = { 1 }, active = { 2 }),
        Button(Select, NoteOn(Cs0), NoteOff(Cs0), inactive = { 1 }, active = { 2 }),
        Button(Mode, NoteOn(D0), NoteOff(D0), inactive = { 1 }, active = { 2 }),
        Button(PatternUp, NoteOn(G0), NoteOff(G0), inactive = { 1 }, active = { 2 }),
        Button(PatternDown, NoteOn(Gs0), NoteOff(Gs0), inactive = { 1 }, active = { 2 }),
        Button(Browser, NoteOn(A0), NoteOff(A0), inactive = { 1 }, active = { 2 }),
        Button(GridLeft, NoteOn(As0), NoteOff(As0), inactive = { 1 }, active = { 2 }),
        Button(GridRight, NoteOn(B0), NoteOff(B0), inactive = { 1 }, active = { 2 }),
        Button(Mute, NoteOn(C1), NoteOff(C1), number = 0, inactive = { 1 }, active = { 2 }),
        Button(Mute, NoteOn(Cs1), NoteOff(Cs1), number = 1, inactive = { 1 }, active = { 2 }),
        Button(Mute, NoteOn(D1), NoteOff(D1), number = 2, inactive = { 1 }, active = { 2 }),
        Button(Mute, NoteOn(Ds1), NoteOff(Ds1), number = 3, inactive = { 1 }, active = { 2 }),
        Button(Volume, NoteOn(`E-1`), NoteOff(E1), inactive = { 1 }, active = { 2 }),
        Button(Pan, NoteOn(`F-1`), NoteOff(F1), inactive = { 1 }, active = { 2 }),
        Button(Filter, NoteOn(`Fs-1`), NoteOff(Fs1), inactive = { 1 }, active = { 2 }),
        Button(Resonance, NoteOn(`G-1`), NoteOff(G1), inactive = { 1 }, active = { 2 }),
        Button(Step, NoteOn(Gs1), NoteOff(Gs1), inactive = { 1 }, active = { 2 }),
        Button(Note, NoteOn(A1), NoteOff(A1), inactive = { 2 }, active = { 4 }),
        Button(Drum, NoteOn(As1), NoteOff(As1), inactive = { 1 }, active = { 2 }),
        Button(Perform, NoteOn(B1), NoteOff(B1), inactive = { 1 }, active = { 2 }),
        Button(Shift, NoteOn(C2), NoteOff(C2), inactive = { 1 }, active = { 2 }),
        Button(Alt, NoteOn(Cs2), NoteOff(Cs2), inactive = { 1 }, active = { 2 }),
        Button(Pattern, NoteOn(D2), NoteOff(D2), inactive = { 2 }, active = { 4 }),
        Button(
            Play,
            NoteOn(Ds2),
            NoteOff(Ds2),
            inactive = { 1 },
            active = { 3 },
            standby = blink(1.half, 1 ,3),
            highlight = { 4 }),
        Button(Stop, NoteOn(E2), NoteOff(E2), inactive = { 1 }, active = { 3 }, highlight = { 4 }),
        Button(
            Record,
            NoteOn(F2),
            NoteOff(F2),
            inactive = { 1 },
            active = { 3 },
            standby = blink(1.half, 1 ,3),
            highlight = { 4 }),
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
            pads.forEach { it.color[BACKGROUND] = 0x00_0C_38 }
            with(pads) {
                get(6, 0).color[BACKGROUND] = 0x06_02_00
                get(8, 0).color[BACKGROUND] = 0x06_02_00
                get(5, 1).color[BACKGROUND] = 0x60_20_00
                get(6, 1).color[BACKGROUND] = 0x08_0C_00
                get(7, 1).color[BACKGROUND] = 0x60_20_00
                get(8, 1).color[BACKGROUND] = 0x08_0C_00
                get(9, 1).color[BACKGROUND] = 0x60_20_00
                get(6, 2).color[BACKGROUND] = 0x60_20_00
                get(7, 2).color[BACKGROUND] = 0x60_20_00
                get(8, 2).color[BACKGROUND] = 0x60_20_00
                get(7, 3).color[BACKGROUND] = 0x08_04_00
            }
        }

        override fun onClock(midi: MidiContext, event: MidiEvent) = with(midi) {
            buttons.dirtyButtons
                .forEach { controlChange(it.mapPress.data1, it.value) }

            pads.dirtyPadsAfterUpdate(midi.midiClock)
                .map { (it.number shl 24) + (it.color.value and 0xff_ff_ff) }
                .let { firePadColors(it) }
        }
    }
}
