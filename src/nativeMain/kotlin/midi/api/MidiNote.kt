package midi.api

import midi.api.MidiNote.Companion.noteByNumber

enum class MidiNote(val number: Int) {
    `C-2`(0+12*0),
    `Cs-2`(1+12*0),
    `Db-2`(1+12*0),
    `D-2`(2+12*0),
    `Ds-2`(3+12*0),
    `Eb-2`(3+12*0),
    `E-2`(4+12*0),
    `F-2`(5+12*0),
    `Fs-2`(6+12*0),
    `Gb-2`(6+12*0),
    `G-2`(7+12*0),
    `Gs-2`(8+12*0),
    `Ab-2`(8+12*0),
    `A-2`(9+12*0),
    `As-2`(10+12*0),
    `Bb-2`(10+12*0),
    `B-2`(11+12*0),

    `C-1`(0+12*1),
    `Cs-1`(1+12*1),
    `Db-1`(1+12*1),
    `D-1`(2+12*1),
    `Ds-1`(3+12*1),
    `Eb-1`(3+12*1),
    `E-1`(4+12*1),
    `F-1`(5+12*1),
    `Fs-1`(6+12*1),
    `Gb-1`(6+12*1),
    `G-1`(7+12*1),
    `Gs-1`(8+12*1),
    `Ab-1`(8+12*1),
    `A-1`(9+12*1),
    `As-1`(10+12*1),
    `Bb-1`(10+12*1),
    `B-1`(11+12*1),

    C0(0+12*2),
    Cs0(1+12*2),
    Db0(1+12*2),
    D0(2+12*2),
    Ds0(3+12*2),
    Eb0(3+12*2),
    E0(4+12*2),
    F0(5+12*2),
    Fs0(6+12*2),
    Gb0(6+12*2),
    G0(7+12*2),
    Gs0(8+12*2),
    Ab0(8+12*2),
    A0(9+12*2),
    As0(10+12*2),
    Bb0(10+12*2),
    B0(11+12*2),

    C1(0+12*3),
    Cs1(1+12*3),
    Db1(1+12*3),
    D1(2+12*3),
    Ds1(3+12*3),
    Eb1(3+12*3),
    E1(4+12*3),
    F1(5+12*3),
    Fs1(6+12*3),
    Gb1(6+12*3),
    G1(7+12*3),
    Gs1(8+12*3),
    Ab1(8+12*3),
    A1(9+12*3),
    As1(10+12*3),
    Bb1(10+12*3),
    B1(11+12*3),

    C2(0+12*4),
    Cs2(1+12*4),
    Db2(1+12*4),
    D2(2+12*4),
    Ds2(3+12*4),
    Eb2(3+12*4),
    E2(4+12*4),
    F2(5+12*4),
    Fs2(6+12*4),
    Gb2(6+12*4),
    G2(7+12*4),
    Gs2(8+12*4),
    Ab2(8+12*4),
    A2(9+12*4),
    As2(10+12*4),
    Bb2(10+12*4),
    B2(11+12*4),

    C3(0+12*5),
    Cs3(1+12*5),
    Db3(1+12*5),
    D3(2+12*5),
    Ds3(3+12*5),
    Eb3(3+12*5),
    E3(4+12*5),
    F3(5+12*5),
    Fs3(6+12*5),
    Gb3(6+12*5),
    G3(7+12*5),
    Gs3(8+12*5),
    Ab3(8+12*5),
    A3(9+12*5),
    As3(10+12*5),
    Bb3(10+12*5),
    B3(11+12*5),

    C4(0+12*6),
    Cs4(1+12*6),
    Db4(1+12*6),
    D4(2+12*6),
    Ds4(3+12*6),
    Eb4(3+12*6),
    E4(4+12*6),
    F4(5+12*6),
    Fs4(6+12*6),
    Gb4(6+12*6),
    G4(7+12*6),
    Gs4(8+12*6),
    Ab4(8+12*6),
    A4(9+12*6),
    As4(10+12*6),
    Bb4(10+12*6),
    B4(11+12*6),

