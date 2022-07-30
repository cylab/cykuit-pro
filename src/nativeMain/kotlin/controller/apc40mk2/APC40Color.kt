package controller.apc40mk2

import midi.core.justify
import kotlin.math.abs

enum class APC40Color(val number: Int) {
    White(3), // #ffffff
    Black(0), // #000000

    // Yellow colors
    Yellow(12), // #ffff4c
    Khaki2(113), // #dcff6b
    Gold(109), // #ffe126
    Yellow4(97), // #b9b000
    Gold8(100), // #392b00
    Gold9(15), // #191900

    // Red colors
    LightSalmon(8), // #ffbd6c
    Crimson(57), // #ff0054
    Red(5), // #ff0000
    Red4(106), // #a8000a
    Crimson6(58), // #59001d
    DarkRed4(6), // #590000

    // Purple colors
    Purple(52), // #ff4cff
    Magenta(52), // #ff4cff
    Fuchsia2(94), // #d31dff
    MediumSlateBlue(48), // #874cff
    DarkViolet(81), // #7a00ff
    DarkMagenta4(54), // #590059
    MediumPurple8(112), // #1e1e30
    SlateBlue8(103), // #15152a
    Indigo7(51), // #0f0030
    Purple8(55), // #190019

    // Pink colors
    HotPink3(56), // #ff4c87
    DeepPink(95), // #ff005d
    MediumVioletRed2(82), // #b21a7d
    DeepPink9(59), // #220013

    // Orange colors
    Tomato(4), // #ff4c4c
    DarkOrange(96), // #ff7f00
    Tomato2(107), // #de513d
    OrangeRed(9), // #ff5400
    DarkOrange4(126), // #b35f00
    OrangeRed4(61), // #993500
    Orange6(62), // #795100
    OrangeRed7(10), // #591d00
    DarkOrange8(83), // #402100

    // Grey colors
    Gray(2), // #7f7f7f
    LightGray5(118), // #757575
    Silver7(117), // #404040
    DimGray7(71), // #202020
    Gray8(1), // #1e1e1e

    // Green colors
    GreenYellow(16), // #88ff4c
    LightGreen3(89), // #59ff71
    GreenYellow2(110), // #9ee12f
    SpringGreen(24), // #4cff5e
    MediumSpringGreen(33), // #00ff99
    LimeGreen(20), // #4cff4c
    Chartreuse(98), // #90ff00
    Chartreuse2(85), // #88e106
    LawnGreen(17), // #54ff00
    LawnGreen3(111), // #67b50f
    Lime(21), // #00ff00
    Lime3(122), // #1ad000
    LawnGreen6(63), // #436400
    Lime5(76), // #108b00
    MediumSeaGreen6(102), // #0d5038
    Teal5(68), // #00454f
    MediumSpringGreen7(34), // #005935
    LawnGreen7(18), // #1d5900
    SpringGreen7(30), // #00591d
    ForestGreen5(101), // #144c10
    DarkGreen2(22), // #005900
    DarkGreen4(123), // #074200
    LawnGreen8(19), // #142b00
    Green6(64), // #033900
    MediumSpringGreen9(31), // #001f12
    DarkGreen8(23), // #001900

    // Cyan colors
    LightCyan(119), // #e0ffff
    Aquamarine(114), // #80ffbd
    MediumTurquoise(36), // #4cc3ff
    Turquoise(32), // #4cffb7

    // Brown colors
    Chocolate(108), // #d86a1c
    DarkGoldenRod4(99), // #835d07
    Sienna4(105), // #693c1c
    Olive4(14), // #595900
    DarkGoldenRod8(11), // #271b00
    Maroon6(121), // #350000
    Maroon8(7), // #190000

    // Blue colors
    CornflowerBlue(91), // #5b8aff
    DodgerBlue(40), // #4c88ff
    DeepSkyBlue(37), // #00a9ff
    RoyalBlue(44), // #4c4cff
    Blue(41), // #0055ff
    RoyalBlue2(92), // #3151c6
    MediumBlue(69), // #2500cc
    DeepSkyBlue6(66), // #00547f
    DeepSkyBlue7(38), // #004152
    RoyalBlue7(104), // #16205a
    Navy3(50), // #190064
    MediumBlue6(42), // #001d59
    DodgerBlue9(39), // #001019
    MidnightBlue8(43), // #000819
    MediumBlue9(47); // #000019

