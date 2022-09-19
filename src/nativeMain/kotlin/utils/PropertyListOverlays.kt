package utils

import midi.api.MidiContext
import midi.api.MidiEvent
import midi.api.MidiFun

class PropertyListOverlays<T, V>(
    private val layerCount: Int,
    private val target: List<T>,
    private val getter: T.() -> V,
    private val setter: T.(V) -> Unit
) : MidiFun {
    private val layers = List<PropertyListBuffer<V?>>(layerCount) { PropertyListBuffer(target.size) }

    operator fun get(layer: Int) = layers[layer]

    override fun MidiContext.process(event: MidiEvent) {
        layers.flatMap { it.dirtyIndices }.distinct()
            .forEach { index ->
                layers.mapNotNull { it[index] }.lastOrNull()
                    ?.let { target[index].setter(it) }
            }
    }
}

