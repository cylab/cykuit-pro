package sequencer

import controller.Button
import controller.events.*
import midi.activity.MidiActivity
import midi.activity.SwitchableMidiActivity
import utils.PropertyOverlays

class ButtonMappedActivities(
    name: String = "unnamed ButtonActivities",
    private val mappedActivities: Map<Button, MidiActivity>
) : SwitchableMidiActivity(name) {

    private val overlays = PropertyOverlays<Button, Int>(2) { value = it }
    private val values = overlays[0]
    private val cursor = overlays[1]

    constructor(
        name: String = "unnamed ButtonActivities",
        vararg mappedActivities: Pair<Button, MidiActivity>
    ) : this(name, mapOf(*mappedActivities))

    init {
        onClock.add(overlays)

        onChange.add {
            println("On Change")
            mappedActivities.entries.forEach { (button, activity) ->
                values[button] = when {
                    target == activity -> 2
                    else -> 1
                }
            }
        }

        onEvent.add { event ->
            if (event is ButtonEvent<*> && event.button in mappedActivities.keys) {
                println("Button Pressed")
                when (event) {
                    is ButtonPressed<*> -> mappedActivities.entries
                        .firstOrNull { (button, _) -> button == event.button }
                        ?.let { (_, activity) -> switchTo(this, activity) }
                    is ButtonReleased<*> -> {}
                }
                changed = true
            }
        }
    }
}