    companion object {
        fun yellow(shade: Int = 2) = when(shade) {
            0 -> 100 // #392b00
            1 -> 13 // #ffff00
            else -> 12 // #ffff4c
        }

        fun red(shade: Int = 2) = when(shade) {
            0 -> 7 // #190000 -> red
            1 -> 6 // #590000
            else -> 5 // #ff0000
        }

        fun purple(shade: Int = 2) = when(shade) {
            0 -> 51 // #0f0030
            1 -> 81 // #7a00ff
            else -> 48 // #874cff
        }

        fun pink(shade: Int = 2) = when(shade) {
            0 -> 59 // #220013
            1 -> 82 // #b21a7d
            else -> 52 // #ff4cff
        }

        fun orange(shade: Int = 2) = when(shade) {
            0 -> 10 // #591d00
            1 -> 61 // #993500
            else -> 96 // #ff7f00
        }

        fun grey(shade: Int = 2) = when(shade) {
            0 -> 71 // #202020
            1 -> 2 // #7f7f7f
            else -> 119 // #e0ffff
        }

//        fun greenYellow(shade: Int = 2) = when(shade) {
//            0 -> GreenYellow.number
//            1 -> LawnGreen3.number
//            2 -> LawnGreen6.number
//            else -> Black.number
//        }

        fun green(shade: Int = 2) = when(shade) {
            0 -> 23 // #001900 -> green
            1 -> 22 // #005900 -> green
            else -> 21 // #00ff00 -> green
        }

        fun cyan(shade: Int = 2) = when(shade) {
            0 -> 38 // #004152 -> cyan
            1 -> 37 // #00a9ff -> cyan
            else -> 90 // #38ffcc
        }

        fun brown(shade: Int = 2) = when(shade) {
            0 -> 105 // #693c1c
            1 -> 108 // #d86a1c
            else -> 113 // #dcff6b -> beige
        }

//        fun olive(shade: Int = 2) = when(shade) {
//            0 -> LawnGreen6.number
//            1 -> Olive4.number
//            2 -> DarkGoldenRod8.number
//            else -> Black.number
//        }
//
//        fun cornFlower(shade: Int = 2) = when(shade) {
//            0 -> CornflowerBlue.number
//            1 -> RoyalBlue2.number
//            2 -> RoyalBlue7.number
//            else -> Black.number
//        }

        fun blue(shade: Int = 2) = when(shade) {
            0 -> 67 // #0000ff
            1 -> 79 // #002aff
            else -> 41 // #0055ff
        }

        // White colors
        fun allWhite(shade: Int = 0) = listOf(
            3, // #ffffff
            119, // #e0ffff
        ).let {
            when{
                shade >= it.size -> Black.number
                else -> it[shade]
            }
        }

        // Red colors
        fun allRed(shade: Int = 0) = listOf(
            5, // #ff0000
            6, // #590000
            7, // #190000 -> red
            0,
            0,
            0,
            0,
            0,

            57, // #ff0054
            106, // #a8000a
            58, // #59001d
            0,
            0,
            0,
            0,
            0,

            8, // #ffbd6c
            57, // #ff0054
            60, // #ff1500
            5, // #ff0000
            72, // #ff0000
            106, // #a8000a
            120, // #a00000
            58, // #59001d

            6, // #590000
        ).let {
            when{
                shade >= it.size -> Black.number
                else -> it[shade]
            }
        }

        // Purple colors
        fun allPurple(shade: Int = 0) = listOf(
            48, // #874cff
            81, // #7a00ff
            51, // #0f0030
            0,
            0,
            0,
            0,
            0,

            52, // #ff4cff
            53, // #ff00ff
            116, // #8e66ff
            93, // #877fe9
            94, // #d31dff
            48, // #874cff
            54, // #590059
            0,

            112, // #1e1e30
            103, // #15152a
            51, // #0f0030
            55, // #190019
            0,
            0,
            0,
            0,
            0,

            81, // #7a00ff
            51, // #0f0030

        ).let {
            when{
                shade >= it.size -> Black.number
                else -> it[shade]
            }
        }

        // Pink colors
        fun allPink(shade: Int = 0) = listOf(
            52, // #ff4cff
            82, // #b21a7d
            59, // #220013
            0,
            0,
            0,
            0,
            0,

            53, // #ff00ff
            54, // #590059
            55, // #190019
            0,
            0,
            0,
            0,
            0,

            56, // #ff4c87
            4, // #ff4c4c
            107, // #de513d
            95, // #ff005d
            82, // #b21a7d
            59, // #220013
            0,
            0,

            52, // #ff4cff
            53, // #ff00ff
            94, // #d31dff
            48, // #874cff
            81, // #7a00ff
            54, // #590059
            0,
            0,

            116, // #8e66ff
            93, // #877fe9
            112, // #1e1e30
            103, // #15152a
            51, // #0f0030
            55, // #190019
        ).let {
            when{
                shade >= it.size -> Black.number
                else -> it[shade]
            }
        }

        // Orange colors
        fun allOrange(shade: Int = 0) = listOf(
            96, // #ff7f00
            61, // #993500
            10, // #591d00
            0,
            0,
            0,
            0,
            0,

            9, // #ff5400
//            84, // #ff4a00
            61, // #993500
            10, // #591d00
            0,
            0,
            0,
            0,
            0,

            96, // #ff7f00
            126, // #b35f00
//            62, // #795100
            127, // #4b1502
            0,
            0,
            60, // #ff1500
            0,
            0,

            86, // #72ff15 -> yellow
            98, // #90ff00 -> yellow
            74, // #afed06 -> orange
            85, // #88e106 -> yellow
            75, // #64ff09 -> yellow
            17, // #54ff00 -> yellow
            18, // #1d5900 -> yellow
            0,

            73, // #bdff2d -> brown
            16, // #88ff4c -> beige
            110, // #9ee12f -> brown
            111, // #67b50f -> brown
            63, // #436400 -> orange/brown
            19, // #142b00 -> brown
            0,
            0,

        ).let {
            when{
                shade >= it.size -> Black.number
                else -> it[shade]
            }
        }

        // Grey colors
        fun allGrey(shade: Int = 0) = listOf(
            2, // #7f7f7f
            70, // #7f7f7f
            118, // #757575
            117, // #404040
            71, // #202020
            1, // #1e1e1e
            0, // #000000
        ).let {
            when{
                shade >= it.size -> Black.number
                else -> it[shade]
            }
        }

        // Green colors
        fun allGreen(shade: Int = 0) = listOf(
//            20, // #4cff4c -> green gray
            21, // #00ff00 -> green
            22, // #005900 -> green
            23, // #001900 -> green
//            123, // #074200 -> green
//            64, // #033900 -> green
            0,
            0,
            0,
            0,
            0,

            88, // #3bff26 -> green
//            122, // #1ad000 -> green
            76, // #108b00 -> green
            101, // #144c10 -> grey

        ).let {
            when{
                shade >= it.size -> Black.number
                else -> it[shade]
            }
        }

        // Brown colors
        fun allBrown(shade: Int = 0) = listOf(
            113, // #dcff6b -> beige
            108, // #d86a1c
            105, // #693c1c
            11, // #271b00
            0,
            0,
            0,
            0,

            86, // #72ff15 -> yellow
            98, // #90ff00 -> yellow
            74, // #afed06 -> orange
            85, // #88e106 -> yellow
            75, // #64ff09 -> yellow
            17, // #54ff00 -> yellow
            18, // #1d5900 -> yellow
            0,

            89, // #59ff71 -> grey
            73, // #bdff2d -> brown
            16, // #88ff4c -> beige
            110, // #9ee12f -> brown
            111, // #67b50f -> brown
            63, // #436400 -> orange/brown
            19, // #142b00 -> brown
            0,

            83, // #402100
            12, // #ffff4c -> yellow
            13, // #ffff00
            100, // #392b00
            15, // #191900
            114, // #80ffbd -> gelb
            36, // #4cc3ff
            90, // #38ffcc
            32, // #4cffb7
            44, // #4c4cff -> pink
            91, // #5b8aff -> grau
            40, // #4c88ff -> grau
            92, // #3151c6 -> grau
            104, // #16205a -> magenta
            115, // #9a99ff -> hellbraun?
            97, // #b9b000
            124, // #b9b000
            12, // #ffff4c -> beige?
            109, // #ffe126 -> orange/brown?
            125, // #3f3100 -> brown?
            42, // #001d59
            121, // #350000 -> red
            7, // #190000 -> red
        ).let {
            when{
                shade >= it.size -> Black.number
                else -> it[shade]
            }
        }

        // Yellow colors
        fun allYellow(shade: Int = 0) = listOf(
            12, // #ffff4c
            13, // #ffff00
            100, // #392b00
//            97, // #b9b000
//            124, // #b9b000
//            109, // #ffe126 -> orange/brown?
//            125, // #3f3100 -> brown?
            15, // #191900
            0,
            0,
            0,
            0,

            86, // #72ff15 -> yellow
            98, // #90ff00 -> yellow
            74, // #afed06 -> orange
            85, // #88e106 -> yellow
            75, // #64ff09 -> yellow
            17, // #54ff00 -> yellow
            18, // #1d5900 -> yellow
            0,

            73, // #bdff2d -> brown
            16, // #88ff4c -> beige
            110, // #9ee12f -> brown
            111, // #67b50f -> brown
            63, // #436400 -> orange/brown
            19, // #142b00 -> brown
            0,
            0,

            99, // #835d07 -> yellow
            14, // #595900  -> yellow
            0,
            0,
            0,
            0,
            0,
            0,
            12, // #ffff4c -> beige?
            113, // #dcff6b -> beige
            109, // #ffe126 -> orange/brown?
            0,
            0,
            114, // #80ffbd -> gelb
            36, // #4cc3ff
            90, // #38ffcc
            32, // #4cffb7
            0,
            0,
            44, // #4c4cff -> pink
            91, // #5b8aff -> grau
            40, // #4c88ff -> grau
            92, // #3151c6 -> grau
            104, // #16205a -> magenta
            0,
            0,
            0,
            115, // #9a99ff -> hellbraun?
            42, // #001d59
        ).let {
            when{
                shade >= it.size -> Black.number
                else -> it[shade]
            }
        }

        // Cyan colors
        fun allCyan(shade: Int = 0) = listOf(
            90, // #38ffcc
            37, // #00a9ff -> cyan
            38, // #004152 -> cyan
//            66, // #00547f -> cyan
            39, // #001019 -> cyan
            0,
            0,
            0,
            0,

            114, // #80ffbd -> gelb
            28, // #4cff88 -> grey
            24, // #4cff5e -> light cyan
            36, // #4cc3ff
            90, // #38ffcc
            32, // #4cffb7
            0,
            0,

            29, // #00ff55 -> cyan
            25, // #00ff19 -> green
            30, // #00591d -> cyan
            26, // #00590d -> cyan
            0,
            0,
            0,
            0,

            ).let {
            when{
                shade >= it.size -> Black.number
                else -> it[shade]
            }
        }

        // Blue colors
        fun allBlue(shade: Int = 0) : Int = listOf(
            41, // #0055ff
            79, // #002aff
            67, // #0000ff
//            45, // #0000ff
            46, // #000059
            47, // #000019
            43, // #000819
            0,
            0,

            77, // #00ff87 -> blueish cyan
            33, // #00ff99 -> blueish cyan
            34, // #005935 -> blue
            102, // #0d5038 -> blue
            68, // #00454f -> blue
            65, // #005735 -> blue
            31, // #001f12 -> blue
            35, // #001912 -> blue

            49, // #5400ff -> magenta
            80, // #3f00ff -> magenta
            69, // #2500cc -> magenta
            50, // #190064 -> magenta
            0,
            0,
            0,
            0,
            44, // #4c4cff -> pink
            91, // #5b8aff -> grau
            40, // #4c88ff -> grau
            92, // #3151c6 -> grau
            104, // #16205a -> magenta
            0,
            0,
            0,
            115, // #9a99ff -> hellbraun?
            42, // #001d59
        ).let {
            when{
                shade >= it.size -> Black.number
                else -> it[shade]
            }
        }
    }
}


