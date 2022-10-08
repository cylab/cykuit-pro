package sequencer

import controller.*
import controller.events.*
import midi.activity.MidiActivity
import midi.activity.SwitchableMidiActivity
import utils.PropertyUpdater

open class MappedActivities(
    name: String = "unnamed MappedActivities",
    protected val mappedActivities: Map<Mapping, MidiActivity>,
    val exclusive: Boolean = false,
) : SwitchableMidiActivity(name) {

    private val buttonUpdater = PropertyUpdater<Button, Int>(1) { state = it }
    private val values = buttonUpdater[0]

    constructor(
        name: String = "unnamed MappedActivities",
        vararg mappedActivities: Pair<Mapping, MidiActivity>
    ) : this(name, mapOf(*mappedActivities), false)

    constructor(
        name: String = "unnamed MappedActivities",
        exclusive: Boolean = false,
        vararg mappedActivities: Pair<Mapping, MidiActivity>
    ) : this(name, mapOf(*mappedActivities), exclusive)

    constructor(
        name: String = "unnamed MappedActivities",
        mappedActivities: List<Pair<Mapping, MidiActivity>>,
        exclusive: Boolean = false,
    ) : this(name, mappedActivities.toMap(), exclusive)

    private val mappedControls: List<MidiControl> = mappedActivities.keys.mapNotNull { it.control }

    init {
        onClock.add(buttonUpdater)

        onStartup.add {
            if (mappedActivities.isNotEmpty()) {
                if (!exclusive) {
                    activate(this, mappedActivities.values.first())
                }
            }
        }

        onChange.add {
            mappedActivities.entries
                .flatMap { (mapping, activity) ->
                    mapping.modifiers.map { it to activity } + (mapping.control to activity)
                }
                .forEach { (control, activity) ->
                    when (control) {
                        is Button -> values[control] = when {
                            target == activity && activity.active -> control.ACTIVE
                            else -> control.INACTIVE
                        }
                        else -> {}
                    }
                }
        }

        onActivate.add {
            if (exclusive) {
                mappedActivities.values.forEach { it.deactivate(this) }
            }
        }

        onEvent.addFor<ControlPressed> { event ->
            if (event.control !in mappedControls) {
                return@addFor
            }
            mappedActivities.keys
                .firstOrNull { mapping -> mapping.control == event.control && mapping.modifiers.all { it.down } }
                ?.let {
                    activate(this, mappedActivities[it]!!)
                    if (exclusive) {
                        deactivate(this)
                    }
                    changed = true
                }
        }
    }
}
