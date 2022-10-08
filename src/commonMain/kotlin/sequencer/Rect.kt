package sequencer

data class Rect (val x1: Int, val y1: Int, val width: Int, val height: Int){
    val x2 = x1 + width
    val y2 = y1 + height
}

