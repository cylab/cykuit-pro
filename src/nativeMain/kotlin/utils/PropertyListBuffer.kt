package utils

class PropertyListBuffer<V>(val size: Int) {

    private val dirty: MutableList<Boolean> = MutableList(size) { true }
    private val buffer: MutableList<V?> = MutableList(size) { null }

    operator fun get(index: Int) = buffer[index]
    operator fun set(index: Int, value: V?) {
        buffer[index] = value
        dirty[index] = true
    }

    val dirtyIndices
        get() = dirty.indices.filter { dirty[it] }.onEach { dirty[it] = false }
}
