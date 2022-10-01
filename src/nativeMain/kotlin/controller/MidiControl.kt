package controller

sealed class MidiControl(
    name: String? = null,
) {
    val name: String = name ?: this::class.simpleName!!

    override fun toString(): String {
        return "${this::class.simpleName}(name='$name'"
    }
}

