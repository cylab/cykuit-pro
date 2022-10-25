package midi.core

import io.kotest.matchers.shouldBe
import midi.api.*
import org.junit.jupiter.api.Test

class MidiGroupTest {

    @Test
    fun `should execute MidiFuns`() {
        val results = mutableListOf<MidiEvent>()
        val midiContext = object : MidiContext {
            override val midiClock: MidiClock; get() = StoppedClock
            override var channel: Int; get() = 0; set(value) {}
            override fun emit(event: MidiEvent, defer: Int) {
                results.add(event)
            }
        }
        val group = MidiGroup()
        with(group) {
            add { emit(NoteOn(MidiNote.C1)) }
            add { emit(NoteOn(MidiNote.C2)) }
            add { emit(NoteOn(MidiNote.C3)) }
        }
        group.process(midiContext, NOOPEvent)
        results.size shouldBe 3
        results[0].isNoteOn(MidiNote.C1) shouldBe true
        results[1].isNoteOn(MidiNote.C2) shouldBe true
        results[2].isNoteOn(MidiNote.C3) shouldBe true
    }
}
