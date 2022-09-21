package utils

import midi.api.*
import midi.core.process

class PropertyOverlays<T, V>(
    private val layerCount: Int,
    private val setter: T.(V) -> Unit
) : MidiFun, List<PropertyOverlay<T, V?>> by List(layerCount, { PropertyOverlay() }) {

    override fun MidiContext.process(event: MidiEvent) {
        forEach { it.process(this, event) }
        flatMap { it.dirtyKeys }.distinct().forEach { key ->
            mapNotNull { it[key] }.lastOrNull()?.let { key.setter(it) }
        }
    }
}

class PropertyOverlay<T, V> : MidiFun {
    private val dirty: MutableMap<T, Boolean> = mutableMapOf()
    private val buffer: MutableMap<T, V> = mutableMapOf()
    private val generators: MutableMap<T, MidiContext.() -> V?> = mutableMapOf()

    val dirtyKeys
        get() = dirty.keys.filter { dirty[it] == true }.onEach { dirty.remove(it) }

    operator fun get(key: T) = buffer[key]
    operator fun set(key: T, value: V?) {
        update(key, value)
        generators.remove(key)
        // for explicit updates, always set the value to dirty
        // this is a hack to force an update of the target, if it was changed from outside of this overlay
        dirty[key] = true
    }

    operator fun set(key: T, generator: MidiContext.() -> V?) {
        generators[key] = generator
    }

    override fun MidiContext.process(event: MidiEvent) {
        generators.entries.forEach { (key, generator) -> update(key, generator()) }
    }

    private fun update(key: T, value: V?) {
        if (buffer[key] != value) {
            when (value) {
                null -> buffer.remove(key)
                else -> buffer[key] = value
            }
            // set the value to dirty only, if the value changes (e.g. via a generator)
            dirty[key] = true
        }
    }
}

