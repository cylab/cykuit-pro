package sequencer

import controller.*
import controller.events.*
import midi.activity.MidiActivity
import midi.activity.SwitchableMidiActivity
import midi.api.MidiFun
import midi.api.NOOPEvent
import midi.core.process
import utils.PropertyUpdater

open class MappedActivities(
    name: String = "unnamed MappedActivities",
    protected open val activities: Map<Mapping, MidiFun> = emptyMap(),
    val exclusive: Boolean = false,
) : SwitchableMidiActivity(name) {

    private val buttonUpdater = PropertyUpdater<Button, Int>(1) { state = it }
    private val values = buttonUpdater[0]

    constructor(
        name: String = "unnamed MappedActivities",
        vararg mappedActivities: Pair<Mapping, MidiFun>
    ) : this(name, mapOf(*mappedActivities), false)

    constructor(
        name: String = "unnamed MappedActivities",
        exclusive: Boolean = false,
        vararg mappedActivities: Pair<Mapping, MidiFun>
    ) : this(name, mapOf(*mappedActivities), exclusive)

    constructor(
        name: String = "unnamed MappedActivities",
        mappedActivities: List<Pair<Mapping, MidiFun>>,
        exclusive: Boolean = false,
    ) : this(name, mappedActivities.toMap(), exclusive)

    init {
//        println("init $name - activities: ${activities.size}")
        onClock.add(buttonUpdater)

        onStartup.add {
            if (activities.isNotEmpty()) {
                if (!exclusive) {
                    activities.values
                        .filterIsInstance<MidiActivity>()
                        .firstOrNull()
                        ?.let { activity -> switchTo(this, activity) }
                }
            }
        }

        onChange.add {
            activities.entries
                .flatMap { (mapping, activity) ->
                    mapping.modifiers.map { it to activity } + (mapping.control to activity)
                }
                .forEach { (control, activity) ->
                    when {
                        control is Button && activity is MidiActivity -> values[control] = when {
                            target == activity && activity.active -> control.ACTIVE
                            else -> control.INACTIVE
                        }
                        else -> {}
                    }
                }
        }

        onActivate.add {
            println("activating $name")
            if (exclusive) {
                activities.values
                    .filterIsInstance<MidiActivity>()
                    .forEach { activity -> activity.deactivate(this) }
            }
        }

        onEvent.addFor<ControlPressed> { event ->
            val mappedControls: List<MidiControl> = activities.keys.mapNotNull { it.control }
            if (event.control !in mappedControls) {
                return@addFor
            }
            activities.keys
                .firstOrNull { mapping -> mapping.control == event.control && mapping.modifiers.all { it.down } }
                ?.let {
                    when(val activity = activities[it]!!) {
                        is MidiActivity -> {
                            switchTo(this, activity)
                            if (exclusive) {
                                deactivate(this)
                            }
                            changed = true
                        }
                        else -> activity.process(this, NOOPEvent)
                    }
                }
        }
    }
}
