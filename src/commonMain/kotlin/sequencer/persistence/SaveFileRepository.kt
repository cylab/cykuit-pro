package sequencer.persistence

import com.soywiz.korio.async.use
import com.soywiz.korio.file.VfsOpenMode.CREATE
import com.soywiz.korio.file.std.*
import com.soywiz.korio.lang.Environment
import com.soywiz.korio.stream.writeString
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import sequencer.persistence.model.SequenceSave

class SaveFileRepository {
    private val saveVfs = run {
        val homeDir = Environment["HOME"] ?: "."
        val saveDir = Environment["CYKUIT_SAVES"]?.replace("~", homeDir)
        localVfs(saveDir ?: "./saves")
    }

    fun load(name: String) : SequenceSave {
        return SequenceSave(hello = "", whatThe = "")
    }

    fun save(name: String, sequence: SequenceSave) : Unit = runBlocking {
        println("hello: ${sequence.hello}")
        val output = Json.encodeToString(sequence)
        println("${saveVfs.path}: $output")
        saveVfs.mkdir()
        saveVfs["$name.csq"].open(CREATE).use {
            writeString(output)
        }
    }
}
