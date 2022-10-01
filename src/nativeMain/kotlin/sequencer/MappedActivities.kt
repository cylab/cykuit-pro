package sequencer

import controller.*
import controller.events.*
import midi.activity.MidiActivity
import midi.activity.SwitchableMidiActivity
import utils.PropertyUpdater

open class MappedActivities(
    name: String = "unnamed ButtonActivities",
    private val mappedActivities: Map<MidiControl, MidiActivity>,
    private val exclusive: Boolean = false,
) : SwitchableMidiActivity(name) {

    private val buttonUpdater = PropertyUpdater<Button, Int>(1) { value = it }
    private val values = buttonUpdater[0]

    constructor(
        name: String = "unnamed ButtonActivities",
        vararg mappedActivities: Pair<MidiControl, MidiActivity>
    ) : this(name, mapOf(*mappedActivities), false)

    constructor(
        name: String = "unnamed ButtonActivities",
        exclusive: Boolean = false,
        vararg mappedActivities: Pair<MidiControl, MidiActivity>
    ) : this(name, mapOf(*mappedActivities), exclusive)

    constructor(
        name: String = "unnamed ButtonActivities",
        mappedActivities: List<Pair<MidiControl, MidiActivity>>,
        exclusive: Boolean = false,
    ) : this(name, mappedActivities.toMap(), exclusive)

    init {
        onClock.add(buttonUpdater)

        onStartup.add {
            if(mappedActivities.isNotEmpty()) {
                if(!exclusive) {
                    switchTo(this, mappedActivities.values.first())
                }
            }
        }

        onChange.add {
            mappedActivities.entries.forEach { (control, activity) ->
                when(control) {
                    is Button -> values[control] = when {
                        target == activity && activity.active -> control.active
                        else -> control.inactive
                    }
                    else -> {}
                }
            }
        }

        onActivate.add {
            if(exclusive) {
                mappedActivities.values.forEach { it.deactivate(this) }
            }
        }

        onEvent.add { event ->
            if (event is ButtonEvent && event.button in mappedActivities.keys) {
                when (event) {
                    is ButtonPressed -> mappedActivities.keys
                        .firstOrNull {it == event.button }
                        ?.let { switchTo(this, mappedActivities[it]!!) }
                    is ButtonReleased -> {}
                }
                if(exclusive) {
                    deactivate(this)
                }
                changed = true
            }
            if (event is PadEvent && event.pad in mappedActivities.keys) {
                when (event) {
                    is PadPressed -> mappedActivities.keys
                        .firstOrNull {it == event.pad }
                        ?.let { switchTo(this, mappedActivities[it]!!) }
                    is PadReleased -> {}
                }
                if(exclusive) {
                    deactivate(this)
                }
                changed = true
            }
        }
    }
}
