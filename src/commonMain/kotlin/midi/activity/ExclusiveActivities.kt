package midi.activity

open class ExclusiveActivities(
    name: String = "unnamed",
    private val activities: List<MidiActivity>,
    active: Boolean = true
) : SwitchableMidiActivity(name, active) {
    init {
        onChange.add {
            println("$name changed!")
            activities.forEach { activity ->
                activity.deactivate()
                activity.onActivate.add { deactivateOthers(activity) }
            }
            onProcess.clear()
            onProcess.addAll(activities)
        }
    }

    private fun deactivateOthers(activated: MidiActivity) {
        activities
            .filterNot { it == activated }
            .forEach { it.deactivate() }
    }
}