    C5(0+12*7),
    Cs5(1+12*7),
    Db5(1+12*7),
    D5(2+12*7),
    Ds5(3+12*7),
    Eb5(3+12*7),
    E5(4+12*7),
    F5(5+12*7),
    Fs5(6+12*7),
    Gb5(6+12*7),
    G5(7+12*7),
    Gs5(8+12*7),
    Ab5(8+12*7),
    A5(9+12*7),
    As5(10+12*7),
    Bb5(10+12*7),
    B5(11+12*7),

    C6(0+12*8),
    Cs6(1+12*8),
    Db6(1+12*8),
    D6(2+12*8),
    Ds6(3+12*8),
    Eb6(3+12*8),
    E6(4+12*8),
    F6(5+12*8),
    Fs6(6+12*8),
    Gb6(6+12*8),
    G6(7+12*8),
    Gs6(8+12*8),
    Ab6(8+12*8),
    A6(9+12*8),
    As6(10+12*8),
    Bb6(10+12*8),
    B6(11+12*8),

    C7(0+12*9),
    Cs7(1+12*9),
    Db7(1+12*9),
    D7(2+12*9),
    Ds7(3+12*9),
    Eb7(3+12*9),
    E7(4+12*9),
    F7(5+12*9),
    Fs7(6+12*9),
    Gb7(6+12*9),
    G7(7+12*9),
    Gs7(8+12*9),
    Ab7(8+12*9),
    A7(9+12*9),
    As7(10+12*9),
    Bb7(10+12*9),
    B7(11+12*9),

    C8(0+12*10),
    Cs8(1+12*10),
    Db8(1+12*10),
    D8(2+12*10),
    Ds8(3+12*10),
    Eb8(3+12*10),
    E8(4+12*10),
    F8(5+12*10),
    Fs8(6+12*10),
    Gb8(6+12*10),
    G8(7+12*10);

    companion object {
        val noteByNumber = arrayOf(
            `C-2`,
            `Cs-2`,
            `D-2`,
            `Ds-2`,
            `E-2`,
            `F-2`,
            `Fs-2`,
            `G-2`,
            `Gs-2`,
            `A-2`,
            `As-2`,
            `B-2`,

            `C-1`,
            `Cs-1`,
            `D-1`,
            `Ds-1`,
            `E-1`,
            `F-1`,
            `Fs-1`,
            `G-1`,
            `Gs-1`,
            `A-1`,
            `As-1`,
            `B-1`,

            C0,
            Cs0,
            D0,
            Ds0,
            E0,
            F0,
            Fs0,
            G0,
            Gs0,
            A0,
            As0,
            B0,

            C1,
            Cs1,
            D1,
            Ds1,
            E1,
            F1,
            Fs1,
            G1,
            Gs1,
            A1,
            As1,
            B1,

            C2,
            Cs2,
            D2,
            Ds2,
            E2,
            F2,
            Fs2,
            G2,
            Gs2,
            A2,
            As2,
            B2,

            C3,
            Cs3,
            D3,
            Ds3,
            E3,
            F3,
            Fs3,
            G3,
            Gs3,
            A3,
            As3,
            B3,

            C4,
            Cs4,
            D4,
            Ds4,
            E4,
            F4,
            Fs4,
            G4,
            Gs4,
            A4,
            As4,
            B4,

            C5,
            Cs5,
            D5,
            Ds5,
            E5,
            F5,
            Fs5,
            G5,
            Gs5,
            A5,
            As5,
            B5,

            C6,
            Cs6,
            D6,
            Ds6,
            E6,
            F6,
            Fs6,
            G6,
            Gs6,
            A6,
            As6,
            B6,

            C7,
            Cs7,
            D7,
            Ds7,
            E7,
            F7,
            Fs7,
            G7,
            Gs7,
            A7,
            As7,
            B7,

            C8,
            Cs8,
            D8,
            Ds8,
            E8,
            F8,
            Fs8,
            G8
        )
    }
}

operator fun MidiNote.plus(offset: Int) = noteByNumber[this.number + offset]
operator fun Int.plus(base: MidiNote) = noteByNumber[this + base.number]
