@file:OptIn(ExperimentalUnsignedTypes::class)

package controller.fire

import controller.Button
import controller.Buttons
import controller.Pads
import midi.activity.MidiActivity
import midi.api.MidiContext
import midi.api.MidiEvent
import midi.api.MidiNote.*
import midi.api.NoteOn
import midi.api.plus
import midi.core.controlChange
import midi.core.sixteenth

class FireController : MidiActivity("Akai Fire Pro") {

    enum class ButtonNames {
        MODE, RESERVED1, RESERVED2, RESERVED3, RESERVED4,
        PATTERN_UP, PATTERN_DOWN, BROWSER, GRID_LEFT, GRID_RIGHT,
        MUTE1, MUTE2, MUTE3, MUTE4,
        VOLUME, PAN, FILTER, RESONANCE,
        STEP, NOTE, DRUM, PERFORM, SHIFT, ALT, PATTERN, PLAY, STOP, RECORD
    }

    val buttons = Buttons(ButtonNames.values().size) { Button(NoteOn(D0 + it), ButtonNames.values()[it].name) }

    val pads = Pads(16, 4, NoteOn(Fs2))

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
        if (midiClock.ticks % 1.sixteenth != 0) {
            return
        }
        controlChange(0x1b, 2)
        controlChange(0x33, 2)

        buttons
            .filter { it.dirty }
            .onEach {
                it.dirty = false
                println("sending ${it.value} to control ${it.mapping.data1}")
                controlChange(it.mapping.data1, it.value)
            }

        firePadColors(
            (pads.indices)
                .filter { pads[it].dirty }
                .onEach { pads[it].dirty = false }
                .map { (it shl 24) + (pads[it].color and 0xff_ff_ff) }
        )

    }

    override fun onEvent(midi: MidiContext, event: MidiEvent) {

        pads.filter { it matches event }.forEach {
            println(">> ${it.name} pressed")
            it.color = 0x00_ff_00
        }

        buttons.filter { it matches event }.forEach {
            println(">> ${it.name} pressed")
            it.value = 2
        }
    }
}

