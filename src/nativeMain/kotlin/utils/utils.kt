package utils

import kotlinx.cinterop.*

fun CPointer<CPointerVar<ByteVar>>.toKStrings() = generateSequence(0) { it+1 }
    .map { this[it] }
    .takeWhile { it != null }
//    .map { ptr -> ptr?.toKString().also { jack_free(ptr) } }
    .mapNotNull { ptr -> ptr?.toKString() }
    .toList()