fun makeColors() {
    val apcColors = listOf(
        ApcColor(0, 0x000000),
        ApcColor(1, 0x1E1E1E),
        ApcColor(2, 0x7F7F7F),
        ApcColor(3, 0xFFFFFF),
        ApcColor(4, 0xFF4C4C),
        ApcColor(5, 0xFF0000),
        ApcColor(6, 0x590000),
        ApcColor(7, 0x190000),
        ApcColor(8, 0xFFBD6C),
        ApcColor(9, 0xFF5400),
        ApcColor(10, 0x591D00),
        ApcColor(11, 0x271B00),
        ApcColor(12, 0xFFFF4C),
        ApcColor(13, 0xFFFF00),
        ApcColor(14, 0x595900),
        ApcColor(15, 0x191900),
        ApcColor(16, 0x88FF4C),
        ApcColor(17, 0x54FF00),
        ApcColor(18, 0x1D5900),
        ApcColor(19, 0x142B00),
        ApcColor(20, 0x4CFF4C),
        ApcColor(21, 0x00FF00),
        ApcColor(22, 0x005900),
        ApcColor(23, 0x001900),
        ApcColor(24, 0x4CFF5E),
        ApcColor(25, 0x00FF19),
        ApcColor(26, 0x00590D),
        ApcColor(27, 0x001902),
        ApcColor(28, 0x4CFF88),
        ApcColor(29, 0x00FF55),
        ApcColor(30, 0x00591D),
        ApcColor(31, 0x001F12),
        ApcColor(32, 0x4CFFB7),
        ApcColor(33, 0x00FF99),
        ApcColor(34, 0x005935),
        ApcColor(35, 0x001912),
        ApcColor(36, 0x4CC3FF),
        ApcColor(37, 0x00A9FF),
        ApcColor(38, 0x004152),
        ApcColor(39, 0x001019),
        ApcColor(40, 0x4C88FF),
        ApcColor(41, 0x0055FF),
        ApcColor(42, 0x001D59),
        ApcColor(43, 0x000819),
        ApcColor(44, 0x4C4CFF),
        ApcColor(45, 0x0000FF),
        ApcColor(46, 0x000059),
        ApcColor(47, 0x000019),
        ApcColor(48, 0x874CFF),
        ApcColor(49, 0x5400FF),
        ApcColor(50, 0x190064),
        ApcColor(51, 0x0F0030),
        ApcColor(52, 0xFF4CFF),
        ApcColor(53, 0xFF00FF),
        ApcColor(54, 0x590059),
        ApcColor(55, 0x190019),
        ApcColor(56, 0xFF4C87),
        ApcColor(57, 0xFF0054),
        ApcColor(58, 0x59001D),
        ApcColor(59, 0x220013),
        ApcColor(60, 0xFF1500),
        ApcColor(61, 0x993500),
        ApcColor(62, 0x795100),
        ApcColor(63, 0x436400),
        ApcColor(64, 0x033900),
        ApcColor(65, 0x005735),
        ApcColor(66, 0x00547F),
        ApcColor(67, 0x0000FF),
        ApcColor(68, 0x00454F),
        ApcColor(69, 0x2500CC),
        ApcColor(70, 0x7F7F7F),
        ApcColor(71, 0x202020),
        ApcColor(72, 0xFF0000),
        ApcColor(73, 0xBDFF2D),
        ApcColor(74, 0xAFED06),
        ApcColor(75, 0x64FF09),
        ApcColor(76, 0x108B00),
        ApcColor(77, 0x00FF87),
        ApcColor(78, 0x00A9FF),
        ApcColor(79, 0x002AFF),
        ApcColor(80, 0x3F00FF),
        ApcColor(81, 0x7A00FF),
        ApcColor(82, 0xB21A7D),
        ApcColor(83, 0x402100),
        ApcColor(84, 0xFF4A00),
        ApcColor(85, 0x88E106),
        ApcColor(86, 0x72FF15),
        ApcColor(87, 0x00FF00),
        ApcColor(88, 0x3BFF26),
        ApcColor(89, 0x59FF71),
        ApcColor(90, 0x38FFCC),
        ApcColor(91, 0x5B8AFF),
        ApcColor(92, 0x3151C6),
        ApcColor(93, 0x877FE9),
        ApcColor(94, 0xD31DFF),
        ApcColor(95, 0xFF005D),
        ApcColor(96, 0xFF7F00),
        ApcColor(97, 0xB9B000),
        ApcColor(98, 0x90FF00),
        ApcColor(99, 0x835D07),
        ApcColor(100, 0x392b00),
        ApcColor(101, 0x144C10),
        ApcColor(102, 0x0D5038),
        ApcColor(103, 0x15152A),
        ApcColor(104, 0x16205A),
        ApcColor(105, 0x693C1C),
        ApcColor(106, 0xA8000A),
        ApcColor(107, 0xDE513D),
        ApcColor(108, 0xD86A1C),
        ApcColor(109, 0xFFE126),
        ApcColor(110, 0x9EE12F),
        ApcColor(111, 0x67B50F),
        ApcColor(112, 0x1E1E30),
        ApcColor(113, 0xDCFF6B),
        ApcColor(114, 0x80FFBD),
        ApcColor(115, 0x9A99FF),
        ApcColor(116, 0x8E66FF),
        ApcColor(117, 0x404040),
        ApcColor(118, 0x757575),
        ApcColor(119, 0xE0FFFF),
        ApcColor(120, 0xA00000),
        ApcColor(121, 0x350000),
        ApcColor(122, 0x1AD000),
        ApcColor(123, 0x074200),
        ApcColor(124, 0xB9B000),
        ApcColor(125, 0x3F3100),
        ApcColor(126, 0xB35F00),
        ApcColor(127, 0x4B1502),
    )

    val cssColors = listOf(
        // White Colors
        CssColor("White", "White", 0xFFFFFF),
        CssColor("White", "Snow", 0xFFFAFA),
        CssColor("White", "HoneyDew", 0xF0FFF0),
        CssColor("White", "MintCream", 0xF5FFFA),
        CssColor("White", "Azure", 0xF0FFFF),
        CssColor("White", "AliceBlue", 0xF0F8FF),
        CssColor("White", "GhostWhite", 0xF8F8FF),
        CssColor("White", "WhiteSmoke", 0xF5F5F5),
        CssColor("White", "SeaShell", 0xFFF5EE),
        CssColor("White", "Beige", 0xF5F5DC),
        CssColor("White", "OldLace", 0xFDF5E6),
        CssColor("White", "FloralWhite", 0xFFFAF0),
        CssColor("White", "Ivory", 0xFFFFF0),
        CssColor("White", "AntiqueWhite", 0xFAEBD7),
        CssColor("White", "Linen", 0xFAF0E6),
        CssColor("White", "LavenderBlush", 0xFFF0F5),
        CssColor("White", "MistyRose", 0xFFE4E1),
        CssColor("White", "LightCyan", 0xE0FFFF),

        // Red Colors
        CssColor("Red", "LightSalmon", 0xFFA07A),
        CssColor("Red", "Salmon", 0xFA8072),
        CssColor("Red", "DarkSalmon", 0xE9967A),
        CssColor("Red", "LightCoral", 0xF08080),
        CssColor("Red", "IndianRed", 0xCD5C5C),
        CssColor("Red", "Crimson", 0xDC143C),
        CssColor("Red", "Red", 0xFF0000),
        CssColor("Red", "FireBrick", 0xB22222),
        CssColor("Red", "DarkRed", 0x8B0000),

        // Orange Colors
        CssColor("Orange", "Orange", 0xFFA500),
        CssColor("Orange", "DarkOrange", 0xFF8C00),
        CssColor("Orange", "Coral", 0xFF7F50),
        CssColor("Orange", "Tomato", 0xFF6347),
        CssColor("Orange", "OrangeRed", 0xFF4500),

        // Brown Colors
        CssColor("Brown", "Cornsilk", 0xFFF8DC),
        CssColor("Brown", "BlanchedAlmond", 0xFFEBCD),
        CssColor("Brown", "Bisque", 0xFFE4C4),
        CssColor("Brown", "NavajoWhite", 0xFFDEAD),
        CssColor("Brown", "Wheat", 0xF5DEB3),
        CssColor("Brown", "BurlyWood", 0xDEB887),
        CssColor("Brown", "Tan", 0xD2B48C),
        CssColor("Brown", "RosyBrown", 0xBC8F8F),
        CssColor("Brown", "SandyBrown", 0xF4A460),
        CssColor("Brown", "GoldenRod", 0xDAA520),
        CssColor("Brown", "DarkGoldenRod", 0xB8860B),
        CssColor("Brown", "Peru", 0xCD853F),
        CssColor("Brown", "Chocolate", 0xD2691E),
        CssColor("Brown", "Olive", 0x808000),
        CssColor("Brown", "SaddleBrown", 0x8B4513),
        CssColor("Brown", "Sienna", 0xA0522D),
        CssColor("Brown", "Brown", 0xA52A2A),
        CssColor("Brown", "Maroon", 0x800000),

        // Cyan Colors
        CssColor("Cyan", "Aqua", 0x00FFFF),
        CssColor("Cyan", "Cyan", 0x00FFFF),
//        CssColor("Cyan", "LightCyan", 0xE0FFFF),
        CssColor("Cyan", "PaleTurquoise", 0xAFEEEE),
        CssColor("Cyan", "Aquamarine", 0x7FFFD4),
        CssColor("Cyan", "Turquoise", 0x40E0D0),
        CssColor("Cyan", "MediumTurquoise", 0x48D1CC),
        CssColor("Cyan", "DarkTurquoise", 0x00CED1),

        // Pink Colors
        CssColor("Pink", "Pink", 0xFFC0CB),
        CssColor("Pink", "LightPink", 0xFFB6C1),
        CssColor("Pink", "HotPink", 0xFF69B4),
        CssColor("Pink", "DeepPink", 0xFF1493),
        CssColor("Pink", "PaleVioletRed", 0xDB7093),
        CssColor("Pink", "MediumVioletRed", 0xC71585),

        // Purple Colors
        CssColor("Purple", "Lavender", 0xE6E6FA),
        CssColor("Purple", "Thistle", 0xD8BFD8),
        CssColor("Purple", "Plum", 0xDDA0DD),
        CssColor("Purple", "Orchid", 0xDA70D6),
        CssColor("Purple", "Violet", 0xEE82EE),
        CssColor("Purple", "Fuchsia", 0xFF00FF),
        CssColor("Purple", "Magenta", 0xFF00FF),
        CssColor("Purple", "MediumOrchid", 0xBA55D3),
        CssColor("Purple", "DarkOrchid", 0x9932CC),
        CssColor("Purple", "DarkViolet", 0x9400D3),
        CssColor("Purple", "BlueViolet", 0x8A2BE2),
        CssColor("Purple", "DarkMagenta", 0x8B008B),
        CssColor("Purple", "Purple", 0x800080),
        CssColor("Purple", "MediumPurple", 0x9370DB),
        CssColor("Purple", "MediumSlateBlue", 0x7B68EE),
        CssColor("Purple", "SlateBlue", 0x6A5ACD),
        CssColor("Purple", "DarkSlateBlue", 0x483D8B),
        CssColor("Purple", "RebeccaPurple", 0x663399),
        CssColor("Purple", "Indigo", 0x4B0082),

        // Yellow Colors
        CssColor("Yellow", "Gold", 0xFFD700),
        CssColor("Yellow", "Yellow", 0xFFFF00),
        CssColor("Yellow", "LightYellow", 0xFFFFE0),
        CssColor("Yellow", "LemonChiffon", 0xFFFACD),
        CssColor("Yellow", "LightGoldenRodYellow", 0xFAFAD2),
        CssColor("Yellow", "PapayaWhip", 0xFFEFD5),
        CssColor("Yellow", "Moccasin", 0xFFE4B5),
        CssColor("Yellow", "PeachPuff", 0xFFDAB9),
        CssColor("Yellow", "PaleGoldenRod", 0xEEE8AA),
        CssColor("Yellow", "Khaki", 0xF0E68C),
        CssColor("Yellow", "DarkKhaki", 0xBDB76B),

        // Green Colors
        CssColor("Green", "GreenYellow", 0xADFF2F),
        CssColor("Green", "Chartreuse", 0x7FFF00),
        CssColor("Green", "LawnGreen", 0x7CFC00),
        CssColor("Green", "Lime", 0x00FF00),
        CssColor("Green", "LimeGreen", 0x32CD32),
        CssColor("Green", "PaleGreen", 0x98FB98),
        CssColor("Green", "LightGreen", 0x90EE90),
        CssColor("Green", "MediumSpringGreen", 0x00FA9A),
        CssColor("Green", "SpringGreen", 0x00FF7F),
        CssColor("Green", "MediumSeaGreen", 0x3CB371),
        CssColor("Green", "SeaGreen", 0x2E8B57),
        CssColor("Green", "ForestGreen", 0x228B22),
        CssColor("Green", "Green", 0x008000),
        CssColor("Green", "DarkGreen", 0x006400),
        CssColor("Green", "YellowGreen", 0x9ACD32),
        CssColor("Green", "OliveDrab", 0x6B8E23),
        CssColor("Green", "DarkOliveGreen", 0x556B2F),
        CssColor("Green", "MediumAquaMarine", 0x66CDAA),
        CssColor("Green", "DarkSeaGreen", 0x8FBC8F),
        CssColor("Green", "LightSeaGreen", 0x20B2AA),
        CssColor("Green", "DarkCyan", 0x008B8B),
        CssColor("Green", "Teal", 0x008080),

        // Blue Colors
        CssColor("Blue", "CadetBlue", 0x5F9EA0),
        CssColor("Blue", "SteelBlue", 0x4682B4),
        CssColor("Blue", "LightSteelBlue", 0xB0C4DE),
        CssColor("Blue", "LightBlue", 0xADD8E6),
        CssColor("Blue", "PowderBlue", 0xB0E0E6),
        CssColor("Blue", "LightSkyBlue", 0x87CEFA),
        CssColor("Blue", "SkyBlue", 0x87CEEB),
        CssColor("Blue", "CornflowerBlue", 0x6495ED),
        CssColor("Blue", "DeepSkyBlue", 0x00BFFF),
        CssColor("Blue", "DodgerBlue", 0x1E90FF),
        CssColor("Blue", "RoyalBlue", 0x4169E1),
        CssColor("Blue", "Blue", 0x0000FF),
        CssColor("Blue", "MediumBlue", 0x0000CD),
        CssColor("Blue", "DarkBlue", 0x00008B),
        CssColor("Blue", "Navy", 0x000080),
        CssColor("Blue", "MidnightBlue", 0x191970),

        // Grey Colors
        CssColor("Grey", "Gainsboro", 0xDCDCDC),
        CssColor("Grey", "LightGray", 0xD3D3D3),
        CssColor("Grey", "Silver", 0xC0C0C0),
        CssColor("Grey", "DarkGray", 0xA9A9A9),
        CssColor("Grey", "DimGray", 0x696969),
        CssColor("Grey", "Gray", 0x808080),
        CssColor("Grey", "LightSlateGray", 0x778899),
        CssColor("Grey", "SlateGray", 0x708090),
        CssColor("Grey", "DarkSlateGray", 0x2F4F4F),
        CssColor("Grey", "Black", 0x000000),
    )
//    startApc40()

    val shadedCssColors = LinkedHashMap<Int, CssColor>()
    cssColors.forEach { shadedCssColors[it.code] = it }

    (1..9).forEach { shade ->
        cssColors.forEach {
            val color = it.copy(
                name = it.name + "${shade+1}",
                r = it.r * (9-shade) / 9,
                g = it.g * (9-shade) / 9,
                b = it.b * (9-shade) / 9,
            )
            if(!shadedCssColors.containsKey(color.code)){
                shadedCssColors[color.code] = color
            }
        }
    }


    val result = mutableMapOf<ApcColor, CssColor>()
    apcColors.forEach { apcColor ->
        var foundDiffR = 255
        var foundDiffG = 255
        var foundDiffB = 255
        var foundColor : CssColor? = null
//        cssColors.forEach { cssColor ->
        shadedCssColors.values.forEach { cssColor ->
            var diffR = abs(cssColor.r - apcColor.r)
            var diffG = abs(cssColor.g - apcColor.g)
            var diffB = abs(cssColor.b - apcColor.b)
            if (diffR + diffG + diffB <= foundDiffR + foundDiffG + foundDiffB) {
                foundDiffR = diffR
                foundDiffG = diffG
                foundDiffB = diffB
                foundColor = cssColor
            }
        }
        val alreadyMatchedColor = result[apcColor]
        var prevDiffR = abs(alreadyMatchedColor?.r ?: 0 - apcColor.r)
        var prevDiffG = abs(alreadyMatchedColor?.g ?: 0 - apcColor.g)
        var prevDiffB = abs(alreadyMatchedColor?.b ?: 0 - apcColor.b)
        if (foundDiffR + foundDiffG + foundDiffB <= prevDiffR + prevDiffG + prevDiffB) {
            result[apcColor] = foundColor!!
        }
    }
    result.entries.toList()
        .sortedByDescending { (apcColor, cssColor) ->
            cssColor.group + justify(apcColor.brightness to 4).replace(" ", "0")
        }
        .groupBy { (_, cssColor) -> cssColor.group }
        .onEach { (group, colorMap) ->
            println("    /* ${group} colors */")
            colorMap.forEach { (apcColor, cssColor) ->
                println("    color: #${justify(apcColor.code.toString(16) to 6).replace(" ", "0")}; /* ${cssColor.name} */")
            }
        }
        .onEach { (group, colorMap) ->
            println()
            println("""
                // ${group} colors
                fun all${group}(shade: Int = 0) = listOf(
                """.trimIndent().prependIndent("    "))

            colorMap.forEach { (apcColor, cssColor) ->
//                println("    ${cssColor.name}(${apcColor.value}), // #${justify(apcColor.code.toString(16) to 6).replace(" ", "0")}")
                println("        ${apcColor.number}, // #${justify(apcColor.code.toString(16) to 6).replace(" ", "0")}")
            }
            println("""
                ).let {
                    when{
                        shade >= it.size -> Black
                        else -> it[shade]
                    }
                }.number
                """.trimIndent().prependIndent("    "))
        }
}

data class ApcColor(val number: Int, val r: Int, val g: Int, val b: Int) {
    constructor(number: Int, code: Int) : this(
        number,
        (code shr 16) and 0xFF,
        (code shr 8) and 0xFF,
        code and 0xFF
    )
    val code = (r shl 16) + (g shl 8) + b
    val brightness = r + g + b
}

data class CssColor(val group: String, val name: String, val r: Int, val g: Int, val b: Int) {
    constructor(group: String, name: String, code: Int) : this(
        group,
        name,
        (code shr 16) and 0xFF,
        (code shr 8) and 0xFF,
        code and 0xFF
    )
    val code = (r shl 16) + (g shl 8) + b
}

