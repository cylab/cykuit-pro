package sequencer.persistence.model

import kotlinx.serialization.Serializable

@Serializable
data class SequenceSave(
    val hello: String,
    val whatThe: String
)
