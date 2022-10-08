package controller

import midi.activity.MidiActivity
import midi.api.*
import utils.DynamicValue
import utils.Value

class Layer private constructor(val name: String) {
    constructor(owner: MidiActivity, name: String = "${owner.name} Layer") : this(name) {
        this.owner = owner
    }
    var owner: MidiActivity? = null; private set
    val active : Boolean
        get() = owner?.active ?: true

    companion object {
        val BACKGROUND = Layer("Background")
    }

    override fun toString(): String {
        return "Layer(name='$name', owner=$owner, active=$active)"
    }
}

class LayeredValue<T>(initial: T) : MutableMap<Layer, Value<T?>?> by mutableMapOf() {
    var value: T = initial; private set

    operator fun set(key: Layer, newValue: T?) {
        put(key, Value { newValue })
    }

    fun dirtyAfterUpdate(clock: MidiClock): Boolean {
//        var output = "Updating $value"
        keys.filterNot { it.active }.forEach { remove(it) }
        val newValue = entries
//            .onEach { (layer, updater) ->
//                output += "\t-> $layer\n"
//            }
            .mapNotNull { (layer, updater) ->
                when (updater) {
                    null -> null
                    is DynamicValue -> updater.supply(clock)
                    else -> updater.invoke()
                }
            }
            .lastOrNull()

        if (newValue != null && newValue != value) {
//            println("$output\t -> $newValue")
            value = newValue
            return true
        }
        return false
    }
}
